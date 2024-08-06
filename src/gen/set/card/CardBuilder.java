package gen.set.card;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import gen.set.FileGeneratorData;
import gen.set.card.face.FaceBuilder;
import gen.set.definitions.Card;
import gen.set.definitions.Face;
import gen.util.SvgBuilder;
import gen.util.Tuple;

public class CardBuilder {

	protected FileGeneratorData fileGeneratorData;
	protected Card card;

	public CardBuilder(Card card, FileGeneratorData fileGeneratorData) {
		this.card = card;
		this.fileGeneratorData = fileGeneratorData;
	}

	public List<FileGeneratorData> build() throws Exception {
		List<FileGeneratorData> returnList = new ArrayList<>();
		Boolean hasTwoSides = card.getFaces().size() == 2;
		Boolean firstSide = true;
		for (Tuple<Face, Node> tuple : generateNodes()) {
			Node node = tuple.y();
			Face face = tuple.x();
			CardBuilderInternal bleedBuilder = new CardBuilderInternal();
			bleedBuilder.generateDocument();
			Node importedNode = bleedBuilder.getDocument().importNode(node, true);
			bleedBuilder.translateNode(importedNode, 3, 3);
			bleedBuilder.getBodyNode().appendChild(importedNode);

			bleedBuilder.generateDocumentInternal();

			FileGeneratorData newData = new FileGeneratorData(fileGeneratorData);
			newData.setDocument(bleedBuilder.getDocument());
			newData.setFace(face);
			newData.setFaceSideString("");
			if (hasTwoSides) {
				newData.setFaceSideString(firstSide ? "A" : "B");
			}
			returnList.add(newData);
			firstSide = false;
		}
		return returnList;
	}

	protected List<Tuple<Face, Node>> generateNodes() throws Exception {
		List<Tuple<Face, Node>> list = new ArrayList<>();
		for (Face face : card.getFaces()) {
			FaceBuilder faceBuilder = new FaceBuilder(face);
			faceBuilder.generateDocument();
			list.add(new Tuple<>(face, faceBuilder.getBodyNode()));
		}
		return list;
	}

	private class CardBuilderInternal extends SvgBuilder {

		@Override
		protected String getMainXmlFilename() {
			return "MainSingle.xml";
		}

		@Override
		protected void generateDocumentInternal() throws Exception {
			Node borderSafeNode = loadFragment("BleedSingle.xml");
			bodyNode.appendChild(borderSafeNode);
		}

		@Override
		protected String getRessourceFolder() {
			return "/gen/set/card/fragments/";
		}

	}
}
