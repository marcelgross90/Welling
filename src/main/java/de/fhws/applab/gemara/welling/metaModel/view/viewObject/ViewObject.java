package de.fhws.applab.gemara.welling.metaModel.view.viewObject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.cardViews.visitor.TitleVisitor;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.visitors.ViewObjectVisitor;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.visitors.ViewObjectXMLVisitor;

import java.util.List;

public abstract class ViewObject<T> {

	protected final T viewAttribute;
	protected final String viewName;

	public ViewObject(T viewAttribute, String viewName) {
		this.viewAttribute = viewAttribute;
		this.viewName = viewName;
	}

	public String getViewName() {
		return this.viewName;
	}

	public abstract T getViewAttribute();
	public abstract FieldSpec addField(ViewObjectVisitor visitor);
	public abstract void addInitializeViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract void addInitializeDetailViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract void addFillResourceStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract void addHideUnnecessaryViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract List<AbstractLayoutGenerator.View> addCardViewSubView(String packageName, ViewObjectXMLVisitor visitor);
	public abstract AbstractLayoutGenerator.View addDetailCardViewSubView(String packageName, ViewObjectXMLVisitor visitor);
	public abstract String getTitleForMethodSpec(TitleVisitor visitor, AppResource appResource);
}
