package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.ViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.visitors.ViewObjectVisitorImpl;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.stream.Collectors;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getCardViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextClass;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTypedArrayClassName;

public class DetailCardViewGenerator extends AbstractModelClass {

	private final AppResource appResource;

	private final ClassName rClassName;
	private final ClassName specificResourceClassName;

	private final ViewObjectVisitorImpl visitor;

	private final ParameterSpec context = getContextParam();
	private final ParameterSpec attrs = ParameterSpec.builder(getAttributeSetClassName(), "attrs").build();
	private final ParameterSpec defStyleAttr = ParameterSpec.builder(int.class, "defStyleAttr").build();

	public DetailCardViewGenerator(String packageName, AppResource appResource) {
		super(packageName + ".specific.customView", appResource.getResourceName() + "DetailCardView");
		this.appResource = appResource;

		this.rClassName = ClassName.get(packageName, "R");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", appResource.getResourceName());
		ClassName attributeViewClassName = ClassName.get(packageName + ".generic.customView", "AttributeView");
		ClassName profileImageViewClassName = ClassName.get(packageName + ".generic.customView", "ProfileImageView");

		this.visitor = new ViewObjectVisitorImpl(profileImageViewClassName, rClassName, attributeViewClassName, appResource.getResourceName().toLowerCase());

	}

	@Override
	public JavaFile javaFile() {
		TypeSpec typeSpec = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getCardViewClassName())
				.addFields(getFields())
				.addMethod(constructorOne())
				.addMethod(constructorTwo())
				.addMethod(constructorThree())
				.addMethod(getInit())
				.addMethod(getSetUpView())
				.addMethod(getHideUnnecessaryViews())
				.build();
		return JavaFile.builder(this.packageName, typeSpec).build();
	}

	private List<FieldSpec> getFields() {
		return appResource.getAppDetailCardView().getViewAttributes().stream().map(viewObject -> viewObject.addField(visitor))
				.collect(Collectors.toList());

	}

	private MethodSpec constructorOne() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addStatement("super($N)", context)
				.addStatement("$N($N, $N, $N)", getInit(), context, "null", "0")
				.build();
	}

	private MethodSpec constructorTwo() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addParameter(attrs)
				.addStatement("super($N, $N)", context, attrs)
				.addStatement("$N($N, $N, $N)", getInit(), context, attrs, "0")
				.build();
	}

	private MethodSpec constructorThree() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addParameter(attrs)
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", context, attrs, defStyleAttr)
				.addStatement("$N($N, $N, $N)", getInit(), context, attrs, defStyleAttr)
				.build();
	}

	private MethodSpec getInit() {
		ParameterSpec context = getContextParam();
		ParameterSpec attrs = ParameterSpec.builder(getAttributeSetClassName(), "attributeSet").build();
		ParameterSpec defStyleAttr = ParameterSpec.builder(int.class, "defStyleAttr").build();

		MethodSpec.Builder builder = MethodSpec.methodBuilder("init");
		builder.addModifiers(Modifier.PRIVATE);
		builder.returns(void.class);
		builder.addParameter(context);
		builder.addParameter(attrs);
		builder.addParameter(defStyleAttr);

		builder.addStatement("$T $N = ($T) $N.getSystemService($T.LAYOUT_INFLATER_SERVICE)", getLayoutInflaterClassName(), "inflater", getLayoutInflaterClassName(), context, getContextClass());
		builder.addStatement("this.addView($N.inflate($T.layout.$N, this, false))", "inflater", rClassName, "view_" + appResource.getResourceName().toLowerCase() + "_detail_card");
		builder.addStatement("$T $N = $N.getTheme().obtainStyledAttributes($N, $T.styleable.$N, $N, 0)", getTypedArrayClassName(), "typedArray", context, attrs, rClassName, appResource.getResourceName() + "DetailCardView", defStyleAttr);
		builder.beginControlFlow("try");

		for (ViewObject viewObject : appResource.getAppDetailCardView().getViewAttributes()) {
			viewObject.addInitializeDetailViewStatements(builder, visitor);
		}

		builder.endControlFlow();
		builder.beginControlFlow("finally");
		builder.addStatement("$N.recycle()", "typedArray");
		builder.endControlFlow();

		return builder.build();
	}

	private MethodSpec getSetUpView() {
		ParameterSpec specificResource = ParameterSpec.builder(specificResourceClassName, appResource.getResourceName().toLowerCase()).build();

		MethodSpec.Builder builder = MethodSpec.methodBuilder("setUpView");
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(void.class);
		builder.addParameter(specificResource);
		builder.addStatement("$N($N)", getHideUnnecessaryViews(), specificResource);

		for (ViewObject viewObject : appResource.getAppDetailCardView().getViewAttributes()) {
			viewObject.addFillResourceStatements(builder, visitor);
		}

		return builder.build();
	}

	private MethodSpec getHideUnnecessaryViews() {
		String specificResourceName = appResource.getResourceName().toLowerCase();
		MethodSpec.Builder method = MethodSpec.methodBuilder("hideUnnecessaryViews");
		method.addModifiers(Modifier.PRIVATE);
		method.returns(void.class);
		method.addParameter(specificResourceClassName, specificResourceName);

		for (ViewObject viewObject : appResource.getAppDetailCardView().getViewAttributes()) {
			viewObject.addHideUnnecessaryViewStatements(method, visitor);
		}

		return method.build();
	}
}
