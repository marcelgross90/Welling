package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.LifecycleMethods.*;

public class DeleteDialogFragment extends AbstractModelClass {

	private final AppDescription appDescription;

	private final ClassName deleteDialogListenerClassName;
	private final ClassName rClassName;
	private final ClassName networkClientClassName;
	private final ClassName networkRequestClassName;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;

	private final FieldSpec url = FieldSpec.builder(String.class, "url", Modifier.PRIVATE).build();
	private final FieldSpec deleteDialogListener;

	public DeleteDialogFragment(AppDescription appDescription) {
		super(appDescription.getLibPackageName() + ".generic.fragment", "DeleteDialogFragment");
		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getLibPackageName(), "R");
		this.deleteDialogListenerClassName = ClassName.get(this.packageName + "." + this.className, "DeleteDialogListener");
		this.networkClientClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkRequest");
		this.networkCallBackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");

		this.deleteDialogListener = FieldSpec.builder(deleteDialogListenerClassName, "deleteDialogListener", Modifier.PRIVATE).build();

		addString();
	}

	private void addString() {
		appDescription.setLibStrings("ok", "OK");
		appDescription.setLibStrings("cancel", "Cancel");
		appDescription.setLibStrings("delete_dialog_title", "Delete %s?");
	}

	@Override
	public JavaFile javaFile() {

		TypeSpec networkCallback = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(MethodSpec.methodBuilder("onFailure")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.addStatement("listener.onDialogClosed(false)")
						.returns(void.class)
						.build())
				.addMethod(MethodSpec.methodBuilder("onSuccess")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.addParameter(networkResponseClassName, "response")
						.addStatement("listener.onDialogClosed(true)")
						.returns(void.class)
						.build()).build();

		MethodSpec delete = MethodSpec.methodBuilder("delete")
				.addModifiers(Modifier.PRIVATE).returns(void.class)
				.addParameter(ParameterSpec.builder(deleteDialogListenerClassName, "listener", Modifier.FINAL).build())
				.addStatement("$T client = new $T(getActivity(), new $T().url($N).delete())", networkClientClassName, networkClientClassName, networkRequestClassName, url)
				.addStatement("client.sendRequest($L)", networkCallback)
				.build();



		TypeSpec positiveClick = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(getDialogInterfaceOnClickListenerClassName())
				.addMethod(MethodSpec.methodBuilder("onClick")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.addParameter(getDialogInterfaceClassName(), "dialogInterface")
						.addParameter(int.class, "i")
						.returns(void.class)
						.addStatement("$N($N)", delete, deleteDialogListener)
						.addStatement("dialogInterface.dismiss()")
						.build())
				.build();

		TypeSpec negativeClick = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(getDialogInterfaceOnClickListenerClassName())
				.addMethod(MethodSpec.methodBuilder("onClick")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.addParameter(getDialogInterfaceClassName(), "dialogInterface")
						.addParameter(int.class, "i")
						.returns(void.class)
						.addStatement("dialogInterface.dismiss()")
						.build())
				.build();


		MethodSpec onCreateDialog = getOnCreateDialog()
				.beginControlFlow("try")
				.addStatement("$N = ($T) getActivity()", deleteDialogListener, deleteDialogListenerClassName)
				.endControlFlow()
				.beginControlFlow("catch($T ex)", ClassName.get(ClassCastException.class))
				.addStatement("$N = ($T) getTargetFragment()", deleteDialogListener, deleteDialogListenerClassName)
				.endControlFlow()
				.addStatement("$T bundle = getArguments()", getBundleClassName())
				.addStatement("$N = bundle.getString(\"url\")", url)
				.addStatement("$T name = bundle.getString(\"name\")", ClassName.get(String.class))
				.addCode("return new $T(getActivity())\n" + ".setTitle(getString($T.string.delete_dialog_title, name)) \n"
								+ ".setPositiveButton($T.string.ok, $L) \n" + ".setNegativeButton($T.string.cancel, $L).create();\n",
						getAlertDialogBuilderClassName(), rClassName, rClassName, positiveClick, rClassName, negativeClick)
				.build();

		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getDialogFragmentClassName())
				.addField(url)
				.addField(deleteDialogListener)
				.addType(generateInterface())
				.addMethod(onCreateDialog)
				.addMethod(delete)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private TypeSpec generateInterface() {
		MethodSpec onDialogClosed = MethodSpec.methodBuilder("onDialogClosed")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(void.class)
				.addParameter(boolean.class, "successfullyDeleted").build();

		return TypeSpec.interfaceBuilder(deleteDialogListenerClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(onDialogClosed)
				.build();
	}
}
