package gen.print;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class PrintGenerator {

	private Path imageFolder;
	private DocumentBuilder documentBuilder;
	private Document document;

	public PrintGenerator(Path imageFolder) throws PrintGeneratorException {
		this.imageFolder = imageFolder;
		// Load html
		InputStream inputStream = loadRessource("print.html");
		DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
		docbf.setNamespaceAware(true);
		try {
			documentBuilder = docbf.newDocumentBuilder();
			document = documentBuilder.parse(inputStream);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new PrintGeneratorException("Failed to load html", e);
		}
	}

	public void buildPrintFile() throws PrintGeneratorException {
		// Get files
		List<File> imageFiles = new ArrayList<>();
		for (File file : imageFolder.toFile().listFiles()) {
			if (!FilenameUtils.getExtension(file.getName()).equals("png"))
				continue;
			imageFiles.add(file);
		}
		// Generate file
		Node rootNode = (Node) document.getDocumentElement();
		Node bodyNode = rootNode.getChildNodes().item(3);
		for (File file : imageFiles) {
			Element imgElement = document.createElement("img");
			imgElement.setAttribute("src", "./" + file.getName().toString());
			bodyNode.appendChild(imgElement);
			bodyNode.appendChild(document.createTextNode(System.getProperty("line.separator")));
			Element divElement = document.createElement("div");
			divElement.setAttribute("class", "pagebreak");
			bodyNode.appendChild(divElement);
		}
		// Write
		try {
			writeToFile();
		} catch (Exception e) {
			throw new PrintGeneratorException("Failed to write final html", e);
		}
	}

	private InputStream loadRessource(String fileName) {
		return this.getClass().getResourceAsStream("/gen/print/data/" + fileName);
	}

	private void writeToFile() throws Exception {
		DOMSource source = new DOMSource(document);
		FileOutputStream out = new FileOutputStream(imageFolder.resolve("print.html").toFile());
		StreamResult result = new StreamResult(out);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(source, result);
	}

	public static class PrintGeneratorException extends Exception {
		private static final long serialVersionUID = -4833661383046636782L;

		private PrintGeneratorException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
