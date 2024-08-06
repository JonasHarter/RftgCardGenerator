package gen.builders.node;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import gen.set.definitions.Face;

public class InnerFaceBuilder extends NodeBuilder {

	private Face face;
	
	public InnerFaceBuilder(Face face) {
		super();
		this.face = face;
	}

	@Override
	protected void buildInternal() throws Exception {
		// TODO textbox x image border
		rootNode.appendChild(createUpperBg());
		rootNode.appendChild(createLowerBg());
		rootNode.appendChild(createHeaderText());
	}

	private Element createUpperBg() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "0");
		element.setAttribute("width", "63");
		element.setAttribute("height", "22");
		element.setAttribute("style", "fill:#bfbfbf;");
		return element;
	}

	private Element createLowerBg() {
		Element element = document.createElement("rect");
		element.setAttribute("x", "0");
		element.setAttribute("y", "66");
		element.setAttribute("width", "63");
		element.setAttribute("height", "22");
		element.setAttribute("style", "fill:#bfbfbf;");
		return element;
	}

	private Element createHeaderText() {
		Element flowRoot = document.createElement("flowRoot");
		flowRoot.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		flowRoot.setAttribute("style", "font-size:3pt;text-align:center");
		Element flowRegion = document.createElement("flowRegion");
		flowRoot.appendChild(flowRegion);
		Element path = document.createElement("path");
		path.setAttribute("d", "M 4,4 59,4 59,22 4,22 Z");
		flowRegion.appendChild(path);
		Element flowPara = document.createElement("flowPara");
		flowPara.setAttribute("x", "50%");
		flowPara.setAttribute("y", "50%");
		flowPara.setTextContent(face.getName());
		flowRoot.appendChild(flowPara);
		return flowRoot;
	}
}