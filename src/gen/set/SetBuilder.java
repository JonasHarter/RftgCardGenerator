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
//		List<FileGeneratorData> finalData = new ArrayList<>();
//		FileGeneratorData fileGeneratorData = new FileGeneratorData(outputFolder, imageFolder);
		Integer cardId = 1;
		try (PngGenerator pngGenerator = new PngGenerator()) {
			for (Card card : set.getCards()) {
				List<Document> innerFacesList = new ArrayList<>();
				for (Face face : card.getFaces()) {
					InnerFaceBuilder innerFaceBuilder = new InnerFaceBuilder(face);
					Document innerFace = innerFaceBuilder.build();
					innerFacesList.add(innerFace);
				}
				List<Document> finalDocumentList = new ArrayList<>();
				Boolean doubleSized = false;
				if (CardType.STARTER_DOUBLE.equals(card.getCardType())) {
					doubleSized = true;
					DoubleFaceCardBuilder doubleCardBuilder = new DoubleFaceCardBuilder(innerFacesList.get(0),
							innerFacesList.get(1));
					Document doubleCard = doubleCardBuilder.build();
					DoubleFaceCardBorderBuilder doubleCardBorderBuilder = new DoubleFaceCardBorderBuilder(doubleCard);
					Document doubleCardBordered = doubleCardBorderBuilder.build();
					DoubleCardBleedBuilder doubleCardBleedBuilder = new DoubleCardBleedBuilder(doubleCardBordered);
					finalDocumentList.add(doubleCardBleedBuilder.build());
				} else {
					for (Document innerFaceDocument : innerFacesList) {
						InnerFaceBorderBuilder innerFaceBorderedBuilder = new InnerFaceBorderBuilder(innerFaceDocument);
						Document innerFaceBordered = innerFaceBorderedBuilder.build();
						SingleCardBleedBuilder singleCardBleedBuilder = new SingleCardBleedBuilder(innerFaceBordered);
						finalDocumentList.add(singleCardBleedBuilder.build());
					}
				}
				for (Document finalDocument : finalDocumentList) {
					Path filePath = outputFolder.resolve(cardId + ".svg");
					SvgBuilder.writeToFolder(finalDocument, filePath);
					pngGenerator.convertFileAsync(filePath.toFile(), doubleSized);
					cardId++;
				}
			}
		}
		return;
	}

	public static class SetBuilderException extends Exception {
		private static final long serialVersionUID = -9078438810344178993L;

		private SetBuilderException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
