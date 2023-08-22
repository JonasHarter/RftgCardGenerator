package gen.set;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import gen.set.definitions.Card;
import gen.set.definitions.Dice;
import gen.set.definitions.Face;
import gen.set.definitions.Phase;
import gen.set.definitions.Set;
import gen.util.StringSplitter;
import gen.util.StringSplitter.StringType;

public class SetBuilder {

	private Set set;
	private Path outputFolder;

	public SetBuilder(Set set, Path outputFolder)  {
		this.set = set;
		this.outputFolder = outputFolder;
	}

	public void buildFaces() throws SetBuilderException {
		int i = 0;
		for (Card card : set.getCards()) {
			for (Face face : card.getFaces()) {
				try {
				FaceBuilder builder = new FaceBuilder(face);
				builder.buildFace();
				builder.writeToFile(set.getName(), i);
				} catch (Exception e)
				{
					throw new SetBuilderException("Failed to build face: " + face.getName(), e);
				}
			}
			i++;
		}
	}

	private class FaceBuilder {

		private Face face;
		private DocumentBuilder documentBuilder;
		private Document document;
		private Node parentNode;

		public FaceBuilder(Face face) throws Exception {
			this.face = face;
			// Load main
			InputStream inputStream = loadRessource("MainDocument.xml");
			DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
			docbf.setNamespaceAware(true);
			documentBuilder = docbf.newDocumentBuilder();
			document = documentBuilder.parse(inputStream);
			parentNode = (Node) document.getDocumentElement();
		}

		private Document buildFace() throws Exception {
			// TextBgs
			parentNode.appendChild(loadFragment("HeaderBG.xml"));
			parentNode.appendChild(loadFragment("TextBG.xml"));
			// Symbol and Colour
			Node symbolNode = null;
			if (face.getColor() == null) {
				symbolNode = loadFragment("Rhombus.xml");
			} else {
				symbolNode = loadFragment("Circle.xml");
				Node styleAttribute = symbolNode.getAttributes().getNamedItem("style");
				String styleText = styleAttribute.getTextContent();
				styleText = styleText.replace("fill:#bfbfbf", "fill:" + face.getColor().getColorHex());
				styleAttribute.setTextContent(styleText);

			}
			parentNode.appendChild(symbolNode);
			// Cost Digit
			Node digitNode = loadFragment("Cost.xml");
			digitNode.setTextContent(face.getCost().toString());
			parentNode.appendChild(digitNode);
			if (face.getTextSp() != null)
				parentNode.appendChild(loadFragment("CostPlus.xml"));
			// Phases
			if (face.getPhase() != null) {
				for (int phaseI = 0; phaseI < face.getPhase().size(); phaseI++) {
					Phase phase = face.getPhase().get(phaseI);
					int y = phaseI * 50;
	                parentNode.appendChild(transformTranslateFragment(loadFragment("PhasesIconBG.xml"), 0, y));
	                parentNode.appendChild(transformTranslateFragment(loadFragment("PhasesIcon" + phase.getValue().toString() + ".xml"), 0, y));
				}
			}
			// Header
			Node headerTextNode = loadFragment("HeaderText.xml");
			headerTextNode.getChildNodes().item(3).setTextContent(face.getName());
			parentNode.appendChild(headerTextNode);
			// Rules with coloured text
			Node rulesTextNode = loadFragment("RulesText.xml");
			insertDiceTextColoured(rulesTextNode.getChildNodes().item(3), face.getText());
			if( face.getTextSp() != null)
				insertDiceTextColoured(rulesTextNode.getChildNodes().item(5), "⬢: " + face.getTextSp());
			parentNode.appendChild(rulesTextNode);
			// Border
			parentNode.appendChild(loadFragment("Border.xml"));
			return document;
		}

		private InputStream loadRessource(String fileName) {
			return SetBuilder.class.getResourceAsStream("/gen/fragments/" + fileName);
		}

		private Node loadFragment(String fileName) throws Exception {
			InputStream inputStreamBorder = loadRessource(fileName);
			Node fragmentNode = documentBuilder.parse(new InputSource(inputStreamBorder)).getDocumentElement();
			fragmentNode = document.importNode(fragmentNode, true);
			return fragmentNode;
		}
		
		private Node transformTranslateFragment(Node fragment, int x, int y)
		{
			//translate(153 -79)"
			Attr transformAttr = document.createAttribute("transform");
			transformAttr.setTextContent("translate(" + x + " " + y + ")");
			fragment.getAttributes().setNamedItem(transformAttr);
			return fragment;
		}

		private void writeToFile(String prefix, Integer i) throws Exception {
			DOMSource source = new DOMSource(document);
			FileOutputStream out = new FileOutputStream(
					outputFolder.resolve(prefix + " - " + i + " - " + face.getName() + ".svg").toFile());
			StreamResult result = new StreamResult(out);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);
		}

		private void insertDiceTextColoured(Node flowParamNode, String text) {
			flowParamNode.setTextContent("");
			List<String> diceNames = new ArrayList<>();
			for (Dice dice : Dice.values()) {
				diceNames.add(dice.getValue());
			}
			var splittedText = StringSplitter.splitForKeywords(text, diceNames);
			for (var splittedTuple : splittedText) {
				if (splittedTuple.x() == StringType.NORMAL) {
					Text textNode = document.createTextNode(splittedTuple.y());
					flowParamNode.appendChild(textNode);
				} else if (splittedTuple.x() == StringType.KEYWORD) {
					Dice dice = Dice.getByValue(splittedTuple.y());
					Element element = document.createElement("flowSpan");
					element.setAttribute("style", "fill:" + dice.getColor().getColorHex());
					element.setTextContent(dice.getValue());
					flowParamNode.appendChild(element);
				}
			}
		}
	}
	
	public static class SetBuilderException extends Exception {
		private static final long serialVersionUID = -9078438810344178993L;

		private SetBuilderException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}