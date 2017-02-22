package de.fhws.applab.gemara.welling.application.lib.specific.java.adapter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAdapterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewGroupClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewHolderClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewOnClickListenerClassName;

public class DetailAdapterGenerator extends AbstractModelClass {

	private final DetailView detailView;

	private final ClassName specificResourceClassName;
	private final ClassName viewHolderClassName;
	private final ClassName viewGroupClassName;
	private final ClassName rClassName;
	private final ClassName specificDetailViewHolderClassName;

	private final FieldSpec listener;
	private final FieldSpec specificResource;

	public DetailAdapterGenerator(String packageName, DetailView detailView) {
		super(packageName + ".specific.adapter", detailView.getResourceName() + "DetailAdapter");
		this.detailView = detailView;

		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", detailView.getResourceName());
		this.viewHolderClassName = getViewHolderClassName();
		this.viewGroupClassName = getViewGroupClassName();
		this.rClassName = ClassName.get(packageName, "R");
		this.specificDetailViewHolderClassName = ClassName
				.get(packageName + ".specific.viewholder", detailView.getResourceName() + "DetailViewHolder");

		this.listener = FieldSpec.builder(getViewOnClickListenerClassName(), "listener", Modifier.PRIVATE, Modifier.FINAL).build();
		this.specificResource = FieldSpec.builder(specificResourceClassName, detailView.getResourceName().toLowerCase(), Modifier.PRIVATE)
				.build();
	}

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(getAdapterClassName())
				.addModifiers(Modifier.PUBLIC)
				.addField(listener)
				.addField(specificResource)
				.addMethod(constructor())
				.addMethod(getAddResource())
				.addMethod(getOnCreateViewHolder())
				.addMethod(getOnBindViewHolder())
				.addMethod(getGetItemCount())
				.addMethod(getGetItemViewType())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec constructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getViewOnClickListenerClassName(), "listener")
				.addStatement("this.$N = $N", listener, "listener")
				.build();
		// @formatter:on
	}

	private MethodSpec getAddResource() {
		ParameterSpec resourceParam = ParameterSpec.builder(specificResourceClassName, detailView.getResourceName().toLowerCase()).build();
		String methodName = "add" + detailView.getResourceName();
		// @formatter: off
		return MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC).returns(void.class).addParameter(resourceParam)
				.addStatement("this.$N = $N", specificResource, resourceParam).addStatement("notifyDataSetChanged()").build();
		// @formatter:on
	}

	private MethodSpec getOnCreateViewHolder() {
		// @formatter:off
		return MethodSpec.methodBuilder("onCreateViewHolder")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(viewHolderClassName)
				.addParameter(viewGroupClassName, "parent")
				.addParameter(int.class, "viewType")
				.addStatement("$T $N = null", viewHolderClassName, "result")
				.beginControlFlow("if ($N == 0)", "viewType")
				.addStatement("$T $N = $T.layout.$N",
						int.class, "layout", rClassName, "card_" + detailView.getResourceName().toLowerCase() + "_detail")
				.addStatement("$T $N = $T.from($N.getContext()).inflate($N, $N, false)",
						getViewClassName(), "v", getLayoutInflaterClassName(), "parent", "layout", "parent")
				.addStatement("$N = new $T($N, $N)", "result", specificDetailViewHolderClassName, "v", listener)
				.endControlFlow()
				.addStatement("return $N", "result")
				.build();
		// @formatter:on
	}

	private MethodSpec getOnBindViewHolder() {
		// @formatter:off
		return MethodSpec.methodBuilder("onBindViewHolder")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(getViewHolderClassName(), "holder")
				.addParameter(int.class, "position")
				.beginControlFlow("if ($N == 0)", "position")
				.addStatement("$T $N = ($T) $N",
						specificDetailViewHolderClassName, "viewHolder", specificDetailViewHolderClassName, "holder")
				.addStatement("$N.assignData($N)", "viewHolder", specificResource)
				.endControlFlow()
				.build();
		// @formatter:on
	}

	private MethodSpec getGetItemCount() {
		// @formatter:off
		return MethodSpec.methodBuilder("getItemCount")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(int.class)
				.addStatement("return 1")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetItemViewType() {
		// @formatter:off
		return MethodSpec.methodBuilder("getItemViewType")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(int.class)
				.addParameter(int.class, "position")
				.addStatement("return $N", "position")
				.build();
		// @formatter:on
	}
}