package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class ContainsSubResourceVisitor implements ResourceViewAttributeVisitor {

	private boolean containsSubResource;
	private String viewName;

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		this.containsSubResource = singleResourceViewAttribute.getDisplayViewAttribute().getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE;
		this.viewName = singleResourceViewAttribute.getDisplayViewAttribute().getAttributeName();
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		this.containsSubResource = groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE;
		this.viewName = groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeName();
	}

	public boolean isContainsSubResource() {
		return containsSubResource;
	}

	public String getViewName() {
		return viewName;
	}
}
