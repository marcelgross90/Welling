package de.fhws.applab.gemara.welling.application.lib.generic.java.viewholder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewHolderClassName;

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
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.superclass(viewHolderClassName)
				.addMethod(getConstructor())
				.addMethod(getAssignData())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getAssignData() {
		// @formatter:off
		return MethodSpec.methodBuilder("assignData")
					.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
					.returns(void.class)
					.addParameter(
							ParameterSpec.builder(resourceClassName, "resource")
									.addModifiers(Modifier.FINAL)
									.build())
					.build();
		// @formatter:on
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PUBLIC)
					.addParameter(viewClassName, "itemView")
					.addStatement("super(itemView)")
					.build();
		// @formatter:on
	}
}