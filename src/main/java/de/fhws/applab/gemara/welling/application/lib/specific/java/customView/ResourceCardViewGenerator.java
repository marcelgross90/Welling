package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.visitors.cardView.FieldVisitor;
import de.fhws.applab.gemara.welling.visitors.cardView.HideViewsVisitor;
import de.fhws.applab.gemara.welling.visitors.cardView.InitializeViewVisitor;
import de.fhws.applab.gemara.welling.visitors.cardView.SetTextVisitor;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class ResourceCardViewGenerator extends AbstractModelClass {

	private final CardView cardView;
	private final FieldVisitor visitor;

	private final ClassName rClassName;
	private final ClassName resourceClassName;
	private final ClassName specificResourceClassName;
	private final ClassName resourceCardViewClassName;
	private final ClassName attributeViewClassName;
	private final ClassName profileImageViewClassName;

	public ResourceCardViewGenerator(String packageName, CardView cardView) {
		super(packageName + ".specific.customView", cardView.getResourceName() + "CardView");
		this.cardView = cardView;

		this.rClassName = ClassName.get(packageName, "R");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", cardView.getResourceName());
		this.resourceCardViewClassName = ClassName.get(packageName + ".generic.customView", "ResourceCardView");
		this.attributeViewClassName = ClassName.get(packageName + ".generic.customView", "AttributeView");
		this.profileImageViewClassName = ClassName.get(packageName + ".generic.customView", "ProfileImageView");

		this.visitor = new FieldVisitor(attributeViewClassName, profileImageViewClassName);
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
		List<FieldSpec> fieldSpecs = new ArrayList<>();
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			fieldSpecs.add(visitor.getFieldSpec());
		}
		return fieldSpecs;
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
				.addStatement("return $T.layout.$N", rClassName, "view_" + cardView.getResourceName().toLowerCase() + "_card")
				.build();
	}

	private MethodSpec getInitializeView() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("initializeView");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PROTECTED);
		method.returns(void.class);

		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			InitializeViewVisitor visitor = new InitializeViewVisitor(method, attributeViewClassName, profileImageViewClassName, rClassName);
			resourceViewAttribute.accept(visitor);

		}

		return method.build();
	}

	private MethodSpec getSetupView() {
		String specificResourceName = cardView.getResourceName().toLowerCase();
		MethodSpec.Builder method = MethodSpec.methodBuilder("setUpView");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addParameter(resourceClassName, "resource");
		method.addStatement("$T $N = ($T) $N", specificResourceClassName, specificResourceName, specificResourceClassName, "resource");
		method.addStatement("$N($N)", getHideUnnecessaryViews(), specificResourceName);

		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			SetTextVisitor visitor = new SetTextVisitor(method, rClassName, specificResourceName);
			resourceViewAttribute.accept(visitor);
		}

		return method.build();
	}

	private MethodSpec getHideUnnecessaryViews() {
		String specificResourceName = cardView.getResourceName().toLowerCase();
		MethodSpec.Builder method = MethodSpec.methodBuilder("hideUnnecessaryViews");
		method.addModifiers(Modifier.PRIVATE);
		method.returns(void.class);
		method.addParameter(specificResourceClassName, specificResourceName);

		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			HideViewsVisitor visitor = new HideViewsVisitor(method, specificResourceName);
			resourceViewAttribute.accept(visitor);
		}

		return method.build();
	}
}
