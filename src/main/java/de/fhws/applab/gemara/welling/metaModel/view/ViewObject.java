package de.fhws.applab.gemara.welling.metaModel.view;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.List;

public abstract class ViewObject<T> {

	protected final T viewObject;
	protected final String viewName;

	public ViewObject(T viewObject, String viewName) {
		this.viewObject = viewObject;
		this.viewName = viewName;
	}

	public String getViewName() {
		return this.viewName;
	}

	public abstract T getViewAttribute();
	public abstract FieldSpec addField(ViewObjectVisitor visitor);
	public abstract void addInitializeViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract void addFillResourceStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract void addHideUnnecessaryViewStatements(MethodSpec.Builder builder, ViewObjectVisitor visitor);
	public abstract List<AbstractLayoutGenerator.View> addCardViewSubView(String packageName, ViewObjectXMLVisitor visitor);
}
