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
		createCenterBorder();
		createBorderTop();
		createBorderLeft();
		createBorderRight();
		createBorderBottom();
	}
	
	private void createCenterBorder() {
		Element top = document.createElement("rect");
		top.setAttribute("x", "60");
		top.setAttribute("y", "0");
		top.setAttribute("width", "6");
		top.setAttribute("height", "22");
		
		Element triangle = document.createElement("path");
		triangle.setAttribute("style", "fill:#000000;stroke:#ffffff;stroke-width:0;stroke-opacity:0");
		triangle.setAttribute("d", "M 60,22 63,25 66,22 Z");

		Element center = document.createElement("rect");
		center.setAttribute("x", "62");
		center.setAttribute("y", "22");
		center.setAttribute("width", "2");
		center.setAttribute("height", "44");
		
		Element pyramid = document.createElement("path");
		pyramid.setAttribute("style", "fill:#000000;stroke:#ffffff;stroke-width:0;stroke-opacity:0");
		pyramid.setAttribute("d", "M 60,66 63,63 66,66 Z");
		
		Element bot = document.createElement("rect");
		bot.setAttribute("x", "60");
		bot.setAttribute("y", "66");
		bot.setAttribute("width", "6");
		bot.setAttribute("height", "22");
		
		rootNode.appendChild(top);
		rootNode.appendChild(triangle);
		rootNode.appendChild(center);
		rootNode.appendChild(pyramid);
		rootNode.appendChild(bot);
	}

	private void createBorderTop() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "127");
		element.setAttribute("height", "3");
		rootNode.appendChild(element);
	}
	
	private void createBorderLeft() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "89");
		rootNode.appendChild(element);
	}
	
	private void createBorderRight() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "124");
		element.setAttribute("y", "0");
		element.setAttribute("width", "3");
		element.setAttribute("height", "89");
		rootNode.appendChild(element);
	}
	
	private void createBorderBottom() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "85");
		element.setAttribute("width", "127");
		element.setAttribute("height", "4");
		rootNode.appendChild(element);
	}
}