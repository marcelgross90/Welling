package de.fhws.applab.gemara.welling.application.lib.specific.java.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ResourceGenerator extends AbstractModelClass {

	private ClassName resourceClassName = ClassName.get(this.packageName + ".generic.model", "Resource");

	private final List<Attribute> attributes = new ArrayList<>();

	public ResourceGenerator(String packageName, AppResource appResource) {
		super(packageName, appResource.getResourceName());
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

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private void addFields(TypeSpec.Builder type) {
		for (Attribute attribute : this.attributes) {
			attribute.addField(type);
		}
	}

	private MethodSpec getStandardConstructor() {
		return MethodSpec.constructorBuilder()
				.addCode("//for Genson\n")
				.build();
	}

	private MethodSpec getConstructor() {
		MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
		for (Attribute attribute : this.attributes) {
			attribute.addParameter(constructor);
			constructor.addStatement("this.$N = $N", attribute.getName(), attribute.getName());
		}

		return constructor.build();
	}
}
