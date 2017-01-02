package de.fhws.applab.gemara.welling.metaModel.view;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public interface ViewObjectVisitor {

	FieldSpec visitForAddField(SingleViewObject singleViewObject);
	FieldSpec visitForAddField(GroupedViewObject groupedViewObject);
	void visitForFindViewById(MethodSpec.Builder builder, SingleViewObject singleViewObject);
	void visitForFindViewById(MethodSpec.Builder builder, GroupedViewObject groupedViewObject);
	void visitForSetText(MethodSpec.Builder builder, SingleViewObject singleViewObject);
	void visitForSetText(MethodSpec.Builder builder, GroupedViewObject groupedViewObject);
	void visitForHideViews(MethodSpec.Builder builder, SingleViewObject singleViewObject);
	void visitForHideViews(MethodSpec.Builder builder, GroupedViewObject groupedViewObject);
}
