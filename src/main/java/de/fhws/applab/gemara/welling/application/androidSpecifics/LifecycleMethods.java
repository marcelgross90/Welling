package de.fhws.applab.gemara.welling.application.androidSpecifics;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getDialogClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getNonNullClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getSavedInstanceStateParam;

public class LifecycleMethods {

	public static MethodSpec.Builder getOnCreate() {
		// @formatter:off
		return MethodSpec.methodBuilder("onCreate")
				.addAnnotation(Override.class)
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PROTECTED).returns(void.class)
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());
		// @formatter:on
	}

	public static MethodSpec.Builder getOnCreateFragment() {
		// @formatter:off
		return MethodSpec.methodBuilder("onCreate")
				.addAnnotation(Override.class)
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());
		// @formatter:on
	}

	public static MethodSpec.Builder getOnCreateDialog() {
		// @formatter:off
		return MethodSpec.methodBuilder("onCreateDialog")
				.addAnnotation(Override.class)
				.addAnnotation(getNonNullClassName())
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(getDialogClassName())
				.addStatement("super.onCreate($N)", getSavedInstanceStateParam());
		// @formatter:on
	}
}
