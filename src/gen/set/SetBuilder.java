package gen.set;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
import gen.set.definitions.CardType;
import gen.set.definitions.Dice;
import gen.set.definitions.Face;
import gen.set.definitions.Phase;
import gen.set.definitions.Set;
import gen.util.StringSplitter;
import gen.util.StringSplitter.StringType;
import gen.util.Stuff;

public class SetBuilder {

	private Set set;
	private Path outputFolder;
	private Path imageFolder;
	private Integer pairStartingNumber;

	public SetBuilder(Set set, Integer pairStartingNumber, Path outputFolder, Path imageFolder)  {
		this.set = set;
		this.outputFolder = outputFolder;
		this.imageFolder = imageFolder.resolve(set.getName());
		this.pairStartingNumber = pairStartingNumber;
	}

	public Integer buildFaces() throws SetBuilderException {
		System.out.println("###");
		System.out.println(set.getName());
		int i = 0;
		Integer doubleTileId = 1;
		if(pairStartingNumber != null)
			doubleTileId = pairStartingNumber;
		for (Card card : set.getCards()) {
			for (Face face : card.getFaces()) {
				try {
					FaceBuilder builder = new FaceBuilder(face);
					if(CardType.STARTER_DOUBLE.equals(card.getCardType()))
					{
						builder.buildFace(doubleTileId);
						if(doubleTileId < 0)
							doubleTileId = (doubleTileId * -1) + 1;
						else
							doubleTileId *= -1;
					}
					else if(card.getFaces().size() == 1)
						builder.buildFace(0);
					else
						builder.buildFace(null);
					builder.writeToFile(set.getName(), i);
				} catch (Exception e)
				{
					throw new SetBuilderException("Failed to build face: " + face.getName(), e);
				}
			}
			i++;
		}
		return doubleTileId;
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

		private Document buildFace(Integer doubleTileId) throws Exception {
			// Image
			var imgNode = addImage(loadFragment("Image.xml"), face.getName());
			if(imgNode.isPresent())
				parentNode.appendChild(imgNode.get());
			else
				System.out.println("No image for: " + face.getName());
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
			// ID
			if(doubleTileId != null)
			{
				Node idFragment = loadFragment("Id.xml");
				if(doubleTileId > 0)
					idFragment = transformTranslateFragment(idFragment, 182, 0);
				else if(doubleTileId < 0)
					doubleTileId *= -1;
				String idString = "";
				if(doubleTileId != 0)
					idString = doubleTileId.toString();
				idFragment.getChildNodes().item(3).getChildNodes().item(3).setTextContent(idString);
				parentNode.appendChild(idFragment);
			}
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
		
		private Optional<Node> addImage(Node imageNode, String name) throws IOException
		{
			var imageFileOpt = Stuff.lookForImage(imageFolder, name);
			if(!imageFileOpt.isPresent())
				return Optional.empty();
			String encodeBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(imageFileOpt.get().toPath()));
			Node  hrefAttribute = imageNode.getAttributes().getNamedItem("xlink:href");
			hrefAttribute.setTextContent("data:image/png;base64," + encodeBytes);
			return Optional.of(imageNode);
		}
		
		private Node transformTranslateFragment(Node fragment, int x, int y)
		{
			Attr transformAttr = document.createAttribute("transform");
			transformAttr.setTextContent("translate(" + x + " " + y + ")");
			fragment.getAttributes().setNamedItem(transformAttr);
			return fragment;
		}

		private void writeToFile(String prefix, Integer i) throws Exception {
			DOMSource source = new DOMSource(document);
			FileOutputStream out = new FileOutputStream(
					outputFolder.resolve(prefix + " - " + String.format("%03d", i) + " - " + face.getName() + ".svg").toFile());
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
