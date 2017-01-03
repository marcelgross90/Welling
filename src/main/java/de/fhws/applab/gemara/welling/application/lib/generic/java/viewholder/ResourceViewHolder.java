package de.fhws.applab.gemara.welling.application.lib.generic.java.viewholder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class ResourceViewHolder extends AbstractModelClass {

	private final ClassName viewHolderClassName = getViewHolderClassName();
	private final ClassName viewClassName = getViewClassName();
	private final ClassName resourceClassName;

	public ResourceViewHolder(String packageName) {
		super(packageName + ".generic.viewholder", "ResourceViewHolder");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
	}

	@Override
	public JavaFile javaFile() {

		MethodSpec constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(viewClassName, "itemView").build())
				.addStatement("super(itemView)")
				.build();

		MethodSpec assignData = MethodSpec.methodBuilder("assignData")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(void.class)
				.addParameter(ParameterSpec.builder(resourceClassName, "resource").addModifiers(Modifier.FINAL).build())
				.build();

		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.superclass(viewHolderClassName)
				.addMethod(constructor)
				.addMethod(assignData)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
