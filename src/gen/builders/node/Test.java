package gen.builders.node;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import gen.builders.node.NodeBuilder.NodeBuilderException;

public class Test {

	public static void main(String[] args) throws Exception {
		InnerFaceBuilder innerFaceBuilder = new InnerFaceBuilder(null);
		Document innerFace = innerFaceBuilder.build();
		InnerFaceBorderBuilder innerFaceBorderedBuilder = new InnerFaceBorderBuilder(innerFace);
		Document innerFaceBordered = innerFaceBorderedBuilder.build();
		SingleCardBleedBuilder singleCardBleedBuilder = new SingleCardBleedBuilder(innerFaceBordered);
		Document innerFaceBleeded = singleCardBleedBuilder.build();
		DoubleFaceCardBuilder doubleCardBuilder = new DoubleFaceCardBuilder(innerFace, innerFace);
		Document doubleCard = doubleCardBuilder.build();
		DoubleFaceCardBorderBuilder doubleCardBorderBuilder = new DoubleFaceCardBorderBuilder(doubleCard);
		Document doubleCardBordered = doubleCardBorderBuilder.build();
		writeToFolder(innerFaceBleeded, Paths.get("D:\\Projects\\Roll for the Galaxy 2\\test.svg"));
	}
	
	public static void writeToFolder(Document document, Path outputFilePath) throws Exception {
		Objects.requireNonNull(document, "Document not generated");
		DOMSource source = new DOMSource(document);
		FileOutputStream out = new FileOutputStream(outputFilePath.toFile());
		StreamResult result = new StreamResult(out);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(source, result);
	}
}
