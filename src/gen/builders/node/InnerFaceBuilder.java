package gen.builders.node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import gen.set.definitions.Dice;
import gen.set.definitions.Face;
import gen.set.definitions.Phase;
import gen.util.StringSplitter;
import gen.util.StringSplitter.StringType;
import gen.util.Stuff;

public class InnerFaceBuilder extends NodeBuilder {

	private final static String FONT = "font-family:'DM Serif Display';-inkscape-font-specification:'DM Serif Display';";
	private Face face;
	private Boolean isStartingPlanet;
	private String id;
	private Path imageFolder;

	public InnerFaceBuilder(Face face, Boolean isStartingPlanet, String id, Path imageFolder) {
		super();
		this.face = face;
		this.isStartingPlanet = isStartingPlanet;
		this.id = id;
		this.imageFolder = imageFolder;
	}

	@Override
	protected void buildInternal() throws Exception {
		createImage();
		createUpperBg();
		createLowerBg();
		createHeaderText();
		createSymbol();
		createCost();
		createPhaseIcon();
		createLowerText();
		createId();
		createStartingPlanetMarker();
	}

	private void createImage() {
		Element element = document.createElement("image");
		element.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		element.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		element.setAttribute("width", "63");
		element.setAttribute("height", "88");
		element.setAttribute("x", "0");
		Integer y = 0;
		if (face.getImageOffset() != null) {
			y = face.getImageOffset();
		}
		element.setAttribute("y", y.toString());
		element.setAttribute("xlink:href", "data:image/png;base64," + getImage() + ";");
		rootNode.appendChild(element);
	}

