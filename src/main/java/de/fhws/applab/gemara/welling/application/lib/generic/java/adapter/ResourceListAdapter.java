package de.fhws.applab.gemara.welling.application.lib.generic.java.adapter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getAdapterClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getViewGroupClassName;

public class ResourceListAdapter extends AbstractModelClass {

	private final ClassName thisClass;
	private final ClassName thisOnResourceClickListener;
	private final ClassName adapterClassName = getAdapterClassName();
	private final ClassName resourceClassName;
	private final ClassName resourceViewHolderClassName;

	private final ParameterizedTypeName resourceListType;

	private final FieldSpec onResourceClickListener;
	private final FieldSpec resourceList;

	public ResourceListAdapter(String packageName) {
		super(packageName + ".generic.adapter", "ResourceListAdapter");
		this.thisClass = ClassName.get(this.packageName, this.className);
		this.thisOnResourceClickListener = ClassName.get(this.packageName + "." + this.className, "OnResourceClickListener");
		this.resourceViewHolderClassName = ClassName.get(packageName + ".generic.viewholder", "ResourceViewHolder");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.resourceListType = ParameterizedTypeName.get(ClassName.get(List.class), this.resourceClassName);
		this.onResourceClickListener = FieldSpec.builder(thisOnResourceClickListener, "onResourceClickListener")
				.addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
		this.resourceList = FieldSpec.builder(this.resourceListType, "resourceList").addModifiers(Modifier.PRIVATE, Modifier.FINAL)
				.initializer("new $T()", ParameterizedTypeName.get(ClassName.get(ArrayList.class), this.resourceClassName)).build();

	}

	@Override
	public JavaFile javaFile() {

		MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(thisOnResourceClickListener, "onResourceClickListener").build())
				.addStatement("this.$N = onResourceClickListener", onResourceClickListener).build();

		MethodSpec addResource = MethodSpec.methodBuilder("addResource").addModifiers(Modifier.PUBLIC).returns(void.class)
				.addParameter(ParameterSpec.builder(resourceListType, "resources").build())
				.beginControlFlow("for ($T resource : resources)", resourceClassName)
				.beginControlFlow("if (!this.$N.contains(resource))", resourceList).addStatement("this.$N.add(resource)", resourceList)
				.endControlFlow().endControlFlow().addStatement("notifyDataSetChanged()").build();

		MethodSpec getLayout = MethodSpec.methodBuilder("getLayout").addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED).returns(int.class)
				.build();

		MethodSpec getViewHolder = MethodSpec.methodBuilder("getViewHolder").addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)
				.returns(TypeVariableName.get("T")).addParameter(ParameterSpec.builder(getViewClassName(), "moduleCard").build())
				.addParameter(ParameterSpec.builder(thisOnResourceClickListener, "onResourceClickListener").build()).build();

		MethodSpec onCreateViewHolder = MethodSpec.methodBuilder("onCreateViewHolder").addModifiers(Modifier.PUBLIC)
				.returns(TypeVariableName.get("T")).addAnnotation(Override.class)
				.addParameter(ParameterSpec.builder(getViewGroupClassName(), "parent").build())
				.addParameter(ParameterSpec.builder(int.class, "viewType").build())
				.addStatement("$T moduleCard = $T.from(parent.getContext()).inflate($N(), parent, false)", getViewClassName(),
						getLayoutInflaterClassName(), getLayout)
				.addStatement("return $N(moduleCard, $N)", getViewHolder, onResourceClickListener).build();

		MethodSpec onBindViewHolder = MethodSpec.methodBuilder("onBindViewHolder").addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class).addParameter(TypeVariableName.get("T"), "holder").addParameter(int.class, "position")
				.addStatement("holder.assignData($N.get(position))", resourceList).build();

		MethodSpec getItemCount = MethodSpec.methodBuilder("getItemCount").addModifiers(Modifier.PUBLIC).returns(int.class)
				.addAnnotation(Override.class).addStatement("return $N.size()", resourceList).build();

		TypeSpec type = TypeSpec.classBuilder(thisClass).superclass(ParameterizedTypeName.get(adapterClassName, TypeVariableName.get("T")))
				.addTypeVariable(TypeVariableName.get("T", resourceViewHolderClassName)).addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
				.addField(this.onResourceClickListener).addField(this.resourceList).addType(generateInterface()).addMethod(getLayout)
				.addMethod(getViewHolder).addMethod(constructor).addMethod(addResource).addMethod(onCreateViewHolder)
				.addMethod(onBindViewHolder).addMethod(getItemCount).build();

		return JavaFile.builder(thisClass.packageName(), type).build();
	}

	private TypeSpec generateInterface() {
		MethodSpec onResourceClickWithView = MethodSpec.methodBuilder("onResourceClickWithView")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(void.class)
				.addParameter(ParameterSpec.builder(resourceClassName, "resource").build())
				.addParameter(ParameterSpec.builder(getViewClassName(), "view").build()).build();

		MethodSpec onResourceClick = MethodSpec.methodBuilder("onResourceClick").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.returns(void.class).addParameter(ParameterSpec.builder(resourceClassName, "resource").build()).build();

		return TypeSpec.interfaceBuilder(thisOnResourceClickListener).addModifiers(Modifier.PUBLIC).addMethod(onResourceClickWithView)
				.addMethod(onResourceClick).build();

	}
}
