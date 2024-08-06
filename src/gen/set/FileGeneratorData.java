package gen.set;

import java.nio.file.Path;
import java.util.Objects;

import org.w3c.dom.Document;

import gen.set.definitions.Card;
import gen.set.definitions.Face;
import gen.set.definitions.Set;
import gen.util.SvgBuilder;

public class FileGeneratorData {

	private Path outputFolderPath;
	private Path imageFolderPath;
	private Set set;
	private Integer cardId;
	private String faceSideString;
	private Card card;
	private Face face;
	private Document document;

	public FileGeneratorData(Path outputFolderPath, Path imageFolderPath) {
		this.outputFolderPath = outputFolderPath;
		this.imageFolderPath = imageFolderPath;
	}

	public FileGeneratorData(FileGeneratorData clone) {
		this(clone.outputFolderPath, clone.imageFolderPath);
		this.set = clone.set;
		this.cardId = clone.cardId;
		this.faceSideString = clone.faceSideString;
		this.card = clone.card;
		this.face = clone.face;
		this.document = clone.document;
	}

	public void writeToFolder() throws Exception {
		SvgBuilder.writeToFolder(document, outputFolderPath.resolve(generateFilename()));
	}

	private String generateFilename() {
		Objects.requireNonNull(set);
		Objects.requireNonNull(cardId);
		Objects.requireNonNull(document);
		StringBuilder builder = new StringBuilder();
		builder.append(set.getName());
		builder.append(" - ");
		builder.append(cardId);
		builder.append(faceSideString);
		builder.append(" - ");
		builder.append(getFaceName());
		builder.append(".svg");
		return builder.toString();
	}

	private String getFaceName() {
		if (!Objects.isNull(face))
			return face.getName();
		Objects.requireNonNull(card);
		StringBuilder builder = new StringBuilder();
		for (Face face : card.getFaces()) {
			builder.append(face.getName());
			builder.append(" - ");
		}
		builder.setLength(builder.length() - 3);
		return builder.toString();
	}

	public Path getImageFolderPath() {
		return imageFolderPath;
	}

	public void setSet(Set set) {
		Objects.requireNonNull(set);
		this.set = set;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public void setFaceSideString(String faceSideString) {
		this.faceSideString = faceSideString;
	}

	public void setCard(Card card) {
		Objects.requireNonNull(card);
		this.card = card;
	}

	public void setFace(Face face) {
		Objects.requireNonNull(face);
		this.face = face;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
