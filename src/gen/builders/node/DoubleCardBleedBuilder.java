package gen.builders.node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DoubleCardBleedBuilder extends NodeBuilder {

	private Node parameterNode;

	public DoubleCardBleedBuilder(Document parameterDocument) {
		super();
		this.parameterNode = parameterDocument.getDocumentElement().getFirstChild();
	}

	@Override
	protected void buildInternal() throws Exception {
		Element rootElement = document.getDocumentElement();
		rootElement.setAttribute("width", "133mm");
		rootElement.setAttribute("height", "95mm");
		rootElement.setAttribute("viewBox", "0 0 133 95");
		Node importedNode = document.importNode(parameterNode, true);
		translateNode(importedNode, 3, 3);
		rootNode.appendChild(importedNode);
		// 89 x 127
		rootNode.appendChild(createBorderTop());
		rootNode.appendChild(createBorderLeft());
		rootNode.appendChild(createBorderRight());
		rootNode.appendChild(createBorderBottom());
	}

	private Element createBorderTop() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "95");
		element.setAttribute("height", "3");
		return element;
	}

	private Element createBorderLeft() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "94");
		return element;
	}

	private Element createBorderRight() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "92");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "133");
		return element;
	}

	private Element createBorderBottom() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "91");
		element.setAttribute("width", "133");
		element.setAttribute("height", "3");
		return element;
	}
}