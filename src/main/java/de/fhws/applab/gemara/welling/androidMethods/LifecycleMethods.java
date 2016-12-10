package de.fhws.applab.gemara.welling.androidMethods;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class LifecycleMethods {

	public static MethodSpec.Builder getOnCreate() {

		return MethodSpec.methodBuilder("onCreate")
				.addAnnotation(Override.class)
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PROTECTED).returns(void.class)
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());

	}

	public static MethodSpec.Builder getOnCreateFragment() {
		return MethodSpec.methodBuilder("onCreate")
				.addAnnotation(Override.class)
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());
	}

	public static MethodSpec.Builder getOnCreateView() {

		return MethodSpec.methodBuilder("onCreateView")
				.addAnnotation(Override.class)
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(getViewClassName())
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());

	}

	public static MethodSpec.Builder getOnCreateDialog() {
		return MethodSpec.methodBuilder("onCreateDialog")
				.addAnnotation(Override.class)
				.addAnnotation(getNonNullClassName())
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(getDialogClassName())
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());
	}


}
