package de.fhws.applab.gemara.welling.androidMethods;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

public class ControllFlowMethods {

	public static MethodSpec.Builder getRunOnUiThread() {
		return MethodSpec.methodBuilder("runOnUiThread").addModifiers(Modifier.PUBLIC);
	}
}
