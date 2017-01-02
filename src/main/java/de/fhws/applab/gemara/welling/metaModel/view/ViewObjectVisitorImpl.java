package de.fhws.applab.gemara.welling.metaModel.view;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getViewClassName;

public class ViewObjectVisitorImpl implements ViewObjectVisitor {

	private final ClassName profileImageViewClassName;
	private final ClassName rClassName;
	private final ClassName attributeViewClassName;
	private final ClassName viewClassName;

	private final String specificResourceName;

	public ViewObjectVisitorImpl(ClassName profileImageViewClassName, ClassName rClassName, ClassName attributeViewClassName,
			String specificResourceName) {
		this.profileImageViewClassName = profileImageViewClassName;
		this.rClassName = rClassName;
		this.attributeViewClassName = attributeViewClassName;
		this.specificResourceName = specificResourceName;

		this.viewClassName = getViewClassName();
	}

	@Override
	public FieldSpec visitForAddField(SingleViewObject singleViewObject) {
		ViewAttribute viewAttribute = singleViewObject.getViewAttribute();
		String viewName = singleViewObject.getViewName();
		if (viewAttribute.getType() == AttributeType.PICTURE) {
			return FieldSpec.builder(profileImageViewClassName, viewName, Modifier.PRIVATE).build();
		} else {
			return _visitForAddField(viewName);
		}
	}

	@Override
	public FieldSpec visitForAddField(GroupedViewObject groupedViewObject) {
		return _visitForAddField(groupedViewObject.getViewName());
	}

	private FieldSpec _visitForAddField(String viewName) {
		return FieldSpec.builder(attributeViewClassName, viewName, Modifier.PRIVATE).build();
	}

	@Override
	public void visitForFindViewById(MethodSpec.Builder builder, SingleViewObject singleViewObject) {
		_visitForFindViewById(builder, singleViewObject.getViewName(), singleViewObject.getViewAttribute());
	}

	@Override
	public void visitForFindViewById(MethodSpec.Builder builder, GroupedViewObject groupedViewObject) {
		for (ViewAttribute viewAttribute : groupedViewObject.getViewAttribute()) {
			_visitForFindViewById(builder, groupedViewObject.getViewName(), viewAttribute);
		}
	}

	private void _visitForFindViewById(MethodSpec.Builder builder, String viewName, ViewAttribute viewAttribute) {
		if (viewAttribute.getType() == AttributeType.PICTURE) {
			builder.addStatement("$N = ($T) findViewById($T.id.$N)", viewName, profileImageViewClassName, rClassName,
					viewAttribute.getResourceName());
		} else {
			builder.addStatement("$N = ($T) findViewById($T.id.$N)", viewName, attributeViewClassName, rClassName,
					viewAttribute.getResourceName());
		}
	}

	@Override
	public void visitForSetText(MethodSpec.Builder builder, SingleViewObject singleViewObject) {
		ViewAttribute viewAttribute = singleViewObject.getViewAttribute();
		if (viewAttribute.getType() == AttributeType.PICTURE) {
			builder.addStatement("$N.loadImage($N.$L, $T.dimen.picture_width, $T.dimen.picture_height)", singleViewObject.getViewName(),
					specificResourceName, viewAttribute.getGetter(), rClassName, rClassName);
		} else if (viewAttribute.getType() == AttributeType.URL) {
			builder.addStatement("$N.setText(getResources().getText($T.string.$N))", singleViewObject.getViewName(), rClassName,
					viewAttribute.getDisplayedName());
		} else {
			builder.addStatement("$N.setText($N.$L())", singleViewObject.getViewName(), specificResourceName, viewAttribute.getGetter());
		}

	}

	@Override
	public void visitForSetText(MethodSpec.Builder builder, GroupedViewObject groupedViewObject) {
		List<ViewAttribute> viewAttributes = groupedViewObject.getViewAttribute();
		String literal = "";
		for (int i = 0; i < viewAttributes.size(); i++) {
			if (i == 0) {
				literal += specificResourceName + "." + viewAttributes.get(i).getGetter() + "()";
			} else {
				literal += "+ \" \" + " + specificResourceName + "." + viewAttributes.get(i).getGetter() + "()";
			}
		}
		builder.addStatement("$N.setText($L)", groupedViewObject.getViewName(), literal);
	}

	@Override
	public void visitForHideViews(MethodSpec.Builder builder, SingleViewObject singleViewObject) {
		_visitForHideViews(builder, singleViewObject.getViewAttribute(), singleViewObject.getViewName());
	}

	@Override
	public void visitForHideViews(MethodSpec.Builder builder, GroupedViewObject groupedViewObject) {
		for (ViewAttribute viewAttribute : groupedViewObject.getViewAttribute()) {
			_visitForHideViews(builder, viewAttribute, groupedViewObject.getViewName());
		}
	}

	public void _visitForHideViews(MethodSpec.Builder builder, ViewAttribute viewAttribute, String viewName) {
		if (viewAttribute.getType() != AttributeType.PICTURE) {
			builder.beginControlFlow("if ($N.$L().trim().isEmpty())", specificResourceName, viewAttribute.getGetter());
			builder.addStatement("$N.setVisibility($T.GONE)", viewName, viewClassName);
			builder.endControlFlow();
			builder.beginControlFlow("else");
			builder.addStatement("$N.setVisibility($T.VISIBLE)", viewName, viewClassName);
			builder.endControlFlow();
		}
	}
}
