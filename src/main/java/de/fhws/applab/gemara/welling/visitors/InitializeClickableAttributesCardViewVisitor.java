package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class InitializeClickableAttributesCardViewVisitor extends InitializeClackableVisitor {

	public InitializeClickableAttributesCardViewVisitor(MethodSpec.Builder method, String viewName, ClassName attributeViewClassName,
			ClassName rClassName) {
		super(method, viewName, attributeViewClassName, rClassName);
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();

		if (displayViewAttribute.isClickActionAndroid()) {
			method.addStatement("$N = ($T) $N.findViewById($T.id.$N)", displayViewAttribute.getAttributeName(), attributeViewClassName,
					viewName, rClassName, displayViewAttribute.getAttributeName());
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {

	}
}