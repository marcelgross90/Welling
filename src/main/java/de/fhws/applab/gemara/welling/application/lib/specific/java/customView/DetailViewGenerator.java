package de.fhws.applab.gemara.welling.application.lib.specific.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.application.util.GetterSetterGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DetailViewGenerator extends AbstractModelClass {

	private final DetailView detailView;
	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName recyclerViewClassName;
	private final ClassName profileImageViewClassName;
	private final ClassName specificResourceClassName;
	private final ClassName resourceDetailViewClassName;
	private final ClassName specificResourceDetailAdapterClassName;

	private final FieldSpec profileImageView;
	private final FieldSpec recyclerView;

	private final ParameterSpec context = getContextParam();
	private final ParameterSpec attrs = ParameterSpec.builder(getAttributeSetClassName(), "attrs").build();
	private final ParameterSpec defStyleAttr = ParameterSpec.builder(int.class, "defStyleAttr").build();

	public DetailViewGenerator(AppDescription appDescription, DetailView detailView) {
		super(appDescription.getLibPackageName() + ".specific.customView", detailView.getResourceName() + "DetailView");
		this.detailView = detailView;
		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getLibPackageName(), "R");
		this.recyclerViewClassName = getRecyclerViewClassName();
		this.profileImageViewClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "ProfileImageView");
		this.specificResourceClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.model", detailView.getResourceName());
		this.resourceDetailViewClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.customView", "ResourceDetailView");
		this.specificResourceDetailAdapterClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.adapter", detailView.getResourceName() + "DetailAdapter");

		this.profileImageView = FieldSpec.builder(profileImageViewClassName, "profileImageView", Modifier.PRIVATE).build();
		this.recyclerView = FieldSpec.builder(recyclerViewClassName, "recyclerView", Modifier.PRIVATE).build();

		addDeclareStyleable();
	}

	public void addDeclareStyleable() {
		appDescription.setDeclareStyleables(new AppDeclareStyleable.DeclareStyleable(detailView.getResourceName() + "DetailView"));
	}

	@Override
	public JavaFile javaFile() {

		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addModifiers(Modifier.PUBLIC);
		type.superclass(resourceDetailViewClassName);
		type.addField(recyclerView);
		if (detailView.getImage() != null) {
			type.addField(profileImageView);
		}

		type.addMethod(constructorOne());
		type.addMethod(constructorTwo());
		type.addMethod(constructorThree());
		type.addMethod(getGetLayout());
		type.addMethod(getGetStyleable());
		type.addMethod(getInitializeView());
		type.addMethod(getSetUpView());

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private MethodSpec constructorOne() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addStatement("super($N)", context)
				.build();
	}

	private MethodSpec constructorTwo() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addParameter(attrs)
				.addStatement("super($N, $N)", context, attrs)
				.build();
	}

	private MethodSpec constructorThree() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(context)
				.addParameter(attrs)
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", context, attrs, defStyleAttr)
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "view_" + detailView.getResourceName().toLowerCase() + "_detail")
				.build();
	}

	private MethodSpec getGetStyleable() {
		return MethodSpec.methodBuilder("getStyleable")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(int[].class)
				.addStatement("return $T.styleable.$N", rClassName, detailView.getResourceName() + "DetailView")

				.build();
	}

	private MethodSpec getInitializeView() {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeView");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PROTECTED);
		builder.returns(void.class);

		if (detailView.getImage() != null) {
			builder.addStatement("$N = ($T) findViewById($T.id.$N)", profileImageView, profileImageViewClassName, rClassName, "ivLecturerPicture");
		}

		builder.addStatement("$N = ($T) findViewById($T.id.$N)", recyclerView, recyclerViewClassName, rClassName, "rv" + detailView.getResourceName() + "Details");
		return builder.build();
	}

	private MethodSpec getSetUpView() {
		String resourceName = detailView.getResourceName();
		MethodSpec.Builder builder = MethodSpec.methodBuilder("setUpView");
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(void.class);
		builder.addParameter(specificResourceClassName, resourceName.toLowerCase());
		builder.addParameter(getViewOnClickListenerClassName(), "listener");
		builder.addStatement("$N.setLayoutManager(new $T($N))", recyclerView, getLinearLayoutManagerClassName(), context);
		builder.addStatement("$N.setHasFixedSize(true)", recyclerView);

		builder.addStatement("$T $N = new $T($N)", specificResourceDetailAdapterClassName, "adapter", specificResourceDetailAdapterClassName, "listener");
		builder.addStatement("$N.$N($N)", "adapter", "add" + resourceName, resourceName.toLowerCase());
		if (detailView.getImage() != null) {
			builder.beginControlFlow("if ($N.$N() != null)", resourceName.toLowerCase(), getImageViewGetter());
			builder.addStatement("$N.loadCutImage($N.$N())", profileImageView, resourceName.toLowerCase(), getImageViewGetter());
			builder.endControlFlow();
		}

		builder.addStatement("$N.setAdapter($N)", recyclerView, "adapter");
		return builder.build();
	}


	private String getImageViewGetter() {
		return GetterSetterGenerator.getGetter(detailView.getImage().getDisplayViewAttribute().getAttributeName());
	}
}
