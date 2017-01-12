package de.fhws.applab.gemara.welling.application.lib.specific.java.adapter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DetailAdapterGenerator extends AbstractModelClass {

	private final AppResource resource;

	private final ClassName specificResourceClassName;
	private final ClassName viewHolderClassName;
	private final ClassName viewGroupClassName;
	private final ClassName rClassName;
	private final ClassName specificDetailViewHolderClassName;


	private final FieldSpec listener;
	private final FieldSpec specificResource;

	public DetailAdapterGenerator(String packageName, AppResource appResource) {
		super(packageName + ".specific.adapter", appResource.getResourceName() + "DetailAdapter");
		this.resource = appResource;

		this.specificResourceClassName = ClassName.get(packageName + ".specific.model", appResource.getResourceName());
		this.viewHolderClassName = getViewHolderClassName();
		this.viewGroupClassName = getViewGroupClassName();
		this.rClassName = ClassName.get(packageName, "R");
		this.specificDetailViewHolderClassName = ClassName.get(packageName + ".specific.viewHolder", appResource.getResourceName() + "DetailViewHolder");

		this.listener = FieldSpec.builder(getViewOnClickListenerClassName(), "listener", Modifier.PRIVATE, Modifier.FINAL).build();
		this.specificResource = FieldSpec.builder(specificResourceClassName, appResource.getResourceName().toLowerCase(), Modifier.PRIVATE).build();
	}

	@Override
	public JavaFile javaFile() {
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

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec constructor() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getViewOnClickListenerClassName(), "listener")
				.addStatement("this.$N = $N", listener, "listener")
				.build();
	}

	private MethodSpec getAddResource() {
		ParameterSpec resourceParam = ParameterSpec.builder(specificResourceClassName, resource.getResourceName().toLowerCase()).build();
		String methodName = "add" + resource.getResourceName();
		return MethodSpec.methodBuilder(methodName)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(resourceParam)
				.addStatement("this.$N = $N", specificResource, resourceParam)
				.addStatement("notifyDataSetChanged()")
				.build();
	}

	private MethodSpec getOnCreateViewHolder() {
		return MethodSpec.methodBuilder("onCreateViewHolder")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(viewHolderClassName)
				.addParameter(viewGroupClassName, "parent")
				.addParameter(int.class, "viewType")
				.addStatement("$T $N = null", viewHolderClassName, "result")
				.beginControlFlow("if ($N == 0)", "viewType")
				.addStatement("$T $N = $T.layout.$N", int.class, "layout", rClassName, "card_" + resource.getResourceName().toLowerCase() + "_detail")
				.addStatement("$T $N = $T.from($N.getContext()).inflate($N, $N, false)", getViewClassName(), "v", getLayoutInflaterClassName(), "parent", "layout", "parent")
				.addStatement("$N = new $T($N, $N)", "result", specificDetailViewHolderClassName, "v", listener)
				.endControlFlow()
				.addStatement("return $N", "result")
				.build();
	}

	private MethodSpec getOnBindViewHolder() {
		return MethodSpec.methodBuilder("onBindViewHolder")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(getViewHolderClassName(), "holder")
				.addParameter(int.class, "position")
				.beginControlFlow("if ($N == 0)", "position")
				.addStatement("$T $N = ($T) $N", specificDetailViewHolderClassName, "viewHolder", specificDetailViewHolderClassName, "holder")
				.addStatement("$N.assignData($N)", "viewHolder", specificResource)
				.endControlFlow()
				.build();
	}

	private MethodSpec getGetItemCount() {
		return MethodSpec.methodBuilder("getItemCount")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(int.class)
				.addStatement("return 1")
				.build();
	}

	private MethodSpec getGetItemViewType() {
		return MethodSpec.methodBuilder("getItemViewType")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(int.class)
				.addParameter(int.class, "position")
				.addStatement("return $N", "position")
				.build();
	}

}
