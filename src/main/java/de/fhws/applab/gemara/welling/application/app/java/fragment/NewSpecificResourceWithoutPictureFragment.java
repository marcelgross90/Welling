package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.Date;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getToastClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewOnClickListenerClassName;

public class NewSpecificResourceWithoutPictureFragment extends AbstractModelClass {

	private final String resourceName;
	private final InputView inputView;
	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName specificResourceInputView;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName newResourceFragmentClassName;
	private final ClassName dateTimeViewClassName;
	private final ClassName onDateTimeSetListenerClassName;
	private final ClassName dateTimePickerFragmentClassName;
	private final ClassName thisClassName;

	public NewSpecificResourceWithoutPictureFragment(AppDescription appDescription, InputView inputView) {
		super(appDescription.getAppPackageName() + ".fragment", "New" + inputView.getResourceName() + "Fragment");
		this.resourceName = inputView.getResourceName();
		this.inputView = inputView;
		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getAppPackageName(), "R");
		this.specificResourceInputView = ClassName
				.get(appDescription.getLibPackageName() + ".specific.customView", resourceName + "InputView");
		this.networkCallBackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");
		this.newResourceFragmentClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.fragment", "NewResourceFragment");
		this.dateTimeViewClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "DateTimeView");
		this.onDateTimeSetListenerClassName = ClassName
				.get(appDescription.getLibPackageName() + ".generic.fragment.DateTimePickerFragment", "OnDateTimeSetListener");
		this.dateTimePickerFragmentClassName = ClassName
				.get(appDescription.getLibPackageName() + ".generic.fragment", "DateTimePickerFragment");
		this.thisClassName = ClassName.get(this.packageName, this.className);

		addString();
	}

	private void addString() {
		appDescription.setLibStrings(resourceName.toLowerCase() + "_saved", resourceName + " saved!");
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.superclass(newResourceFragmentClassName);

		type.addModifiers(Modifier.PUBLIC);
		type.addMethod(getGetLayout());
		type.addMethod(getInitializeView());
		type.addMethod(getGetCallback());
		boolean containsDate = false;
		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				containsDate = true;
				type.addField(
						FieldSpec.builder(dateTimeViewClassName, inputViewAttribute.getAttributeName() + "View", Modifier.PRIVATE).build());
			}
		}

		if (containsDate) {
			type.addSuperinterface(getViewOnClickListenerClassName());
			type.addSuperinterface(onDateTimeSetListenerClassName);
			type.addMethod(getOnClick());
			type.addMethod(getOnDateTimeSet());

			type.addField(FieldSpec.builder(int.class, "state", Modifier.PRIVATE).build());
		}

		return JavaFile.builder(this.packageName, type.build()).build();
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
		MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeView");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PROTECTED);
		builder.returns(void.class);
		builder.addParameter(getViewClassName(), "view");
		builder.addStatement("$N = ($T) $N.findViewById($T.id.$N)", "inputView", specificResourceInputView, "view", rClassName,
				resourceName.toLowerCase() + "_input");

		inputView.getInputViewAttributes().stream()
				.filter(inputViewAttribute -> inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE)
				.forEach(inputViewAttribute -> {
					builder.addStatement("$N = ($T) $N.findViewById($T.id.$N)", inputViewAttribute.getAttributeName() + "View",
							dateTimeViewClassName, "view", rClassName, inputViewAttribute.getAttributeName());
					builder.addStatement("$N.setOnClickListener(this)", inputViewAttribute.getAttributeName() + "View");
				});

		return builder.build();
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
								.addStatement("$T.makeText(getActivity(), $T.string.$N, $T.LENGTH_SHORT).show()", getToastClassName(),
										rClassName, resourceName.toLowerCase() + "_saved", getToastClassName())
								.addStatement("getFragmentManager().popBackStack()")
								.build())
				.build();

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

	private MethodSpec getOnClick() {
		// @formatter:off
		return MethodSpec.methodBuilder("onClick")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(getViewClassName(), "view")
				.addStatement("this.$N = $N.getId()", "state", "view")
				.addStatement("$T $N = new $T()", dateTimePickerFragmentClassName, "dateTimePickerFragment", dateTimePickerFragmentClassName)
				.addStatement("$N.setTargetFragment($T.this, 0)", "dateTimePickerFragment", thisClassName)
				.addStatement("$N.show(getFragmentManager(), $S)", "dateTimePickerFragment", "dateTime")
				.build();
		// @formatter:on
	}

	private MethodSpec getOnDateTimeSet() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("onDateTimeSet");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(void.class);
		builder.addParameter(Date.class, "date");
		builder.beginControlFlow("switch($N)", "state");

		inputView.getInputViewAttributes().stream()
				.filter(inputViewAttribute -> inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE)
				.forEach(inputViewAttribute -> {
					builder.addCode("case $T.id.$N: \n", rClassName, inputViewAttribute.getAttributeName());
					builder.addStatement("$N.setDate($N)", inputViewAttribute.getAttributeName() + "View", "date");
					builder.addStatement("break");
				});

		builder.endControlFlow();
		return builder.build();
	}
}