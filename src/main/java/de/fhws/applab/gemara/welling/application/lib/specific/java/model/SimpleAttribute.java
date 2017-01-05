package de.fhws.applab.gemara.welling.application.lib.specific.java.model;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class SimpleAttribute extends Attribute {

	private final DataType type;

	public SimpleAttribute(String name, DataType type, Modifier... modifiers) {
		super(name, type, modifiers);
		this.type = type;
	}

	@Override
	protected void addReturnValue(MethodSpec.Builder builder) {
		switch (type) {
			case INT:
				builder.returns(int.class);
				break;
			case STRING:
				builder.returns(String.class);
				break;
			default:
				builder.returns(void.class);
				break;
		}
	}

	@Override
	protected void addParameter(MethodSpec.Builder builder) {
		switch (type) {
			case INT:
				builder.addParameter(int.class, this.name);
				break;
			case STRING:
				builder.addParameter(String.class, this.name);
				break;
			default:
				break;
		}
	}

	@Override
	protected void addField(TypeSpec.Builder builder) {
		switch (type) {
			case INT:
				builder.addField(FieldSpec.builder(int.class, this.name, this.modifiers).build());
				break;
			case STRING:
				builder.addField(FieldSpec.builder(String.class, this.name, this.modifiers).build());
				break;
			default:
				break;
		}

	}
}