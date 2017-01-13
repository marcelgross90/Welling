package de.fhws.applab.gemara.welling.metaModel.view;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import java.util.List;

public class SingleViewObject extends ViewObject<ViewAttribute> {

	public SingleViewObject(ViewAttribute viewObject, String viewName) {
		super(viewObject, viewName);
	}

	@Override
	public ViewAttribute getViewAttribute() {
		return this.viewAttribute;
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
	public void addInitializeDetailViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor) {
		visitor.visitForDetailFindViewById(builder, this);
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

	@Override
	public AbstractLayoutGenerator.View addDetailCardViewSubView(String packageName, ViewObjectXMLVisitor visitor) {
		return visitor.visitForDetailCardSubView(this, packageName);
	}

	@Override
	public String getTitleForMethodSpec(TitleVisitor visitor, AppResource appResource) {
		return visitor.visitForTitle(this, appResource);
	}
}
