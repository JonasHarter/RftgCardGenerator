package gen.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import gen.set.SetBuilder;

public abstract class SvgBuilder {
	private DocumentBuilder documentBuilder;
	private Document document;
	protected Node bodyNode;

	public void generateDocument() throws Exception {
		InputStream inputStream = loadRessource(getMainXmlFilename());
		DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
		docbf.setNamespaceAware(true);
		documentBuilder = docbf.newDocumentBuilder();
		document = documentBuilder.parse(inputStream);
		Node parentNode = (Node) document.getDocumentElement();
		bodyNode = parentNode.getChildNodes().item(1);
		generateDocumentInternal();
	}

	protected abstract String getMainXmlFilename();

	protected abstract void generateDocumentInternal() throws Exception;

	public Document getDocument() {
		Objects.requireNonNull(document, "Document not generated");
		return document;
	}

	public Node getBodyNode() {
		Objects.requireNonNull(bodyNode, "Document not generated");
		return bodyNode;
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

	protected Node loadFragment(String fileName) throws Exception {
		InputStream inputStreamBorder = loadRessource(fileName);
		Node fragmentNode = documentBuilder.parse(new InputSource(inputStreamBorder)).getDocumentElement();
		fragmentNode = document.importNode(fragmentNode, true);
		return fragmentNode;
	}

	protected InputStream loadRessource(String fileName) {
		return SetBuilder.class.getResourceAsStream(getRessourceFolder() + fileName);
	}

	protected abstract String getRessourceFolder();

	public Node translateNode(Node fragment, int x, int y) {
		Attr transformAttr = document.createAttribute("transform");
		transformAttr.setTextContent("translate(" + x + " " + y + ")");
		Objects.requireNonNull(fragment.getAttributes(), "Node is not an Element");
		fragment.getAttributes().setNamedItem(transformAttr);
		return fragment;
	}

}
