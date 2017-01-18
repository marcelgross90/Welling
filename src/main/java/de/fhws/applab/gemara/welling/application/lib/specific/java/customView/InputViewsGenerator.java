package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.inputView.AppInputView;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.AttributeType;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.SingleViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.visitors.ViewObjectVisitorImpl;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;

public class InputViewsGenerator extends CustomView {

	private final AppInputView appInputView;
	private final AppResource appResource;

	private final ClassName attributeInputClassName;
	private final ClassName resourceClassName;
	private final ClassName specificResourceClassName;
	private final ClassName linkClassName;


	public InputViewsGenerator(String packageName, AppResource appResource) {
		super(packageName + ".specific.customView", appResource.getResourceName() + "InputView", ClassName.get(packageName + ".generic.customView", "ResourceInputView"));

		this.appInputView = appResource.getInputView();
		this.appResource = appResource;

		this.attributeInputClassName = ClassName.get(packageName + ".generic.customView", "AttributeInput");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", appResource.getResourceName());

	}

	@Override
	public Modifier[] addClassModifiers() {
		return new Modifier[]{Modifier.PUBLIC};
	}

	@Override
	protected MethodSpec getConstructorOne() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addStatement("super($N)", getContextParam())
				.build();
	}

	@Override
	protected MethodSpec getConstructorTwo() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.build();
	}

	@Override
	protected MethodSpec getConstructorThree() {

		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), defStyleAttr)
				.build();
	}

	@Override
	public List<MethodSpec> addConstructors() {
		return getStandardConstructors();
	}

	@Override
	public List<FieldSpec> addFields() {
		List<FieldSpec> fields = new ArrayList<>();
		for (SingleViewObject singleViewObject : appInputView.getViewAttributes()) {
			if (singleViewObject.getViewAttribute().getType() == AttributeType.DATE) {
				//todo add field for date
			} else {
				fields.add(FieldSpec.builder(attributeInputClassName, singleViewObject.getViewName(), Modifier.PRIVATE).build());
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
		String resourceName = appResource.getResourceName().toLowerCase();
		MethodSpec.Builder builder = MethodSpec.methodBuilder("setResource");
		builder.addModifiers(Modifier.PUBLIC);
		builder.addAnnotation(Override.class);
		builder.returns(void.class);
		builder.addParameter(resourceClassName, "resource");
		builder.addStatement("$T $N = ($T) $N", specificResourceClassName, resourceName,
				specificResourceClassName, "resource");

		for (SingleViewObject singleViewObject : appInputView.getViewAttributes()) {
			if (singleViewObject.getViewAttribute().getType() == AttributeType.URL) {
				builder.addStatement("$N.setText($N.$N().getHref())", singleViewObject.getViewName(), resourceName, singleViewObject.getViewAttribute().getGetter());
			} else {
				builder.addStatement("$N.setText($N.$N())", singleViewObject.getViewName(), resourceName, singleViewObject.getViewAttribute().getGetter());
			}
		}

		return builder.build();
	}

	private  MethodSpec getGetResource() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("getResource");
		builder.addModifiers(Modifier.PUBLIC);
		builder.addAnnotation(Override.class);
		builder.returns(resourceClassName);
		builder.addStatement("$T $N = $N", boolean.class, "error", "false");

		for (SingleViewObject singleViewObject : appInputView.getViewAttributes()) {
			String stringName = singleViewObject.getViewAttribute().getLabel().toLowerCase();
			String viewName = singleViewObject.getViewName();
			builder.addStatement("$T $N = $N.getText()", String.class, stringName + "String", viewName);
			builder.beginControlFlow("if ($N.isEmpty())", stringName);
			builder.addStatement("$N.setError($N.getString($T.string.$N))", viewName, "context", rClass, stringName + "_missing");
			builder.addStatement("$N = $N", "error", "true");
			builder.endControlFlow();
		}

		builder.beginControlFlow("if (!$N)", "error");
		builder.addStatement("$T $N = new $T()", specificResourceClassName, "current", specificResourceClassName);
		for (SingleViewObject singleViewObject : appInputView.getViewAttributes()) {
			String stringName = singleViewObject.getViewAttribute().getLabel().toLowerCase() + "String";

			if (singleViewObject.getViewAttribute().getType() == AttributeType.URL) {
				builder.addStatement("$T $N = new $T($N, $S, $S)", linkClassName, singleViewObject.getViewName() + "Url", linkClassName, stringName, singleViewObject.getViewAttribute().getLabel().toLowerCase(), "text/html");
				builder.addStatement("$N.$N($N)", "current", singleViewObject.getViewAttribute().getSetter(), singleViewObject.getViewName() + "Url");
			} else {
				builder.addStatement("$N.$N($N)", "current", singleViewObject.getViewAttribute().getSetter(), stringName);
			}
		}
		builder.addStatement("return $N", "current");
		builder.endControlFlow();
		builder.addStatement("return null");


		return builder.build();
	}

	private MethodSpec getInitializeViews() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeViews");
		builder.addModifiers(Modifier.PUBLIC);
		builder.addAnnotation(Override.class);
		builder.returns(void.class);

		for (SingleViewObject singleViewObject : appInputView.getViewAttributes()) {
			String id = Character.toLowerCase(singleViewObject.getViewAttribute().getLabel().charAt(0)) + singleViewObject.getViewAttribute().getAttributeName().substring(1);
			builder.addStatement("$N = ($T) findViewById($T.id.$N)", singleViewObject.getViewName(), attributeInputClassName, rClass, id);
		}

		return builder.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClass, "view_" + appResource.getResourceName().toLowerCase() + "_input")
				.build();
	}

	private MethodSpec getGetStyleable() {
		return MethodSpec.methodBuilder("getStyleable").addModifiers(Modifier.PROTECTED).addAnnotation(Override.class).returns(int[].class)
				.addStatement("return $T.styleable.$N", rClass, appResource.getResourceName() + "InputView")
				.build();
	}
}
