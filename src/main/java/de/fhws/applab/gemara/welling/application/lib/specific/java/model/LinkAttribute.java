package de.fhws.applab.gemara.welling.application.lib.specific.java.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class LinkAttribute extends Attribute {

	private final ClassName linkClassName;

	public LinkAttribute(String name, String packageName, Modifier... modifiers) {
		super(name, modifiers);
		linkClassName = ClassName.get(packageName + ".generic.model", "Link");
	}

	@Override
	protected void addReturnValue(MethodSpec.Builder builder) {
		builder.returns(linkClassName);
	}

	@Override
	protected void addParameter(MethodSpec.Builder builder) {
		builder.addParameter(linkClassName, this.name);
	}

	@Override
	protected void addField(TypeSpec.Builder builder) {
		builder.addField(FieldSpec.builder(linkClassName, this.name, this.modifiers).build());
	}
}
