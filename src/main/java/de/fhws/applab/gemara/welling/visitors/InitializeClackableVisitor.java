package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;

public abstract class InitializeClackableVisitor implements ResourceViewAttributeVisitor {

	protected MethodSpec.Builder method;
	protected String viewName;
	protected ClassName attributeViewClassName;
	protected ClassName rClassName;

	public InitializeClackableVisitor(MethodSpec.Builder method, String viewName, ClassName attributeViewClassName,
			ClassName rClassName) {
		this.method = method;
		this.viewName = viewName;
		this.attributeViewClassName = attributeViewClassName;
		this.rClassName = rClassName;
	}
}
