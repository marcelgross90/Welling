package de.fhws.applab.gemara.welling.application.lib.generic.java.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;
public class FragmentHandler extends AbstractModelClass {

	private final ClassName rClassName;

	public FragmentHandler(String packageName) {
		super(packageName + ".generic.util", "FragmentHandler");
		this.rClassName = ClassName.get(packageName, "R");
	}

	public JavaFile javaFile() {

		MethodSpec replaceFragment = MethodSpec.methodBuilder("replaceFragment")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(void.class)
				.addParameter(getFragmentManagerClassName(), "fm")
				.addParameter(getFragmentClassName(), "fragment")
				.addCode("fm.beginTransaction()\n"
						+ ".replace(\n"
						+ "$T.id.content_container, \n"
						+ "fragment, fragment.getClass().getName())\n"
						+ ".addToBackStack(null)\n"
						+ ".commit();", rClassName)
				.build();

		MethodSpec replaceFragmentPopBackStack = MethodSpec.methodBuilder("replaceFragmentPopBackStack")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(void.class)
				.addParameter(getFragmentManagerClassName(), "fm")
				.addParameter(getFragmentClassName(), "fragment")
				.addStatement("fm.popBackStack()")
				.addStatement("$N(fm, fragment)", replaceFragment)
				.build();


		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(replaceFragment)
				.addMethod(replaceFragmentPopBackStack)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
