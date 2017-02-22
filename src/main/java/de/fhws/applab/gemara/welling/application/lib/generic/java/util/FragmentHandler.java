package de.fhws.applab.gemara.welling.application.lib.generic.java.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentManagerClassName;

public class FragmentHandler extends AbstractModelClass {

	private final ClassName rClassName;

	public FragmentHandler(String packageName) {
		super(packageName + ".generic.util", "FragmentHandler");
		this.rClassName = ClassName.get(packageName, "R");
	}

	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getReplaceFragment())
				.addMethod(getReplaceFragmentPopBackStack())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getReplaceFragmentPopBackStack() {
		// @formatter:off
		return MethodSpec.methodBuilder("replaceFragmentPopBackStack")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(void.class)
					.addParameter(getFragmentManagerClassName(), "fm")
					.addParameter(getFragmentClassName(), "fragment")
					.addStatement("$N.popBackStack()", "fm")
					.addStatement("$N($N, $N)", getReplaceFragment(), "fm", "fragment")
					.build();
		// @formatter:on
	}

	private MethodSpec getReplaceFragment() {
		// @formatter:off
		return MethodSpec.methodBuilder("replaceFragment")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(void.class)
					.addParameter(getFragmentManagerClassName(), "fm")
					.addParameter(getFragmentClassName(), "fragment")
					.addCode("$N.beginTransaction()\n"
									+ ".replace(\n"
									+ "$T.id.$N, \n"
									+ "$N, $N.getClass().getName())\n"
									+ ".addToBackStack(null)\n"
									+ ".commit();",
							"fm", rClassName, "content_container", "fragment", "fragment")
					.build();
		// @formatter:on
	}
}