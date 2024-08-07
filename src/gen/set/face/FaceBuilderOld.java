package gen.set.face;

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

import gen.set.SetBuilder;
import gen.set.definitions.Dice;
import gen.set.definitions.Face;
import gen.set.definitions.Phase;
import gen.set.definitions.Set;
import gen.util.StringSplitter;
import gen.util.StringSplitter.StringType;
import gen.util.Stuff;

public class FaceBuilderOld {
	
	private Set set;
	private Path imageFolder;
	private Path outputFolder;

	private Face face;
	private Integer id;
	private DocumentBuilder documentBuilder;
	private Document document;
	private Node parentNode;

	private Boolean isStartingTile = false;
	private Boolean isStartingPlanet = false;
	private Boolean isSecondFace = false;

	public FaceBuilderOld(Set set, Path imageFolder, Path outputFolder, Face face, Integer id) throws Exception {
		this.set = set;
		this.imageFolder = imageFolder;
		this.outputFolder = outputFolder;
		this.face = face;
		this.id = id;
		// Load main
		InputStream inputStream = loadRessource("MainDocument.xml");
		DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
		docbf.setNamespaceAware(true);
		documentBuilder = docbf.newDocumentBuilder();
		document = documentBuilder.parse(inputStream);
		parentNode = (Node) document.getDocumentElement();
		parentNode = parentNode.getChildNodes().item(1);
	}

	public void setIsStartingTile(Boolean isStartingTile) {
		this.isStartingTile = isStartingTile;
	}

	public void setIsStartingPlanet(Boolean isStartingPlanet) {
		this.isStartingPlanet = isStartingPlanet;
	}

	public void setIsSecondFace(Boolean isSecondFace) {
		this.isSecondFace = isSecondFace;
	}

	public Document buildFace(Integer doubleTileId) throws Exception {
		// Image
		Node imageFragment = loadFragment("Image.xml");
		if (face.getImageOffset() != null) {
			Node yAtt = imageFragment.getAttributes().getNamedItem("y");
			Integer yValue = Integer.parseInt(yAtt.getTextContent());
			yValue += face.getImageOffset();
			yAtt.setTextContent(yValue.toString());
		}
		var imgNode = addImage(imageFragment, face.getName());
		if (imgNode.isPresent())
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
//		// Phases
		if (face.getPhase() != null) {
			for (int phaseI = 0; phaseI < face.getPhase().size(); phaseI++) {
				Phase phase = face.getPhase().get(phaseI);
				int y = phaseI * 50;
				parentNode.appendChild(transformTranslateFragment(loadFragment("PhasesIconBG.xml"), 0, y));
				parentNode.appendChild(transformTranslateFragment(
						loadFragment("PhasesIcon" + phase.getValue().toString() + ".xml"), 0, y));
			}
		}
		// Header
		String name = face.getName();
		Node headerTextNode = loadFragment("HeaderText.xml");
		headerTextNode.getChildNodes().item(3).setTextContent(name);
		int offset = 5;
		if (name.length() < 30)
			offset = 15;
		headerTextNode = transformTranslateFragment(headerTextNode, 0, offset);
		parentNode.appendChild(headerTextNode);
		// Rules with coloured text
		Node rulesTextNode = loadFragment("RulesText.xml");
		// setTextSize
		String rulesText = face.getText();
		Boolean hasRulesText = rulesText != null;
		String pointsText = face.getTextSp();
		Boolean hasPointsText = pointsText != null;
		Integer textLength = 0;
		if (hasRulesText) {
			textLength += rulesText.length();
		}
		if (hasPointsText) {
			pointsText = "⬢: " + pointsText;
			textLength += pointsText.length();
		}
		Integer textSizePt = 8;
		setTextSize(rulesTextNode, textSizePt);
		if (hasRulesText)
			insertDiceTextColoured(rulesTextNode.getChildNodes().item(3), rulesText);
		if (hasPointsText)
			insertDiceTextColoured(rulesTextNode.getChildNodes().item(5), pointsText);
		parentNode.appendChild(rulesTextNode);
		// ID
		if (isStartingTile || isStartingPlanet) {
			Node idFragment = loadFragment("Id.xml");
			if (!isSecondFace)
				idFragment = transformTranslateFragment(idFragment, 50, 0);
			String idString = "";
			if (isStartingTile)
				idString = doubleTileId.toString();
			idFragment.getChildNodes().item(3).getChildNodes().item(3).setTextContent(idString);
			parentNode.appendChild(idFragment);
		}
		// Bottom Id
		Node bottomIdFragment = loadFragment("BottomId.xml");
		StringBuilder bottomIdBuilder = new StringBuilder();
		bottomIdBuilder.append(set.getLetter());
		bottomIdBuilder.append("-");
		bottomIdBuilder.append(String.format("%02d", id));
		bottomIdBuilder.append("-");
		if (!isSecondFace)
			bottomIdBuilder.append("A");
		else
			bottomIdBuilder.append("B");
		bottomIdFragment.getChildNodes().item(1).getChildNodes().item(3).setTextContent(bottomIdBuilder.toString());
		parentNode.appendChild(bottomIdFragment);
		// Border
		Node borderBleedNode = loadFragment("BorderBleed.xml");
		Node borderSafeNode = loadFragment("Border.xml");
		parentNode.appendChild(borderBleedNode);
		parentNode.appendChild(borderSafeNode);
		return document;
	}

