package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.visitors.ContainsDateVisitor;
import de.fhws.applab.gemara.welling.visitors.FieldVisitor;
import de.fhws.applab.gemara.welling.visitors.HideViewsVisitor;
import de.fhws.applab.gemara.welling.visitors.InitializeViewVisitor;
import de.fhws.applab.gemara.welling.visitors.SetTextVisitor;

import javax.lang.model.element.Modifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addFields(getFields());
		type.superclass(resourceCardViewClassName);
		type.addModifiers(Modifier.PUBLIC);
		type.addMethod(getConstructorOne());
		type.addMethod(getConstructorTwo());
		type.addMethod(getConstructorThree());
		type.addMethod(getGetStyleable());
		type.addMethod(getGetLayout());
		type.addMethod(getInitializeView());
		type.addMethod(getSetupView());
		type.addMethod(getHideUnnecessaryViews());

		ContainsDateVisitor visitor = new ContainsDateVisitor();
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			if (visitor.isContainsDate()) {
				type.addMethod(getConvertDate());
			}
		}

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private List<FieldSpec> getFields() {
		List<FieldSpec> fieldSpecs = new ArrayList<>();
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			if (visitor.getFieldSpec() != null) {
				fieldSpecs.add(visitor.getFieldSpec());
			}
		}
		return fieldSpecs;
	}

	private MethodSpec getConstructorOne() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addStatement("super($N)", getContextParam())
				.build();
	}

	private MethodSpec getConstructorTwo() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.build();
	}

	private MethodSpec getConstructorThree() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
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

	private MethodSpec getConvertDate() {
		return MethodSpec.methodBuilder("convertDate")
				.addModifiers(Modifier.PRIVATE)
				.returns(String.class)
				.addParameter(Date.class, "date")
				.addStatement("$T $N = new $T($S, $T.GERMANY)", SimpleDateFormat.class, "formatter", SimpleDateFormat.class, "dd.MM.yyyy HH:mm",
						Locale.class)
				.addStatement("return $N.format($N)", "formatter", "date").build();
	}
}
