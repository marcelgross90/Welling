package de.fhws.applab.gemara.welling.metaModel.view;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.List;

public class GroupedViewObject extends ViewObject<List<ViewAttribute>> {

	public GroupedViewObject(List<ViewAttribute> viewObject, String viewName) {
		super(viewObject, viewName);
	}

	@Override
	public List<ViewAttribute> getViewAttribute() {
		return this.viewObject;
	}

	@Override
	public FieldSpec addField(ViewObjectVisitor visitor) {
		return visitor.visitForAddField(this);
	}

	@Override
	public void addInitializeViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor) {
		visitor.visitForFindViewById(builder, this);
	}

	@Override
	public void addFillResourceStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor) {
		visitor.visitForSetText(builder, this);
	}

	@Override
	public void addHideUnnecessaryViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor) {
		visitor.visitForHideViews(builder, this);
	}

	@Override
	public List<AbstractLayoutGenerator.View> addCardViewSubView(String packageName, ViewObjectXMLVisitor visitor) {
		return visitor.visitForCardSubView(this, packageName);
	}
}
