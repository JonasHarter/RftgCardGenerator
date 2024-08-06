package gen.set.card.face;

import org.w3c.dom.Node;

import gen.set.definitions.Face;
import gen.util.SvgBuilder;

public class FaceBuilder extends SvgBuilder {

	private Face face;

	public FaceBuilder(Face face) throws Exception {
		super();
		this.face = face;
	}

	@Override
	protected String getRessourceFolder() {
		return "/gen/set/card/face/fragments/";
	}

	@Override
	protected void generateDocumentInternal() throws Exception {
		// TextBG
		bodyNode.appendChild(loadFragment("HeaderBG.xml"));
		bodyNode.appendChild(loadFragment("TextBG.xml"));
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
		bodyNode.appendChild(symbolNode);
		// Header
		String name = face.getName();
		Node headerTextNode = loadFragment("HeaderText.xml");
		headerTextNode.getChildNodes().item(3).setTextContent(name);
		int offset = 5;
		if (name.length() < 30)
			offset = 15;
		headerTextNode = translateNode(headerTextNode, 0, offset);
		bodyNode.appendChild(headerTextNode);
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
//		setTextSize(rulesTextNode, textSizePt);
//		if (hasRulesText)
//			insertDiceTextColoured(rulesTextNode.getChildNodes().item(3), rulesText);
//		if (hasPointsText)
//			insertDiceTextColoured(rulesTextNode.getChildNodes().item(5), pointsText);
//		parentNode.appendChild(rulesTextNode);
		// TODO steal from old
		Node borderSafeNode = loadFragment("Border.xml");
		bodyNode.appendChild(borderSafeNode);
	}

	@Override
	protected String getMainXmlFilename() {
		return "Main.xml";
	}
}
