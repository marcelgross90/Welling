package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class EditSpecificResourceFragment extends AbstractModelClass {

	private final String resourceName;
	private final AppResource appResource;
	private final ClassName rClassName;
	private final ClassName specificResourceInputViewClassname;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;

	public EditSpecificResourceFragment(String packageName, String appName, AppResource appResource) {
		super(packageName + ".fragment", "Edit" + appResource.getResourceName() + "Fragment");
		this.appResource = appResource;
		this.resourceName = appResource.getResourceName();

		this.rClassName = ClassName.get(packageName, "R");
		this.specificResourceInputViewClassname = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.specific.customView", appResource.getResourceName() + "Inputview");
		this.networkCallBackClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkResponse");
	}

	@Override
	public JavaFile javaFile() {
		return null;
	}


	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "fragment_edit_" + resourceName.toLowerCase())
				.build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(void.class)
				.addParameter(getViewClassName(), "view")
				.addStatement("$N = ($T) $N.findViewById($T.id.$N)", "inputView", specificResourceInputViewClassname, rClassName, resourceName.toLowerCase() + "_input")
				.build();
	}

	private MethodSpec getGetLoadCallback() {
		return MethodSpec.methodBuilder("getLoadCallback")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallBackClassName)
				.addStatement("return $L", getCallback())
				.build();
	}

	private TypeSpec getCallback() {
		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addStatement("$N.setResource($N)", "inputView", resourceName.toLowerCase())
								.build()
				).build();

		MethodSpec onSuccess = MethodSpec.methodBuilder("onSuccess")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(networkResponseClassName, "response", Modifier.FINAL)
				//todo go on
				.addStatement("getActivity().runOnUiThread($L)", runnable)
				.build();

		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(MethodSpec.methodBuilder("onFailure").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class).build())
				.addMethod(onSuccess)
				.build();
	}
}
