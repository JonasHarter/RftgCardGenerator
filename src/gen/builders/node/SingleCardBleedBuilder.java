package gen.builders.node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SingleCardBleedBuilder extends NodeBuilder {

	private Node parameterNode;

	public SingleCardBleedBuilder(Document parameterDocument) {
		super();
		this.parameterNode = parameterDocument.getDocumentElement().getFirstChild();
	}

	@Override
	protected void buildInternal() throws Exception {
		Element rootElement = document.getDocumentElement();
		rootElement.setAttribute("width", "69mm");
		rootElement.setAttribute("height", "94mm");
		rootElement.setAttribute("viewBox", "0 0 69 94");
		Node importedNode = document.importNode(parameterNode, true);
		translateNode(importedNode, 3, 3);
		rootNode.appendChild(importedNode);
		// 63x88
		rootNode.appendChild(createBorderTop());
		rootNode.appendChild(createBorderLeft());
		rootNode.appendChild(createBorderRight());
		rootNode.appendChild(createBorderBottom());
	}

	private Element createBorderTop() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "69");
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
		element.setAttribute("x", "66");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "94");
		return element;
	}

	private Element createBorderBottom() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "91");
		element.setAttribute("width", "69");
		element.setAttribute("height", "3");
		return element;
	}
}