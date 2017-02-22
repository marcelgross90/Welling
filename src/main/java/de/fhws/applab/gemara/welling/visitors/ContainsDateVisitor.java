package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class ContainsDateVisitor implements ResourceViewAttributeVisitor {

	private boolean containsDate;

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		this.containsDate = singleResourceViewAttribute.getDisplayViewAttribute().getAttributeType() == ViewAttribute.AttributeType.DATE;
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		this.containsDate =
				groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeType() == ViewAttribute.AttributeType.DATE;
		for (DisplayViewAttribute displayViewAttribute : groupResourceViewAttribute.getDisplayViewAttributes()) {
			if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				containsDate = true;
				break;
			}
		}
	}

	public boolean isContainsDate() {
		return containsDate;
	}
}