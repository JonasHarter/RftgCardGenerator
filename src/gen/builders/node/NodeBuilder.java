package gen.builders.node;

import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class NodeBuilder {
	private DocumentBuilder documentBuilder;
	protected Document document;
	protected Node rootNode;

	protected NodeBuilder() {
	}

	public Document build() throws NodeBuilderException {
		try {
			DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
			docbf.setNamespaceAware(true);
			documentBuilder = docbf.newDocumentBuilder();
			document = documentBuilder.newDocument();
			Element svgElement = document.createElement("svg:svg");
			svgElement.setAttribute("version", "1.2");
			svgElement.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			svgElement.setAttribute("xmlns:svg", "http://www.w3.org/2000/svg");
			document.appendChild(svgElement);
			Element gElement = document.createElement("g");
			gElement.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			svgElement.appendChild(gElement);
			rootNode = gElement;
			buildInternal();
			return document;
		} catch (Exception e) {
			throw new NodeBuilderException("Failed to build node", e);
		}
	}

	protected abstract void buildInternal() throws Exception;

	public Node translateNode(Node node, int x, int y) {
		Attr transformAttr = document.createAttribute("transform");
		transformAttr.setTextContent("translate(" + x + " " + y + ")");
		Objects.requireNonNull(node.getAttributes(), "Node is not an Element");
		node.getAttributes().setNamedItem(transformAttr);
		return node;
	}

	public static class NodeBuilderException extends Exception {
		private static final long serialVersionUID = 6468323278569510045L;

		public NodeBuilderException(String message, Throwable cause) {
			super(message, cause);
		}

		public NodeBuilderException(String message) {
			super(message);
		}

	}
}
