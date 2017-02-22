package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class InitializeClickableAttributesDetailViewVisitor extends InitializeClackableVisitor {

	public InitializeClickableAttributesDetailViewVisitor(MethodSpec.Builder method, String viewName, ClassName attributeViewClassName,
			ClassName rClassName) {
		super(method, viewName, attributeViewClassName, rClassName);
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();

		if (displayViewAttribute.isClickActionAndroid()) {
			method.addStatement("$T $N = ($T) $N.findViewById($T.id.$N)", attributeViewClassName, displayViewAttribute.getAttributeName(),
					attributeViewClassName, viewName, rClassName,
					"tv" + getInputWithCapitalStart(displayViewAttribute.getAttributeName()) + "Value");
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {

	}

	private String getInputWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
}