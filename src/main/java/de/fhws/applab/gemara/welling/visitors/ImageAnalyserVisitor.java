package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class ImageAnalyserVisitor implements ResourceViewAttributeVisitor {

	private boolean containsImage;
	private DisplayViewAttribute.PicturePosition picturePosition;

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		this.containsImage =
				singleResourceViewAttribute.getDisplayViewAttribute().getAttributeType() == ViewAttribute.AttributeType.PICTURE;
		this.picturePosition = singleResourceViewAttribute.getDisplayViewAttribute().getPicturePosition();
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		this.containsImage =
				groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeType() == ViewAttribute.AttributeType.PICTURE;
		this.picturePosition = groupResourceViewAttribute.getGroupResouceViewAttribute().getPicturePosition();
	}

	public boolean isContainsImage() {
		return containsImage;
	}

	public DisplayViewAttribute.PicturePosition getPicturePosition() {
		return picturePosition;
	}
}