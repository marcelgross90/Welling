package de.fhws.applab.gemara.welling.application.lib.generic.java.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class ScrollListener extends AbstractModelClass {

	private final ClassName thisOnScrollListenerInterfaceClassName;
	private final ClassName linearLayoutManagerClassName = getLinearLayoutManagerClassName();
	private final ClassName onScrollListenerClassName = getOnScrollListenerClassName();
	private final ClassName recyclerViewClassName = getRecyclerViewClassName();

	private final FieldSpec offsetField = FieldSpec.builder(int.class, "offset")
			.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "FieldCanBeLocal").build())
			.addModifiers(Modifier.PRIVATE, Modifier.FINAL)
			.initializer("$L", 1)
			.build();

	private final FieldSpec linearLayoutManagerField = FieldSpec.builder(linearLayoutManagerClassName, "linearLayoutManager")
			.addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	private final FieldSpec listenerField;

	public ScrollListener(String packageName, String className) {
		super(packageName + ".generic.util", className);
		thisOnScrollListenerInterfaceClassName = ClassName.get(this.packageName, "OnScrollListener");
		listenerField = FieldSpec.builder(thisOnScrollListenerInterfaceClassName, "listener")
				.addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	}

	public JavaFile javaFile() {

		TypeSpec onScrollListener = TypeSpec.interfaceBuilder(thisOnScrollListenerInterfaceClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(MethodSpec.methodBuilder("load")
						.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
						.build())
				.build();

		MethodSpec constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(linearLayoutManagerClassName, "linearLayoutManager")
				.addParameter(thisOnScrollListenerInterfaceClassName, "listener")
				.addStatement("this.$N = linearLayoutManager", linearLayoutManagerField)
				.addStatement("this.$N = listener", listenerField)
				.build();

		MethodSpec onScrolled = MethodSpec.methodBuilder("onScrolled")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(ParameterSpec.builder(recyclerViewClassName, "recyclerView").build())
				.addParameter(ParameterSpec.builder(int.class, "dx").build())
				.addParameter(ParameterSpec.builder(int.class, "dy").build())
				.addStatement("super.onScrolled(recyclerView, dx, dy)")
				.addStatement("$T visibleItems = recyclerView.getChildCount()", int.class)
				.addStatement("$T totalItems = linearLayoutManager.getItemCount()", int.class)
				.addStatement("$T firstVisible = linearLayoutManager.findFirstVisibleItemPosition()", int.class)
				.beginControlFlow("if (totalItems - visibleItems <= firstVisible + $N)", offsetField)
				.addStatement("$N.load()", listenerField)
				.endControlFlow()
				.build();

		TypeSpec tpye = TypeSpec.classBuilder(this.className)
				.superclass(onScrollListenerClassName)
				.addModifiers(Modifier.PUBLIC)
				.addType(onScrollListener)
				.addField(offsetField)
				.addField(linearLayoutManagerField)
				.addField(listenerField)
				.addMethod(constructor)
				.addMethod(onScrolled)
				.build();

		return JavaFile.builder(this.packageName, tpye).build();
	}


}