	private InputStream loadRessource(String fileName) {
		return SetBuilder.class.getResourceAsStream("/gen/set/fragments/" + fileName);
	}

	private Node loadFragment(String fileName) throws Exception {
		InputStream inputStreamBorder = loadRessource(fileName);
		Node fragmentNode = documentBuilder.parse(new InputSource(inputStreamBorder)).getDocumentElement();
		fragmentNode = document.importNode(fragmentNode, true);
		return fragmentNode;
	}

	private Optional<Node> addImage(Node imageNode, String name) throws IOException {
		var imageFileOpt = Stuff.lookForImage(imageFolder, name);
		if (!imageFileOpt.isPresent())
			return Optional.empty();
		String encodeBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(imageFileOpt.get().toPath()));
		Node hrefAttribute = imageNode.getAttributes().getNamedItem("xlink:href");
		hrefAttribute.setTextContent("data:image/png;base64," + encodeBytes);
		return Optional.of(imageNode);
	}

	private Node transformTranslateFragment(Node fragment, int x, int y) {
		Attr transformAttr = document.createAttribute("transform");
		transformAttr.setTextContent("translate(" + x + " " + y + ")");
		fragment.getAttributes().setNamedItem(transformAttr);
		return fragment;
	}

	private void writeToFile(String prefix) throws Exception {
		DOMSource source = new DOMSource(document);
		StringBuilder fileNameBuilder = new StringBuilder();
		fileNameBuilder.append(prefix);
		fileNameBuilder.append(" - ");
		fileNameBuilder.append(String.format("%02d", id));
		if (!isSecondFace)
			fileNameBuilder.append("A");
		else
			fileNameBuilder.append("B");
		fileNameBuilder.append(" - ");
		fileNameBuilder.append(face.getName());
		fileNameBuilder.append(".svg");
		FileOutputStream out = new FileOutputStream(outputFolder.resolve(fileNameBuilder.toString()).toFile());
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
				Text textNode = document.createTextNode(splittedTuple.y());
				flowParamNode.appendChild(textNode);
				Dice dice = Dice.getByValue(splittedTuple.y());
				Element element = document.createElement("flowSpan");
				element.setAttribute("style", "stroke:black;stroke-width:.25px;fill:" + dice.getColor().getColorHex());
				element.setTextContent("●");
				flowParamNode.appendChild(element);
			}
		}
	}

	private void setTextSize(Node flowParamNode, Integer pt) {
		Node styleAttribute = flowParamNode.getAttributes().getNamedItem("style");
		String styleText = styleAttribute.getTextContent();
		String styleTextModded = styleText.replace("font-size:8pt", "font-size:" + pt + "pt");
		styleAttribute.setTextContent(styleTextModded);
	}
}