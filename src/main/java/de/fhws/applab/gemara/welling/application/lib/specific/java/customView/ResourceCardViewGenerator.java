package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.cardViews.AppCardView;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.ViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.visitors.ViewObjectVisitorImpl;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.stream.Collectors;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class ResourceCardViewGenerator extends AbstractModelClass {

	private final AppCardView cardView;
	private final AppResource appResource;
	private final ViewObjectVisitorImpl visitor;

	private final ClassName rClassName;
	private final ClassName resourceClassName;
	private final ClassName specificResourceClassName;
	private final ClassName resourceCardViewClassName;

	public ResourceCardViewGenerator(String packageName, AppResource resource) {
		super(packageName + ".specific.customView", resource.getResourceName() + "CardView");
		this.cardView = resource.getAppCardView();
		this.appResource = resource;

		this.rClassName = ClassName.get(packageName, "R");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", resource.getResourceName());
		this.resourceCardViewClassName = ClassName.get(packageName + ".generic.customView", "ResourceCardView");
		ClassName attributeViewClassName = ClassName.get(packageName + ".generic.customView", "AttributeView");
		ClassName profileImageViewClassName = ClassName.get(packageName + ".generic.customView", "ProfileImageView");

		this.visitor = new ViewObjectVisitorImpl(profileImageViewClassName, rClassName, attributeViewClassName, appResource.getResourceName().toLowerCase());
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addFields(getFields())
				.superclass(resourceCardViewClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getConstructorOne())
				.addMethod(getConstructorTwo())
				.addMethod(getConstructorThree())
				.addMethod(getGetStyleable())
				.addMethod(getGetLayout())
				.addMethod(getInitializeView())
				.addMethod(getSetupView())
				.addMethod(getHideUnnecessaryViews())
				.build();
		return JavaFile.builder(this.packageName, type).build();
	}

	private List<FieldSpec> getFields() {
		return cardView.getViewAttributes().stream().map(viewObject -> viewObject.addField(visitor))
				.collect(Collectors.toList());

	}

	private MethodSpec getConstructorOne() {
		return MethodSpec.constructorBuilder()
				.addParameter(getContextParam())
				.addStatement("super($N)", getContextParam())
				.build();
	}

	private MethodSpec getConstructorTwo() {
		return MethodSpec.constructorBuilder()
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.build();
	}

	private MethodSpec getConstructorThree() {
		return MethodSpec.constructorBuilder()
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(int.class, "defStyleAttr")
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), "defStyleAttr")
				.build();
	}

	private MethodSpec getGetStyleable() {
		return MethodSpec.methodBuilder("getStyleable")
				.addModifiers(Modifier.PROTECTED)
				.returns(int[].class)
				.addAnnotation(Override.class)
				.addStatement("return $T.styleable.$N", rClassName, this.className)
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "view_" + appResource.getResourceName().toLowerCase() + "_card")
				.build();
	}

	private MethodSpec getInitializeView() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("initializeView");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PROTECTED);
		method.returns(void.class);


		for (ViewObject viewObject : cardView.getViewAttributes()) {
			viewObject.addInitializeViewStatements(method, visitor);

		}

		return method.build();
	}



	private MethodSpec getSetupView() {
		String specificResourceName = appResource.getResourceName().toLowerCase();
		MethodSpec.Builder method = MethodSpec.methodBuilder("setUpView");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addParameter(resourceClassName, "resource");
		method.addStatement("$T $N = ($T) $N", specificResourceClassName, specificResourceName, specificResourceClassName, "resource");
		method.addStatement("$N($N)", getHideUnnecessaryViews(), specificResourceName);

		for (ViewObject viewObject : cardView.getViewAttributes()) {
			viewObject.addFillResourceStatements(method, visitor);
		}

		return method.build();
	}

	private MethodSpec getHideUnnecessaryViews() {
		String specificResourceName = appResource.getResourceName().toLowerCase();
		MethodSpec.Builder method = MethodSpec.methodBuilder("hideUnnecessaryViews");
		method.addModifiers(Modifier.PRIVATE);
		method.returns(void.class);
		method.addParameter(specificResourceClassName, specificResourceName);

		for (ViewObject viewObject : cardView.getViewAttributes()) {
			viewObject.addHideUnnecessaryViewStatements(method, visitor);
		}

		return method.build();
	}
}
