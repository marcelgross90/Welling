package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class InitializeDetailViewVisitor implements ResourceViewAttributeVisitor {

	private MethodSpec.Builder builder;
	private final ClassName attributeViewClassName;
	private final ClassName rClassName;

	public InitializeDetailViewVisitor(MethodSpec.Builder builder, ClassName attributeViewClassName, ClassName rClassName) {
		this.builder = builder;
		this.attributeViewClassName = attributeViewClassName;
		this.rClassName = rClassName;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		addStatement(displayViewAttribute.getAttributeName(), displayViewAttribute.getAttributeType());
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
		addStatement(displayViewAttribute.getAttributeName(), displayViewAttribute.getAttributeType());
	}

	private void addStatement(String viewName, ViewAttribute.AttributeType attributeType) {
		if (attributeType != ViewAttribute.AttributeType.PICTURE && attributeType != ViewAttribute.AttributeType.SUBRESOURCE) {
			builder.addStatement("$N = ($T) findViewById($T.id.$N)", viewName, attributeViewClassName, rClassName, "tv" + getNameWithCapitalStart(viewName) + "Value");
		}
	}

	private String getNameWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
}