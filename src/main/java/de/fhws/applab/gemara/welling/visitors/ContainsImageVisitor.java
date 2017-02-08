package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class ContainsImageVisitor implements ResourceViewAttributeVisitor {

	private boolean containsImage;

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		this.containsImage = singleResourceViewAttribute.getDisplayViewAttribute().getAttributeType() == ViewAttribute.AttributeType.PICTURE;
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		this.containsImage = groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeType() == ViewAttribute.AttributeType.PICTURE;
	}

	public boolean isContainsImage() {
		return containsImage;
	}
}
