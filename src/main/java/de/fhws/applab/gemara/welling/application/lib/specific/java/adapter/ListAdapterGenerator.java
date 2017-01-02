package de.fhws.applab.gemara.welling.application.lib.specific.java.adapter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class ListAdapterGenerator extends AbstractModelClass {

	private final String resourceName;
	private final ClassName rClassName;
	private final ClassName listViewHolderClassName;
	private final ClassName resourceListAdapterClassName;
	private final ClassName onResourceClickListenerClassName;

	public ListAdapterGenerator(String packageName, String resourceName) {
		super(packageName + ".specific.adapter", resourceName + "ListAdapter");
		this.rClassName = ClassName.get(packageName, "R");
		this.listViewHolderClassName = ClassName.get(packageName + ".specific.viewholder", resourceName + "ListViewHolder");
		this.resourceListAdapterClassName = ClassName.get(packageName + ".generic.adapter", "ResourceListAdapter");
		this.onResourceClickListenerClassName = ClassName.get(packageName + ".generic.adapter.ResourceListAdapter", "OnResourceClickListener");

		this.resourceName = resourceName;
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(resourceName + "ListAdapter")
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(resourceListAdapterClassName, listViewHolderClassName))
				.addMethod(getConstructor())
				.addMethod(getGetViewHolder())
				.addMethod(getGetLayout())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getConstructor() {
		ParameterSpec onResourceClickListener = ParameterSpec.builder(onResourceClickListenerClassName, "onResourceClickListener").build();
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(onResourceClickListener)
				.addStatement("super($N)", onResourceClickListener)
				.build();
	}

	private MethodSpec getGetViewHolder() {
		ParameterSpec resourceCard = ParameterSpec.builder(getViewClassName(), "resourceCard").build();
		ParameterSpec onResourceClickListener = ParameterSpec.builder(onResourceClickListenerClassName, "onResourceClickListener").build();
		return MethodSpec.methodBuilder("getViewHolder")
				.addModifiers(Modifier.PROTECTED).returns(listViewHolderClassName)
				.addParameter(resourceCard)
				.addParameter(onResourceClickListener)
				.addStatement("return new $T($N, $N)", listViewHolderClassName, resourceCard, onResourceClickListener)
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED).returns(int.class)
				.addAnnotation(Override.class)
				.addStatement("return $T.layout.$N", rClassName, "card_" + resourceName.toLowerCase())
				.build();
	}

}
