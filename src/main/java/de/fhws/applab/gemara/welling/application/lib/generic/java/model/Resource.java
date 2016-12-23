package de.fhws.applab.gemara.welling.application.lib.generic.java.model;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

public class Resource extends AbstractModelClass {

	public Resource(String packageName, String className) {
		super(packageName + ".generic.model", className);
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.interfaceBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.build();
		return JavaFile.builder(this.packageName, type).build();
	}
}
