package de.fhws.applab.gemara.welling.application.app.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class MainActivity extends AbstractModelClass {

	private final AppResource startResource;
	private final ClassName rClassName;
	private final ClassName abstractMainActivityClassName;
	private final ClassName resourceListFragmentClassName;


	public MainActivity(String packageName, AppResource startResource, String appName) {
		super(packageName, "MainActivity");
		this.startResource = startResource;

		this.rClassName = ClassName.get(packageName, "R");
		this.abstractMainActivityClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.activity", "AbstractMainActivity");
		this.resourceListFragmentClassName = ClassName.get(packageName + ".fragment", startResource.getResourceName() + "ListFragment");
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(abstractMainActivityClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getGetStartFragment())
				.addMethod(getGetLoadErrorMessage())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getGetStartFragment() {
		return MethodSpec.methodBuilder("getStartFragment")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getFragmentClassName())
				.addStatement("return new $T()", resourceListFragmentClassName)
				.build();
	}

	private MethodSpec getGetLoadErrorMessage() {
		return MethodSpec.methodBuilder("getLoadErrorMessage")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.string.$N", rClassName, "load_" + startResource.getResourceName().toLowerCase() + "_error")
				.build();
	}

}
