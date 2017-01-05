package de.fhws.applab.gemara.welling.application.lib.generic.java.model;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

public class Resource extends AbstractModelClass {

	public Resource(String packageName) {
		super(packageName + ".generic.model", "Resource");
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.interfaceBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.build();
		return JavaFile.builder(this.packageName, type).build();
	}
}