	private String getImage() {
		var imageFileOpt = Stuff.lookForImage(imageFolder, face.getName());
		if (!imageFileOpt.isPresent())
			throw new RuntimeException("No image for: " + face.getName());
		String encodeBytes;
		try {
			encodeBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(imageFileOpt.get().toPath()));
		} catch (IOException e) {
			throw new RuntimeException("Failed image for: " + face.getName());
		}
		return encodeBytes;
	}

	private void createCost() {
		Element text = document.createElement("text");
		text.setAttribute("style", "font-size:10;");
		text.setAttribute("x", "8");
		text.setAttribute("y", "25.5");
		text.setTextContent(face.getCost().toString());
		rootNode.appendChild(text);
		if (face.getTextSp() != null) {
			Element plus = document.createElement("text");
			plus.setAttribute("style", "font-size:8;");
			plus.setAttribute("x", "12");
			plus.setAttribute("y", "22");
			plus.setTextContent("+");
			rootNode.appendChild(plus);
		}
	}

	private void createSymbol() {
		Element element;
		if (face.getColor() == null) {
			element = document.createElement("rect");
			element.setAttribute("x", "11");
			element.setAttribute("y", "15");
			element.setAttribute("width", "10");
			element.setAttribute("height", "10");
			element.setAttribute("style", "fill:#e0d0a7;stroke:#373737;stroke-width:0.25");
			element.setAttribute("transform", "rotate(45, 11, 15)");
		} else {
			element = document.createElement("circle");
			element.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			element.setAttribute("cx", "11");
			element.setAttribute("cy", "22");
			element.setAttribute("r", "7");
			element.setAttribute("style",
					"fill:" + face.getColor().getColorHex() + ";stroke:#373737;stroke-width:0.25");
		}
		rootNode.appendChild(element);
	}

	private void createId() {
		if (id == null || id.length() == 0)
			return;
		Element text = document.createElement("text");
		text.setAttribute("style", "font-size:2;text-align:left;");
		text.setAttribute("x", "3.5");
		text.setAttribute("y", "84.5");
		text.setTextContent(id);
		rootNode.appendChild(text);
	}

	private void createStartingPlanetMarker() {
		if (!isStartingPlanet)
			return;
		Element element = document.createElement("path");
		element.setAttribute("style", "fill:#000000;stroke:#ffffff;stroke-width:0;stroke-opacity:0");
		element.setAttribute("d", "M 63,0 63,10 53,0 Z");
		rootNode.appendChild(element);
	}

	private void createLowerText() {
		Element flowRoot = document.createElement("flowRoot");
		flowRoot.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		flowRoot.setAttribute("style", "font-size:2pt;text-align:center;" + FONT);
		Element flowRegion = document.createElement("flowRegion");
		flowRoot.appendChild(flowRegion);
		Element path = document.createElement("rect");
		path.setAttribute("width", "55");
		path.setAttribute("height", "20");
		path.setAttribute("x", "4");
		path.setAttribute("y", "67");
		flowRegion.appendChild(path);
		if (face.getText() != null) {
			Element flowPara1 = document.createElement("flowPara");
			insertDiceTextColoured(flowPara1, face.getText());
			flowRoot.appendChild(flowPara1);
		}
		if (face.getTextSp() != null) {
			Element flowPara2 = document.createElement("flowPara");
			insertDiceTextColoured(flowPara2, "⬢: " + face.getTextSp());
			flowRoot.appendChild(flowPara2);
		}
		rootNode.appendChild(flowRoot);
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

	private void createPhaseIcon() {
		if (face.getPhase() == null) {
			return;
		}
		int yOffset = 0;
		for (Phase phase : face.getPhase()) {
			createPhaseIconInternal(phase, yOffset);
			yOffset += 13;
		}
	}

	private void createPhaseIconInternal(Phase phase, int yOffset) {

		Element overGroup = document.createElement("g");
		translateNode(overGroup, 0, yOffset);

		Element background = document.createElement("rect");
		background.setAttribute("x", "49");
		background.setAttribute("y", "17");
		background.setAttribute("width", "10");
		background.setAttribute("height", "10");
		background.setAttribute("style", "fill:#000000;stroke:#373737;stroke-width:0.25");
		overGroup.appendChild(background);

		Element icon = document.createElement("g");
		if (phase == Phase.ASSIGN) {
			Element path1 = document.createElement("path");
			path1.setAttribute("style",
					"fill:#ffffff;stroke:#ffffff;stroke-width:3;stroke-linejoin:round;stroke-linecap:round");
			path1.setAttribute("d", "m 111.10324,75.46937 -7.31027,1.696021 1.16892,-5.932635 z");
			Element path2 = document.createElement("path");
			path2.setAttribute("style",
					"fill:none;stroke:#ffffff;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter");
			path2.setAttribute("d",
					"m 67.916921,118.26094 c 1.043179,-0.81367 5.707057,-3.44612 9.618699,-3.61463 10.483552,-0.45161 15.766888,14.12641 7.24278,14.23327 -8.524108,0.10686 -2.71894,-14.61073 6.264762,-14.22598 3.417313,0.14636 6.403681,1.19152 7.821046,1.8351");
			Element path1Group = document.createElement("g");
			Element path2Group = document.createElement("g");
			path1Group.setAttribute("transform", "translate(-5,42)");
			path1Group.appendChild(path1);
			path2Group.appendChild(path2);
			icon.appendChild(path1Group);
			icon.appendChild(path2Group);
			icon.setAttribute("transform", "translate(34,-5.5) scale(0.23, 0.23)");
		} else if (phase == Phase.SCOUT) {

			Element group = document.createElement("g");
			group.setAttribute("transform", "translate(46,18) scale(0.23,0.23)");

			Element ellipse = document.createElement("ellipse");
			ellipse.setAttribute("cx", "35");
			ellipse.setAttribute("cy", "18");
			ellipse.setAttribute("rx", "7.5");
			ellipse.setAttribute("ry", "7.5");
			ellipse.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			Element path = document.createElement("path");
			path.setAttribute("d",
					"M 55,18 C 55,18 45.07503,28.5 35,28.5 24.92497,28.5 15,18 15,18 c 0,0 9.92497,-9.5 20,-9.5 10.07503,-2e-7 20,9.5 20,9.5 z");
			path.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			group.appendChild(ellipse);
			group.appendChild(path);
			icon.appendChild(group);
		} else if (phase == Phase.DEVELOP) {
			Element path = document.createElement("path");
			path.setAttribute("d", "M 54,18 57,22 54,26 51,22 Z");
			path.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:0.8;");

			icon.appendChild(path);
		} else if (phase == Phase.SETTLE) {
			Element circle = document.createElement("circle");
			circle.setAttribute("cx", "54");
			circle.setAttribute("cy", "22");
			circle.setAttribute("r", "4");
			circle.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:0.8");

			icon.appendChild(circle);
		} else if (phase == Phase.PRODUCE) {
			Element group = document.createElement("g");
			group.setAttribute("transform", "translate(46,-7) scale(0.23,0.23)");

			Element ellipse = document.createElement("ellipse");
			ellipse.setAttribute("cx", "35.01973");
			ellipse.setAttribute("cy", "111.12118");
			ellipse.setAttribute("rx", "9.9637756");
			ellipse.setAttribute("ry", "4.1795826");
			ellipse.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			Element path1 = document.createElement("path");
			path1.setAttribute("d",
					"M 25.083377,111.01201 24.930499,140 c 6.843946,4.15975 12.623314,4.10597 19.785764,0 l 0.111235,-29.32171");
			path1.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			Element path2 = document.createElement("path");
			path2.setAttribute("d", "m 25,127 c 6.550614,3.24948 13.227005,2.97719 20,0");
			path2.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			Element path3 = document.createElement("path");
			path3.setAttribute("d", "m 35,114.718 v 28.63544");
			path3.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			group.appendChild(ellipse);
			group.appendChild(path1);
			group.appendChild(path2);
			group.appendChild(path3);
			icon.appendChild(group);

		} else if (phase == Phase.SHIP) {

			Element group = document.createElement("g");
			group.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			group.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			group.setAttribute("transform", "translate(38,-14.5) scale(0.22,0.22)");

			Element path1 = document.createElement("path");
			path1.setAttribute("d",
					"m 63.392133,168.68688 c 0,0 20.653085,-22.07473 28.165414,-22.36182 0.17551,7.26565 -19.908424,29.64732 -19.908424,29.64732 z");
			path1.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			Element path2 = document.createElement("path");
			path2.setAttribute("id", "fin");
			path2.setAttribute("d",
					"m 79.93232,166.0914 c 4.793296,4.30629 -1.54848,16.40681 -5.650044,20.22439 2.72924,-4.54519 4.63011,-13.08011 1.645732,-15.19945 z");
			path2.setAttribute("style", "fill:none;stroke:#ffffff;stroke-width:3;");

			Element use = document.createElement("use");
			use.setAttribute("x", "0");
			use.setAttribute("y", "0");
			use.setAttribute("xlink:href", "#fin");
			use.setAttribute("transform", "matrix(0,-1,-1,0,237.99294,239.81828)");

			group.appendChild(path1);
			group.appendChild(path2);
			group.appendChild(use);
			icon.appendChild(group);
		}
		overGroup.appendChild(icon);
		rootNode.appendChild(overGroup);
	}

	private void createUpperBg() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "63");
		element.setAttribute("height", "22");
		element.setAttribute("style", "fill:#bfbfbf;");
		
		Element border = document.createElement("rect");
		border.setAttribute("x", "0");
		border.setAttribute("y", "21.75");
		border.setAttribute("width", "63");
		border.setAttribute("height", "0.25");
		border.setAttribute("style", "fill:#373737;");
		
		rootNode.appendChild(element);
		rootNode.appendChild(border);
	}

	private void createLowerBg() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "66");
		element.setAttribute("width", "63");
		element.setAttribute("height", "22");
		element.setAttribute("style", "fill:#bfbfbf;");
		
		Element border = document.createElement("rect");
		border.setAttribute("x", "0");
		border.setAttribute("y", "66");
		border.setAttribute("width", "63");
		border.setAttribute("height", "0.25");
		border.setAttribute("style", "fill:#373737;");
		
		rootNode.appendChild(element);
		rootNode.appendChild(border);
	}

	private void createHeaderText() {
		String headerText = face.getName();

		Element flowRoot = document.createElement("flowRoot");
		flowRoot.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		flowRoot.setAttribute("style", "font-size:3pt;text-align:center;" + FONT);
		Element flowRegion = document.createElement("flowRegion");
		flowRoot.appendChild(flowRegion);
		Element path = document.createElement("path");
		// path.setAttribute("d", "M 4,4 59,4 59,22 4,22 Z");
		path.setAttribute("d", "M 3,3 60,3 60,22 3,22 Z");
		flowRegion.appendChild(path);
		Element flowPara = document.createElement("flowPara");
		flowPara.setTextContent(headerText);
		flowRoot.appendChild(flowPara);

		int offset = 5;
//		if (headerText.length() <= 35)
//			offset = 5;
		translateNode(flowRoot, 0, offset);

		rootNode.appendChild(flowRoot);
	}
}