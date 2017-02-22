package de.fhws.applab.gemara.welling.application.lib.specific.java.adapter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;

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
		this.onResourceClickListenerClassName = ClassName
				.get(packageName + ".generic.adapter.ResourceListAdapter", "OnResourceClickListener");

		this.resourceName = resourceName;
	}

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(resourceName + "ListAdapter")
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(resourceListAdapterClassName, listViewHolderClassName))
				.addMethod(getConstructor())
				.addMethod(getGetViewHolder())
				.addMethod(getGetLayout())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getConstructor() {
		ParameterSpec onResourceClickListener = ParameterSpec.builder(onResourceClickListenerClassName, "onResourceClickListener").build();

		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(onResourceClickListener)
				.addStatement("super($N)", onResourceClickListener)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetViewHolder() {
		ParameterSpec resourceCard = ParameterSpec.builder(getViewClassName(), "resourceCard").build();
		ParameterSpec onResourceClickListener = ParameterSpec.builder(onResourceClickListenerClassName, "onResourceClickListener").build();

		// @formatter:off
		return MethodSpec.methodBuilder("getViewHolder")
				.addModifiers(Modifier.PROTECTED).returns(listViewHolderClassName)
				.addParameter(resourceCard)
				.addParameter(onResourceClickListener)
				.addStatement("return new $T($N, $N)", listViewHolderClassName, resourceCard, onResourceClickListener)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetLayout() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addAnnotation(Override.class)
				.addStatement("return $T.layout.$N", rClassName, "card_" + resourceName.toLowerCase())
				.build();
		// @formatter:on
	}
}