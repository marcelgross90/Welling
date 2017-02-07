package de.fhws.applab.gemara.welling.application.lib.specific.java.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceGenerator extends AbstractModelClass {

	private final ClassName resourceClassName;
	private final ClassName specificResourceClassName;
	private final AppResource appResource;

	private final List<Attribute> attributes = new ArrayList<>();

	public ResourceGenerator(String packageName, AppResource appResource) {
		super(packageName + ".specific.model", appResource.getResourceName());
		this.appResource = appResource;
		this.specificResourceClassName = ClassName.get(this.packageName, this.className);

		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");

		this.attributes.addAll(appResource.getAttributes());
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addModifiers(Modifier.PUBLIC);
		type.addSuperinterface(resourceClassName);
		addFields(type);
		type.addMethod(getStandardConstructor());
		type.addMethod(getConstructor());
		for (Attribute attribute : this.attributes) {
			type.addMethods(attribute.createGetterSetter());
		}
		type.addMethod(getEquals());
		type.addMethod(getHashCode());

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private void addFields(TypeSpec.Builder type) {
		for (Attribute attribute : this.attributes) {
			attribute.addField(type);
		}
	}

	private MethodSpec getStandardConstructor() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addCode("//for Genson\n")
				.build();
	}

	private MethodSpec getConstructor() {
		MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
		constructor.addModifiers(Modifier.PUBLIC);
		for (Attribute attribute : this.attributes) {
			attribute.addParameter(constructor);
			constructor.addStatement("this.$N = $N", attribute.getName(), attribute.getName());
		}

		return constructor.build();
	}

	private MethodSpec getEquals() {
		ParameterSpec o = ParameterSpec.builder(Object.class, "o").build();
		String resourceName = appResource.getResourceName().toLowerCase();
		MethodSpec.Builder builder = MethodSpec.methodBuilder("equals");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(boolean.class);
		builder.addParameter(o);
		builder.addStatement("if (this == $N) return true", o);
		builder.addStatement("if ($N == null || getClass() != $N.getClass()) return false", o, o);
		builder.addStatement("$T $N = ($T) $N", specificResourceClassName, resourceName, specificResourceClassName, o);

		List<Attribute> attributes = this.attributes.stream().filter(attribute -> attribute.getDataType() == Attribute.DataType.STRING
				|| attribute.getDataType() == Attribute.DataType.INT).collect(Collectors.toList());

		for (int i = 0; i < attributes.size(); i++) {
			String attributeName = attributes.get(i).getName();
			if (i == attributes.size()-1) {
				if (attributes.get(i).dataType == Attribute.DataType.INT) {
					builder.addStatement("return $N == $N.$N", attributeName, resourceName, attributeName);
				} else if (attributes.get(i).dataType == Attribute.DataType.STRING) {
					builder.addStatement("return $N != null ? $N.equals($N.$N) : $N.$N == null", attributeName, attributeName, resourceName, attributeName, resourceName, attributeName);
				}
			} else {
				if (attributes.get(i).dataType == Attribute.DataType.INT) {
					builder.addStatement("if ($N != $N.$N) return false", attributeName, resourceName, attributeName);
				} else if (attributes.get(i).dataType == Attribute.DataType.STRING) {
					builder.addStatement("if ($N != null ? !$N.equals($N.$N) : $N.$N != null) return false", attributeName, attributeName, resourceName, attributeName, resourceName, attributeName);
				}
			}

		}
		return builder.build();
	}

	private MethodSpec getHashCode() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("hashCode");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(int.class);
		boolean firstAttempt = true;
		for (Attribute attribute: attributes) {
			String attributeName = attribute.getName();
			if (firstAttempt) {
				if (attribute.getDataType() == Attribute.DataType.STRING) {
					builder.addStatement("$T $N = $N != null ? $N.hashCode() : 0", int.class, "result", attributeName, attributeName);
				} else if (attribute.getDataType() == Attribute.DataType.INT) {
					builder.addStatement("$T $N = $N", int.class, "result", attributeName);
				}
				firstAttempt = false;
			} else {
				if (attribute.getDataType() == Attribute.DataType.STRING) {
					builder.addStatement("$N = 31 * $N + ($N != null ? $N.hashCode() : 0)", "result", "result", attributeName, attributeName);
				} else if (attribute.getDataType() == Attribute.DataType.INT) {
					builder.addStatement("$N = 31 * $N + $N", "result", "result", attributeName);
				}
			}
		}
		builder.addStatement("return $N", "result");
		return builder.build();
	}
}
