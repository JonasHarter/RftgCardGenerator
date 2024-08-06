package gen.builders.node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InnerFaceBorderBuilder extends NodeBuilder {

	private Document innerFaceDocument;

	public InnerFaceBorderBuilder(Document innerFaceDocument) {
		super();
		this.innerFaceDocument = innerFaceDocument;
	}

	@Override
	protected void buildInternal() throws Exception {
		Node importedNode = document.importNode(innerFaceDocument.getDocumentElement(), true);
		rootNode.appendChild(importedNode);
		rootNode.appendChild(createBorderTop());
		rootNode.appendChild(createBorderLeft());
		rootNode.appendChild(createBorderRight());
		rootNode.appendChild(createBorderBottom());
	}

	private Element createBorderTop() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "63");
		element.setAttribute("height", "3");
		return element;
	}

	private Element createBorderLeft() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "85");
		return element;
	}

	private Element createBorderRight() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "60");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "88");
		return element;
	}

	private Element createBorderBottom() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "85");
		element.setAttribute("width", "63");
		element.setAttribute("height", "3");
		return element;
	}
}