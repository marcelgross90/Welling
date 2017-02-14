package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.generator.GetterSetterGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute.AttributeType;

import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;

public class HideViewsVisitor implements ResourceViewAttributeVisitor {

	private MethodSpec.Builder builder;
	private final String specificResourceName;
	private final ClassName viewClassName;

	public HideViewsVisitor(MethodSpec.Builder builder, String specificResourceName) {
		this.builder = builder;
		this.specificResourceName = specificResourceName.toLowerCase();
		this.viewClassName = getViewClassName();
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		addStatement(builder, displayViewAttribute.getAttributeType(), displayViewAttribute.getAttributeName());
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		if (!containsSubResourceAttribute(groupResourceViewAttribute)) {
			String ifCondition = "";
			List<DisplayViewAttribute> displayViewAttributes = groupResourceViewAttribute.getDisplayViewAttributes();
			for (int i = 0; i < displayViewAttributes.size(); i++) {
				DisplayViewAttribute attribute = displayViewAttributes.get(i);
				ifCondition += generateIfCondition(attribute.getAttributeType(), attribute.getAttributeName());
				if (i < displayViewAttributes.size() -1) {
					ifCondition += " || ";
				}
			}
			DisplayViewAttribute attribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
			builder.beginControlFlow("if (" + ifCondition + ")");
			builder.addStatement("$N.setVisibility($T.GONE)", attribute.getAttributeName(), viewClassName);
			builder.endControlFlow();
			builder.beginControlFlow("else");
			builder.addStatement("$N.setVisibility($T.VISIBLE)", attribute.getAttributeName(), viewClassName);
			builder.endControlFlow();
		}
	}

	private boolean containsSubResourceAttribute(GroupResourceViewAttribute groupResourceViewAttribute) {
		if (groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeType() == AttributeType.SUBRESOURCE) {
			return true;
		}
		for (DisplayViewAttribute displayViewAttribute : groupResourceViewAttribute.getDisplayViewAttributes()) {
			if (displayViewAttribute.getAttributeType() == AttributeType.SUBRESOURCE) {
				return true;
			}
		}

		return false;
	}

	private void addStatement(MethodSpec.Builder builder, AttributeType attributeType, String viewName) {
		if (attributeType != AttributeType.PICTURE && attributeType != AttributeType.SUBRESOURCE) {
			if (attributeType == AttributeType.URL || attributeType == AttributeType.DATE) {
				builder.beginControlFlow("if ($N.$L() == null)", specificResourceName, GetterSetterGenerator.getGetter(viewName));
			} else {
				builder.beginControlFlow("if ($N.$L().trim().isEmpty())", specificResourceName, GetterSetterGenerator.getGetter(viewName));
			}
			builder.addStatement("$N.setVisibility($T.GONE)", viewName, viewClassName);
			builder.endControlFlow();
			builder.beginControlFlow("else");
			builder.addStatement("$N.setVisibility($T.VISIBLE)", viewName, viewClassName);
			builder.endControlFlow();
		}
	}

	private String generateIfCondition(AttributeType attributeType, String viewName) {
		if (attributeType != AttributeType.PICTURE) {
			if (attributeType == AttributeType.URL || attributeType == AttributeType.DATE) {
				return specificResourceName + "." + GetterSetterGenerator.getGetter(viewName) + "() == null";
			} else {
				return specificResourceName + "." + GetterSetterGenerator.getGetter(viewName) + "().trim().isEmpty()";
			}
		}
		return "";
	}
}
