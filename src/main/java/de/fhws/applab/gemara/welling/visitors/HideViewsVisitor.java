package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.application.util.GetterSetterGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute.AttributeType;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;

public class HideViewsVisitor implements ResourceViewAttributeVisitor {

	private MethodSpec.Builder builder;
	private final String specificResourceName;
	private final ClassName viewClassName;

	public HideViewsVisitor(MethodSpec.Builder builder, String specificResourceName) {
		this.builder = builder;
		this.specificResourceName = specificResourceName;
		this.viewClassName = getViewClassName();
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		addStatement(builder, displayViewAttribute.getAttributeType(), displayViewAttribute.getAttributeName());
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		for (DisplayViewAttribute displayViewAttribute : groupResourceViewAttribute.getDisplayViewAttributes()) {
			addStatement(builder, displayViewAttribute.getAttributeType(), displayViewAttribute.getAttributeName());
		}
	}

	public void addStatement(MethodSpec.Builder builder, AttributeType attributeType, String viewName) {
		if (attributeType != AttributeType.PICTURE) {
			if (attributeType == AttributeType.URL) {
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
}
