package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

public class NetworkCallback extends AbstractModelClass {

	public NetworkCallback(String packageName) {
		super(packageName + ".generic.network", "NetworkCallback");
	}

	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.interfaceBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(MethodSpec.methodBuilder("onFailure")
						.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
						.build())
				.addMethod(MethodSpec.methodBuilder("onSuccess")
						.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
						.addParameter(ParameterSpec.builder(ClassName.get(this.packageName, "NetworkResponse"), "response").build())
						.build())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
