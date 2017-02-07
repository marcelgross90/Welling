package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class NewSpecificResourceFragment extends AbstractModelClass {

	private final AppResource appResource;

	private final ClassName rClassName;
	private final ClassName specificResourceInputView;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName detailActivityClassName;
	private final ClassName newResourceFragmentClassName;

	public NewSpecificResourceFragment(String packageName, AppResource appResource, String appName) {
		super(packageName + ".fragment", "New" + appResource.getResourceName() + "Fragment");
		this.appResource = appResource;

		this.rClassName = ClassName.get(packageName, "R");
		this.specificResourceInputView = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.specific.customView", appResource.getResourceName() + "InputView");
		this.networkCallBackClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkResponse");
		this.newResourceFragmentClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.fragment", "NewSpecificResourceFragment");
		this.detailActivityClassName = ClassName.get(packageName, appResource.getResourceName() + "DetailActivity");
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(newResourceFragmentClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getGetLayout())
				.addMethod(getInitializeView())
				.addMethod(getGetCallback())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "fragment_new_" + appResource.getResourceName().toLowerCase())
				.build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(void.class)
				.addParameter(getViewClassName(), "view")
				.addStatement("$N = ($T) $N.findViewById($T.id.$N)", "inputView", specificResourceInputView, "view", rClassName, appResource.getResourceName().toLowerCase() + "_input")
				.build();
	}

	private MethodSpec getGetCallback() {
		return MethodSpec.methodBuilder("getCallback")
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
								.addStatement("$T $N = new $T(getActivity(), $T.class)", getIntentClassName(), "intent", getIntentClassName(), detailActivityClassName)
								.addStatement("$N.putExtra($S, $N.getHeader().get($S).get(0))", "intent", "selfUrl", "response", "location")
								.addStatement("getActivity().startActivity($N)", "intent")
								.addStatement("getFragmentManager().popBackStack()")
								.build()
				).build();

		MethodSpec onSuccess = MethodSpec.methodBuilder("onSuccess")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(networkResponseClassName, "response", Modifier.FINAL)
				.addStatement("getActivity().runOnUiThread($L)", runnable)
				.build();

		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(MethodSpec.methodBuilder("onFailure").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class).build())
				.addMethod(onSuccess)
				.build();
	}
}
