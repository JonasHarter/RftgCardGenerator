package gen.builders.node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DoubleFaceCardBorderBuilder extends NodeBuilder {

	private Document doubleCardDocument;

	public DoubleFaceCardBorderBuilder(Document doubleCardDocument) {
		super();
		this.doubleCardDocument = doubleCardDocument;
	}

	@Override
	protected void buildInternal() throws Exception {
		Node importedNode = document.importNode(doubleCardDocument.getDocumentElement(), true);
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
		element.setAttribute("width", "127");
		element.setAttribute("height", "3");
		return element;
	}
	
	private Element createBorderLeft() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "89");
		return element;
	}
	
	private Element createBorderRight() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "124");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "89");
		return element;
	}
	
	private Element createBorderBottom() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "86");
		element.setAttribute("width", "127");
		element.setAttribute("height", "3");
		return element;
	}
}