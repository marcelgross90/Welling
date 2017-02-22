package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;

@SuppressWarnings("WeakerAccess")
public abstract class InitializeClickableVisitor implements ResourceViewAttributeVisitor {

	protected final MethodSpec.Builder method;
	protected final String viewName;
	protected final ClassName attributeViewClassName;
	protected final ClassName rClassName;

	public InitializeClickableVisitor(MethodSpec.Builder method, String viewName, ClassName attributeViewClassName, ClassName rClassName) {
		this.method = method;
		this.viewName = viewName;
		this.attributeViewClassName = attributeViewClassName;
		this.rClassName = rClassName;
	}
}
