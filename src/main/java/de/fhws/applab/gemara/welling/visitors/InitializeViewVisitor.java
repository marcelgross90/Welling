package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute.AttributeType;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class InitializeViewVisitor implements ResourceViewAttributeVisitor {

	private final MethodSpec.Builder builder;
	private final ClassName attributeViewClassName;
	private final ClassName profileImageViewClassName;
	private final ClassName rClassName;

	public InitializeViewVisitor(MethodSpec.Builder builder, ClassName attributeViewClassName, ClassName profileImageViewClassName,
			ClassName rClassName) {
		this.builder = builder;
		this.attributeViewClassName = attributeViewClassName;
		this.profileImageViewClassName = profileImageViewClassName;
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

	private void addStatement(String viewName, AttributeType attributeType) {
		if (attributeType != AttributeType.SUBRESOURCE) {
			if (attributeType == AttributeType.PICTURE) {
				builder.addStatement("$N = ($T) findViewById($T.id.$N)", viewName, profileImageViewClassName, rClassName, "profileImg");
			} else {
				builder.addStatement("$N = ($T) findViewById($T.id.$N)", viewName, attributeViewClassName, rClassName, viewName);
			}
		}
	}
}
