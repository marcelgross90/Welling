package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView;
import de.fhws.applab.gemara.welling.application.util.GetterSetterGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;

public class InputViewsGenerator extends CustomView {

	private final InputView inputView;
	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName attributeInputClassName;
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

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}

			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				//todo add field for date
			} else {
				fields.add(FieldSpec.builder(attributeInputClassName, inputViewAttribute.getAttributeName(), Modifier.PRIVATE).build());
			}
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

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}

			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.URL) {
				builder.addStatement("$N.setText($N.$N().getHref())", inputViewAttribute.getAttributeName(), resourceName,
						GetterSetterGenerator.getGetter(inputViewAttribute.getAttributeName()));
			} else {
				builder.addStatement("$N.setText($N.$N())", inputViewAttribute.getAttributeName(), resourceName,
						GetterSetterGenerator.getGetter(inputViewAttribute.getAttributeName()));
			}
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

			builder.addStatement("$T $N = $N.getText()", String.class, stringName + "String", viewName);
			builder.beginControlFlow("if ($N.isEmpty())", stringName + "String");
			builder.addStatement("$N.setError($N.getString($T.string.$N))", viewName, "context", rClassName, viewName + "_missing");
			builder.addStatement("$N = $N", "error", "true");
			builder.endControlFlow();
		}

		builder.beginControlFlow("if (!$N)", "error");
		builder.addStatement("$T $N = new $T()", specificResourceClassName, "current", specificResourceClassName);

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
				continue;
			}

			String stringName = inputViewAttribute.getAttributeName().toLowerCase() + "String";

			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.URL) {
				builder.addStatement("$T $N = new $T($N, $S, $S)", linkClassName, inputViewAttribute.getAttributeName() + "Url",
						linkClassName, stringName, inputViewAttribute.getAttributeName().toLowerCase(), "text/html");
				builder.addStatement("$N.$N($N)", "current", GetterSetterGenerator.getSetter(inputViewAttribute.getAttributeName()),
						inputViewAttribute.getAttributeName() + "Url");
			} else {
				builder.addStatement("$N.$N($N)", "current", GetterSetterGenerator.getSetter(inputViewAttribute.getAttributeName()),
						stringName);
			}
		}

		builder.addStatement("return $N", "current");
		builder.endControlFlow();
		builder.addStatement("return null");

		return builder.build();
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
			builder.addStatement("$N = ($T) findViewById($T.id.$N)", inputViewAttribute.getAttributeName(), attributeInputClassName,
					rClassName, id);
		}

		return builder.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout").addModifiers(Modifier.PROTECTED).addAnnotation(Override.class).returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "view_" + inputView.getResourceName().toLowerCase() + "_input").build();
	}

	private MethodSpec getGetStyleable() {
		appDescription.setDeclareStyleables(new AppDeclareStyleable.DeclareStyleable(replaceIllegalCharacters(inputView.getResourceName() + "InputView")));
		return MethodSpec.methodBuilder("getStyleable").addModifiers(Modifier.PROTECTED).addAnnotation(Override.class).returns(int[].class)
				.addStatement("return $T.styleable.$N", rClassName, replaceIllegalCharacters(inputView.getResourceName() + "InputView")).build();
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(replaceIllegalCharacters(key), value);

	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}
}
