package gen.builders.node;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DoubleFaceCardBuilder extends NodeBuilder {

	private Node leftInnerFaceNode;
	private Node rightInnerFaceNode;

	public DoubleFaceCardBuilder(Document leftInnerFaceDocument, Document rightInnerFaceDocument) {
		this.leftInnerFaceNode = leftInnerFaceDocument.getDocumentElement().getFirstChild();
		this.rightInnerFaceNode = rightInnerFaceDocument.getDocumentElement().getFirstChild();
	}

	@Override
	protected void buildInternal() throws Exception {
		Node leftImportedNode = document.importNode(leftInnerFaceNode, true);
		rootNode.appendChild(leftImportedNode);
		Node rightImportedNode = document.importNode(rightInnerFaceNode, true);
		translateNode(rightImportedNode, 63, 0);
		rootNode.appendChild(rightImportedNode);
	}
}
