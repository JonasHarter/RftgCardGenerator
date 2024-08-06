package gen.set.card;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import gen.set.FileGeneratorData;
import gen.set.definitions.Card;
import gen.set.definitions.Face;
import gen.util.SvgBuilder;
import gen.util.Tuple;

public class DoubleSizeCardBuilder extends CardBuilder {

	public DoubleSizeCardBuilder(Card card, FileGeneratorData fileGeneratorData) {
		super(card, fileGeneratorData);
	}

	@Override
	public List<FileGeneratorData> build() throws Exception {
		List<FileGeneratorData> returnList = new ArrayList<>();
		CardBuilderInternal bleedBuilder = new CardBuilderInternal();
		bleedBuilder.generateDocument();
		int i = 0;
		for (Tuple<Face, Node> tuple : generateNodes()) {
			Node node = tuple.y();
			Node importedNode = bleedBuilder.getDocument().importNode(node, true);
			bleedBuilder.translateNode(importedNode, 3 + (i * 63), 3);
			bleedBuilder.getBodyNode().appendChild(importedNode);
			i++;
		}
		bleedBuilder.generateDocumentInternal();
		FileGeneratorData newData = new FileGeneratorData(fileGeneratorData);
		newData.setDocument(bleedBuilder.getDocument());
		newData.setCard(card);
		newData.setFaceSideString("");
		returnList.add(newData);
		return returnList;
	}

	private class CardBuilderInternal extends SvgBuilder {

		@Override
		protected String getMainXmlFilename() {
			return "MainDouble.xml";
		}

		@Override
		protected void generateDocumentInternal() throws Exception {
			Node borderSafeNode = loadFragment("BleedDouble.xml");
			bodyNode.appendChild(borderSafeNode);
		}

		@Override
		protected String getRessourceFolder() {
			return "/gen/set/card/fragments/";
		}

	}

}
