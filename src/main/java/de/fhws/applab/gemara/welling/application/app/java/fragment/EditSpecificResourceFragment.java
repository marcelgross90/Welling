package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class EditSpecificResourceFragment extends AbstractModelClass {

	private final String resourceName;
	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName specificResourceInputViewClassName;
	private final ClassName specificResourceClassName;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName editResourceFragmentClassName;

	private final ParameterizedTypeName linkMap;

	public EditSpecificResourceFragment(AppDescription appDescription, InputView inputView) {
		super(appDescription.getAppPackageName() + ".fragment", "Edit" + inputView.getResourceName() + "Fragment");
		this.resourceName = inputView.getResourceName();
		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getAppPackageName(), "R");
		this.specificResourceInputViewClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.customView", resourceName + "InputView");
		this.specificResourceClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.model", resourceName);
		this.networkCallBackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");
		this.editResourceFragmentClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.fragment", "EditResourceFragment");
		ClassName linkClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Link");
		linkMap = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), linkClassName);
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.superclass(editResourceFragmentClassName)
				.addMethod(getGetLayout())
				.addMethod(getInitializeView())
				.addMethod(getGetLoadCallback())
				.addMethod(getGetSaveCallback())
				.build();

		return JavaFile.builder(this.packageName, type).build();
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
				.addStatement("$N = ($T) $N.findViewById($T.id.$N)", "inputView", specificResourceInputViewClassName, "view", rClassName,
						resourceName.toLowerCase() + "_input")
				.build();
	}

	private MethodSpec getGetLoadCallback() {
		return MethodSpec.methodBuilder("getLoadCallBack")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallBackClassName)
				.addStatement("return $L", getLoadCallback())
				.build();
	}

	private TypeSpec getLoadCallback() {
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
				.addStatement("final $T $N = $N.deserialize($N.getResponseReader(), $T.class)", specificResourceClassName,
						resourceName.toLowerCase(), "genson", "response", specificResourceClassName)
				.addStatement("$T $N = $N.getLinkHeader()", linkMap, "linkHeader", "response")
				.addStatement("$N = $N.get(getActivity().getString($T.string.$N))", "resourceEditLink", "linkHeader", rClassName,
						appDescription.getAppRestAPI().getRestApi().get("PUT_" + resourceName).getKey())
				.addStatement("getActivity().runOnUiThread($L)", runnable)
				.build();

		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(MethodSpec.methodBuilder("onFailure").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class).build())
				.addMethod(onSuccess)
				.build();
	}

	private MethodSpec getGetSaveCallback() {
		return MethodSpec.methodBuilder("getSaveCallBack")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallBackClassName)
				.addStatement("return $L", getSaveCallback())
				.build();
	}

	private TypeSpec getSaveCallback() {
		addString(replaceIllegalCharacters(resourceName.toLowerCase() + "_updated"), resourceName + " updated!");
		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addStatement("$T.makeText(getActivity(), $T.string.$N, $T.LENGTH_SHORT).show()", getToastClassName(), rClassName, replaceIllegalCharacters(resourceName.toLowerCase() + "_updated"), getToastClassName())
								.addStatement("getActivity().onBackPressed()")
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

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(key, value);
	}
}
