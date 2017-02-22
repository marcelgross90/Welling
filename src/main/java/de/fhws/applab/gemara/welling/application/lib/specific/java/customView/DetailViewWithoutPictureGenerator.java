package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.Category;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModelExtension.AppDeclareStyleable;
import de.fhws.applab.gemara.welling.visitors.FieldVisitor;
import de.fhws.applab.gemara.welling.visitors.InitializeDetailViewVisitor;
import de.fhws.applab.gemara.welling.visitors.SetTextVisitor;

import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;

public class DetailViewWithoutPictureGenerator extends AbstractModelClass {

	private final DetailView detailView;
	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName resourceDetailViewClassName;
	private final ClassName specificResourceClassName;
	private final ClassName attributeViewClassName;

	private final ParameterSpec context = getContextParam();
	private final ParameterSpec attrs = ParameterSpec.builder(getAttributeSetClassName(), "attrs").build();
	private final ParameterSpec defStyleAttr = ParameterSpec.builder(int.class, "defStyleAttr").build();

	public DetailViewWithoutPictureGenerator(AppDescription appDescription, DetailView detailView) {
		super(appDescription.getLibPackageName() + ".specific.customView", detailView.getResourceName() + "DetailView");
		this.detailView = detailView;
		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getLibPackageName(), "R");
		this.attributeViewClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "AttributeView");
		this.specificResourceClassName = ClassName
				.get(appDescription.getLibPackageName() + ".specific.model", detailView.getResourceName());
		this.resourceDetailViewClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "ResourceDetailView");

		addDeclareStyleable();
	}

	private void addDeclareStyleable() {
		appDescription.setDeclareStyleables(detailView.getResourceName() + "DetailView",
				new AppDeclareStyleable.DeclareStyleable(detailView.getResourceName() + "DetailView"));
	}

	@Override
	public JavaFile javaFile() {

		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addModifiers(Modifier.PUBLIC);
		type.superclass(resourceDetailViewClassName);
		type.addFields(getFields());
		type.addMethod(constructorOne());
		type.addMethod(constructorTwo());
		type.addMethod(constructorThree());
		type.addMethod(getGetLayout());
		type.addMethod(getGetStyleable());
		type.addMethod(getInitializeView());
		type.addMethod(getSetUpView());
		type.addMethod(getConvertDate());

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private List<FieldSpec> getFields() {
		FieldVisitor fieldVisitor = new FieldVisitor(attributeViewClassName, null);
		List<FieldSpec> fieldSpecs = new ArrayList<>();
		for (Category category : detailView.getCategories()) {
			for (ResourceViewAttribute resourceViewAttribute : category.getResourceViewAttributes()) {
				resourceViewAttribute.accept(fieldVisitor);
				if (fieldVisitor.getFieldSpec() != null) {
					fieldSpecs.add(fieldVisitor.getFieldSpec());
				}
			}
		}
		return fieldSpecs;

	}

	private MethodSpec constructorOne() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addStatement("super($N)", context)
				.build();
		// @formatter:on
	}

	private MethodSpec constructorTwo() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addParameter(attrs)
				.addStatement("super($N, $N)", context, attrs)
				.build();
		// @formatter:on
	}

	private MethodSpec constructorThree() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addParameter(attrs)
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", context, attrs, defStyleAttr)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetLayout() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "view_" + detailView.getResourceName().toLowerCase() + "_detail")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetStyleable() {
		// @formatter:off
		return MethodSpec.methodBuilder("getStyleable")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(int[].class)
				.addStatement("return $T.styleable.$N", rClassName, detailView.getResourceName() + "DetailView")
				.build();
		// @formatter:on
	}

	private MethodSpec getInitializeView() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeView");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PROTECTED);
		builder.returns(void.class);

		for (Category category : detailView.getCategories()) {
			for (ResourceViewAttribute resourceViewAttribute : category.getResourceViewAttributes()) {
				InitializeDetailViewVisitor visitor = new InitializeDetailViewVisitor(builder, attributeViewClassName, rClassName);
				resourceViewAttribute.accept(visitor);
			}
		}

		return builder.build();
	}

	private MethodSpec getSetUpView() {
		String resourceName = detailView.getResourceName();
		MethodSpec.Builder builder = MethodSpec.methodBuilder("setUpView");
		builder.addParameter(specificResourceClassName, detailView.getResourceName().toLowerCase());
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(void.class);

		for (Category category : detailView.getCategories()) {
			for (ResourceViewAttribute resourceViewAttribute : category.getResourceViewAttributes()) {
				SetTextVisitor visitor = new SetTextVisitor(builder, rClassName, resourceName);
				resourceViewAttribute.accept(visitor);
			}
		}

		return builder.build();
	}

	private MethodSpec getConvertDate() {
		// @formatter:off
		return MethodSpec.methodBuilder("convertDate")
				.addModifiers(Modifier.PRIVATE)
				.returns(String.class)
				.addParameter(Date.class, "date")
				.addStatement("return new $T($S, $T.GERMANY).format($N)",
						SimpleDateFormat.class, "dd.MM.yyyy HH:mm", Locale.class, "date")
				.build();
		// @formatter:off
	}
}