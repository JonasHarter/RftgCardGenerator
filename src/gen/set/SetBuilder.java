package gen.set;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import gen.builders.node.DoubleCardBleedBuilder;
import gen.builders.node.DoubleFaceCardBorderBuilder;
import gen.builders.node.DoubleFaceCardBuilder;
import gen.builders.node.InnerFaceBorderBuilder;
import gen.builders.node.InnerFaceBuilder;
import gen.builders.node.SingleCardBleedBuilder;
import gen.png.PngGenerator;
import gen.set.definitions.Card;
import gen.set.definitions.CardType;
import gen.set.definitions.Face;
import gen.set.definitions.Set;
import gen.util.SvgBuilder;
import gen.util.Tuple;

public class SetBuilder {

	private Set set;
	private Path outputFolder;
	private Path imageFolder;

	public SetBuilder(Set set, Path outputFolder, Path imageFolder) {
		this.set = set;
		this.outputFolder = outputFolder;
		this.imageFolder = imageFolder.resolve(set.getName());
	}

	public void build() throws Exception {
		Integer cardId = 1;
		List<Tuple<Document, CardData>> cardList = new ArrayList<Tuple<Document, CardData>>();
		for (Card card : set.getCards()) {
			List<Tuple<Document, CardData>> innerFacesList = new ArrayList<>();
			String cardSide = "A";
			for (Face face : card.getFaces()) {
				Boolean isStartingPlanet = card.getFaces().size() == 1;
				CardData cardData = new CardData(set.getLetter(), cardId.toString(), cardSide);
				cardData.isSingleSided = isStartingPlanet;
				InnerFaceBuilder innerFaceBuilder = new InnerFaceBuilder(face, isStartingPlanet,
						cardData.generateIdString(), imageFolder);
				Document innerFace = innerFaceBuilder.build();
				innerFacesList.add(new Tuple<>(innerFace, cardData));
				cardSide = "B";
			}
			if (CardType.STARTER_DOUBLE.equals(card.getCardType())) {
				DoubleFaceCardBuilder doubleCardBuilder = new DoubleFaceCardBuilder(innerFacesList.get(0).x(),
						innerFacesList.get(1).x());
				Document doubleCard = doubleCardBuilder.build();
				DoubleFaceCardBorderBuilder doubleCardBorderBuilder = new DoubleFaceCardBorderBuilder(doubleCard);
				Document doubleCardBordered = doubleCardBorderBuilder.build();
				DoubleCardBleedBuilder doubleCardBleedBuilder = new DoubleCardBleedBuilder(doubleCardBordered);
				CardData cardData = innerFacesList.get(0).y();
				cardData.isDoubleSized = true;
				cardList.add(new Tuple<>(doubleCardBleedBuilder.build(), innerFacesList.get(0).y()));
			} else {
				for (Tuple<Document, CardData> tuple : innerFacesList) {
					InnerFaceBorderBuilder innerFaceBorderedBuilder = new InnerFaceBorderBuilder(tuple.x());
					Document innerFaceBordered = innerFaceBorderedBuilder.build();
					SingleCardBleedBuilder singleCardBleedBuilder = new SingleCardBleedBuilder(innerFaceBordered);
					cardList.add(new Tuple<>(singleCardBleedBuilder.build(), tuple.y()));
				}
			}
			cardId++;
		}
		try (PngGenerator pngGenerator = new PngGenerator()) {
			for (Tuple<Document, CardData> tuple : cardList) {
				Path filePath = outputFolder.resolve(tuple.y().generateIdString() + ".svg");
				SvgBuilder.writeToFolder(tuple.x(), filePath);
				pngGenerator.convertFileAsync(filePath.toFile(), tuple.y().isDoubleSized());
			}
		}
		return;
	}

	private class CardData {
		private String set;
		private String id;
		private String side;
		private Boolean isDoubleSized = false;
		private Boolean isSingleSided = false;

		private CardData(String set, String id, String side) {
			this.set = set;
			this.id = id;
			this.side = side;
		}

		public String generateIdString() {
			if (isDoubleSized || isSingleSided)
				return set + "-" + id;
			return set + "-" + id + "-" + side;
		}

		public Boolean isDoubleSized() {
			return isDoubleSized;
		}

	}

	public static class SetBuilderException extends Exception {
		private static final long serialVersionUID = -9078438810344178993L;

		private SetBuilderException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
