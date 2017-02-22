package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getIntentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;

public class NewSpecificResourceWithPictureFragment extends AbstractModelClass {

	private final String resourceName;

	private final ClassName rClassName;
	private final ClassName specificResourceInputView;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName detailActivityClassName;
	private final ClassName newResourceFragmentClassName;

	public NewSpecificResourceWithPictureFragment(AppDescription appDescription, InputView inputView) {
		super(appDescription.getAppPackageName() + ".fragment", "New" + inputView.getResourceName() + "Fragment");
		this.resourceName = inputView.getResourceName();

		this.rClassName = ClassName.get(appDescription.getAppPackageName(), "R");
		this.specificResourceInputView = ClassName
				.get(appDescription.getLibPackageName() + ".specific.customView", resourceName + "InputView");
		this.networkCallBackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");
		this.newResourceFragmentClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.fragment", "NewResourceFragment");
		this.detailActivityClassName = ClassName.get(appDescription.getAppPackageName(), resourceName + "DetailActivity");
	}

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(newResourceFragmentClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getGetLayout())
				.addMethod(getInitializeView())
				.addMethod(getGetCallback())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getGetLayout() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "fragment_new_" + resourceName.toLowerCase())
				.build();
		// @formatter:on
	}

	private MethodSpec getInitializeView() {
		// @formatter:off
		return MethodSpec.methodBuilder("initializeView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(void.class)
				.addParameter(getViewClassName(), "view")
				.addStatement("$N = ($T) $N.findViewById($T.id.$N)",
						"inputView", specificResourceInputView, "view", rClassName, resourceName.toLowerCase() + "_input")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetCallback() {
		// @formatter:off
		return MethodSpec.methodBuilder("getCallback")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallBackClassName)
				.addStatement("return $L", getCallback())
				.build();
		// @formatter:on
	}

	private TypeSpec getCallback() {
		// @formatter:off
		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addStatement("$T $N = new $T(getActivity(), $T.class)",
										getIntentClassName(), "intent", getIntentClassName(), detailActivityClassName)
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
				.addMethod(
						MethodSpec.methodBuilder("onFailure")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.build())
				.addMethod(onSuccess)
				.build();
		// @formatter:on
	}
}