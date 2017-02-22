package de.fhws.applab.gemara.welling.application.lib.specific.java.model;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class Attribute {

	public enum DataType {
		INT, STRING, LINK, DATE
	}

	protected abstract void addReturnValue(MethodSpec.Builder builder);

	protected abstract void addParameter(MethodSpec.Builder builder);

	protected abstract void addField(TypeSpec.Builder builder);

	protected final String name;
	protected final Modifier[] modifiers;
	protected final DataType dataType;

	public Attribute(String name, DataType dataType, Modifier... modifiers) {
		this.name = name;
		this.modifiers = modifiers;
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public List<MethodSpec> createGetterSetter() {
		List<MethodSpec> getterSetter = new ArrayList<>();
		getterSetter.add(createGetter());
		getterSetter.add(createSetter());

		return getterSetter;
	}

	public DataType getDataType() {
		return dataType;
	}

	private MethodSpec createGetter() {
		String name = getNameWithCapitalStart();

		MethodSpec.Builder getter = MethodSpec.methodBuilder("get" + name);
		getter.addModifiers(Modifier.PUBLIC);
		addReturnValue(getter);
		getter.addStatement("return $N", this.name);

		return getter.build();
	}

	private MethodSpec createSetter() {
		String name = getNameWithCapitalStart();

		MethodSpec.Builder setter = MethodSpec.methodBuilder("set" + name);
		setter.addModifiers(Modifier.PUBLIC);
		setter.returns(void.class);
		addParameter(setter);
		setter.addStatement("this.$N = $N", this.name, this.name);

		return setter.build();
	}

	private String getNameWithCapitalStart() {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
}