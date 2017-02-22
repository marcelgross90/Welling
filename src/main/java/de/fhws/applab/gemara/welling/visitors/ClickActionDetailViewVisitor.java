package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class ClickActionDetailViewVisitor extends ClickActionVisitor {

	@SuppressWarnings("SameParameterValue")
	public ClickActionDetailViewVisitor(MethodSpec.Builder method, String index, ClassName rClassName, String resourceName,
			boolean contextNeeded) {
		super(method, index, rClassName, resourceName, contextNeeded);
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();

		if (displayViewAttribute.isClickActionAndroid()) {
			method.beginControlFlow("if ($N == $T.id.$N)", index, rClassName,
					"tv" + getInputWithCapitalStart(displayViewAttribute.getAttributeName()) + "Value");
			getRightAction(displayViewAttribute);
			method.endControlFlow();
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {

	}

	private String getInputWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
}