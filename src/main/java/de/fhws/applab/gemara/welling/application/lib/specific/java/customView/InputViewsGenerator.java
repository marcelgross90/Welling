package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView;
import de.fhws.applab.gemara.welling.generator.GetterSetterGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.metaModelExtension.AppDeclareStyleable;

import javax.lang.model.element.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;

public class InputViewsGenerator extends CustomView {

	private final InputView inputView;
	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName attributeInputClassName;
	private final ClassName dateTimeViewClassName;
	private final ClassName resourceClassName;
	private final ClassName specificResourceClassName;
	private final ClassName linkClassName;

	public InputViewsGenerator(AppDescription appDescription, InputView inputView) {
		super(appDescription.getLibPackageName() + ".specific.customView", inputView.getResourceName() + "InputView",
				ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "ResourceInputView"));

		this.inputView = inputView;
		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getLibPackageName(), "R");
		this.attributeInputClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "AttributeInput");
		this.dateTimeViewClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "DateTimeView");
		this.resourceClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Resource");
		this.linkClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Link");
		this.specificResourceClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.model", inputView.getResourceName());
	}

	@Override
	public Modifier[] addClassModifiers() {
		return new Modifier[] { Modifier.PUBLIC };
	}

	@Override
	protected MethodSpec getConstructorOne() {
		return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(getContextParam())
				.addStatement("super($N)", getContextParam()).build();
	}

	@Override
	protected MethodSpec getConstructorTwo() {
		return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(getContextParam())
				.addParameter(getAttributeSetParam()).addStatement("super($N, $N)", getContextParam(), getAttributeSetParam()).build();
	}

	@Override
	protected MethodSpec getConstructorThree() {

		return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(getContextParam())
				.addParameter(getAttributeSetParam()).addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), defStyleAttr).build();
	}

	@Override
	public List<MethodSpec> addConstructors() {
		return getStandardConstructors();
	}

	@Override
	public List<FieldSpec> addFields() {
		List<FieldSpec> fields = new ArrayList<>();
		boolean containsDate = false;
		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				fields.add(FieldSpec.builder(dateTimeViewClassName, inputViewAttribute.getAttributeName(), Modifier.PRIVATE).build());
				containsDate = true;
			} else {
				fields.add(FieldSpec.builder(attributeInputClassName, inputViewAttribute.getAttributeName(), Modifier.PRIVATE).build());
			}
		}

		if (containsDate) {
			fields.add(FieldSpec.builder(specificResourceClassName, "current" + inputView.getResourceName(), Modifier.PRIVATE).build());
			fields.add(FieldSpec.builder(specificResourceClassName, "old" + inputView.getResourceName(), Modifier.PRIVATE).build());
			FieldSpec simpleDateFormat = FieldSpec.builder(SimpleDateFormat.class, "simpleDateFormat", Modifier.PRIVATE, Modifier.FINAL).initializer(
					"new $T($S, $T.GERMANY)", SimpleDateFormat.class, "dd.MM.yyyy HH:mm", Locale.class).build();
			fields.add(simpleDateFormat);
		}
		return fields;
	}

	@Override
	public MethodSpec getInitMethod() {
		return null;
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();

		methods.add(getSetResource());
		methods.add(getGetResource());
		methods.add(getInitializeViews());
		methods.add(getGetLayout());
		methods.add(getGetStyleable());

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				methods.add(getDateToString());
				methods.add(getStringToDate());
				break;
			}
		}

		return methods;
	}

	private MethodSpec getSetResource() {
		String resourceName = inputView.getResourceName().toLowerCase();
		MethodSpec.Builder builder = MethodSpec.methodBuilder("setResource");
		builder.addModifiers(Modifier.PUBLIC);
		builder.addAnnotation(Override.class);
		builder.returns(void.class);
		builder.addParameter(resourceClassName, "resource");
		builder.addStatement("$T $N = ($T) $N", specificResourceClassName, resourceName, specificResourceClassName, "resource");

		boolean containsDate = false;
		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}

			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.URL) {
				builder.addStatement("$N.setText($N.$N().getHref())", inputViewAttribute.getAttributeName(), resourceName,
						GetterSetterGenerator.getGetter(inputViewAttribute.getAttributeName()));
			} else if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				containsDate = true;
				builder.addStatement("$N.setText($N($N.$N()))", inputViewAttribute.getAttributeName(), getDateToString(), resourceName,
						GetterSetterGenerator.getGetter(inputViewAttribute.getAttributeName()));
			} else {
				builder.addStatement("$N.setText($N.$N())", inputViewAttribute.getAttributeName(), resourceName,
						GetterSetterGenerator.getGetter(inputViewAttribute.getAttributeName()));
			}
		}

		if (containsDate) {
			builder.addStatement("this.$N = $N", "old" + inputView.getResourceName(), inputView.getResourceName().toLowerCase());
		}

		return builder.build();
	}

	private MethodSpec getGetResource() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("getResource");
		builder.addModifiers(Modifier.PUBLIC);
		builder.addAnnotation(Override.class);
		builder.returns(resourceClassName);
		builder.addStatement("$T $N = $N", boolean.class, "error", "false");

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}
			String stringName = replaceIllegalCharacters(inputViewAttribute.getAttributeName().toLowerCase());
			String viewName = replaceIllegalCharacters(inputViewAttribute.getAttributeName());

			addString(viewName + "_missing", inputViewAttribute.getMissingText());

			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				builder.addStatement("$T $N = $N.getText().toString()", String.class, stringName + "String", viewName);
			} else {
				builder.addStatement("$T $N = $N.getText()", String.class, stringName + "String", viewName);
			}

			builder.beginControlFlow("if ($N.isEmpty())", stringName + "String");
			builder.addStatement("$N.setError($N.getString($T.string.$N))", viewName, "context", rClassName, viewName + "_missing");
			builder.addStatement("$N = $N", "error", "true");
			builder.endControlFlow();
		}

		builder.beginControlFlow("if (!$N)", "error");
		boolean containsDate = false;
		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				containsDate = true;
				break;
			}
		}

		if (containsDate) {
			getResourceWithDate(builder);
		} else {
			getResourceWithoutDate(builder);
		}


		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}

			String stringName = inputViewAttribute.getAttributeName().toLowerCase() + "String";

			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.URL) {
				builder.addStatement("$T $N = new $T($N, $S, $S)", linkClassName, inputViewAttribute.getAttributeName() + "Url",
						linkClassName, stringName, inputViewAttribute.getAttributeName().toLowerCase(), "text/html");
				builder.addStatement("$N.$N($N)", "current" + inputView.getResourceName(), GetterSetterGenerator.getSetter(inputViewAttribute.getAttributeName()),
						inputViewAttribute.getAttributeName() + "Url");
			} else if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				builder.addStatement("$N.$N($N($N))", "current" + inputView.getResourceName(), GetterSetterGenerator.getSetter(inputViewAttribute.getAttributeName()),
						getStringToDate(), stringName);
			} else {
				builder.addStatement("$N.$N($N)", "current" + inputView.getResourceName(), GetterSetterGenerator.getSetter(inputViewAttribute.getAttributeName()),
						stringName);
			}
		}

		builder.addStatement("return $N", "current" + inputView.getResourceName());
		builder.endControlFlow();
		builder.addStatement("return null");

		return builder.build();
	}

	private void getResourceWithDate(MethodSpec.Builder builder) {

		builder.beginControlFlow("if ($N == $L)",  "current" + inputView.getResourceName(), null);
		builder.addStatement("$N = new $T()",  "current" + inputView.getResourceName(), specificResourceClassName);
		builder.endControlFlow();
		builder.beginControlFlow("if ($N != $L)", "old" + inputView.getResourceName(), null);
		builder.addStatement("$N.setId($N.getId())", "current" + inputView.getResourceName(), "old" + inputView.getResourceName());
		builder.endControlFlow();

	}

	private void getResourceWithoutDate(MethodSpec.Builder builder) {
		builder.addStatement("$T $N = new $T()", specificResourceClassName, "current" + inputView.getResourceName(), specificResourceClassName);
	}


	private MethodSpec getInitializeViews() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeView");
		builder.addModifiers(Modifier.PUBLIC);
		builder.addAnnotation(Override.class);
		builder.returns(void.class);

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}

			String id = Character.toLowerCase(inputViewAttribute.getAttributeName().charAt(0)) + inputViewAttribute.getAttributeName()
					.substring(1);
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				builder.addStatement("$N = ($T) findViewById($T.id.$N)", inputViewAttribute.getAttributeName(), dateTimeViewClassName,
						rClassName, id);
			} else {
				builder.addStatement("$N = ($T) findViewById($T.id.$N)", inputViewAttribute.getAttributeName(), attributeInputClassName,
						rClassName, id);
			}
		}

		return builder.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout").addModifiers(Modifier.PROTECTED).addAnnotation(Override.class).returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "view_" + inputView.getResourceName().toLowerCase() + "_input").build();
	}

	private MethodSpec getGetStyleable() {
		appDescription.setDeclareStyleables(replaceIllegalCharacters(inputView.getResourceName() + "InputView"), new AppDeclareStyleable.DeclareStyleable(replaceIllegalCharacters(inputView.getResourceName() + "InputView")));
		return MethodSpec.methodBuilder("getStyleable").addModifiers(Modifier.PROTECTED).addAnnotation(Override.class).returns(int[].class)
				.addStatement("return $T.styleable.$N", rClassName, replaceIllegalCharacters(inputView.getResourceName() + "InputView")).build();
	}


	private MethodSpec getDateToString() {
		return MethodSpec.methodBuilder("dateToString")
				.addModifiers(Modifier.PRIVATE)
				.returns(String.class)
				.addParameter(Date.class, "date")
				.addStatement("return $N.format($N)", "simpleDateFormat", "date")
				.build();
	}

	private MethodSpec getStringToDate() {
		return MethodSpec.methodBuilder("stringToDate")
				.addModifiers(Modifier.PRIVATE)
				.returns(Date.class)
				.addParameter(String.class, "date")
				.beginControlFlow("try")
				.addStatement("return $N.parse($N)", "simpleDateFormat", "date")
				.endControlFlow()
				.beginControlFlow("catch ($T $N)", ParseException.class, "ex")
				.addStatement("$N.printStackTrace()", "ex")
				.addStatement("return new $T()", Date.class)
				.endControlFlow()
				.build();
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(replaceIllegalCharacters(key), value);

	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}
}
