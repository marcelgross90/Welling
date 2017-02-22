package de.fhws.applab.gemara.welling.application.lib.generic.java.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLinearLayoutManagerClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getOnScrollListenerClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getRecyclerViewClassName;

public class ScrollListener extends AbstractModelClass {

	private final ClassName thisOnScrollListenerInterfaceClassName;
	private final ClassName linearLayoutManagerClassName = getLinearLayoutManagerClassName();
	private final ClassName onScrollListenerClassName = getOnScrollListenerClassName();
	private final ClassName recyclerViewClassName = getRecyclerViewClassName();

	private final FieldSpec offsetField;
	private final FieldSpec linearLayoutManagerField;
	private final FieldSpec listenerField;

	public ScrollListener(String packageName) {
		super(packageName + ".generic.util", "ScrollListener");

		// @formatter:off
		thisOnScrollListenerInterfaceClassName = ClassName.get(this.packageName + "." + this.className, "OnScrollListener");

		offsetField = FieldSpec.builder(int.class, "offset")
				.addAnnotation(
						AnnotationSpec.builder(SuppressWarnings.class)
								.addMember("value", "$S", "FieldCanBeLocal")
								.build())
				.addModifiers(Modifier.PRIVATE, Modifier.FINAL)
				.initializer("$L", 1)
				.build();

		linearLayoutManagerField = FieldSpec.builder(linearLayoutManagerClassName, "linearLayoutManager")
				.addModifiers(Modifier.PRIVATE, Modifier.FINAL)
				.build();

		listenerField = FieldSpec.builder(thisOnScrollListenerInterfaceClassName, "listener")
				.addModifiers(Modifier.PRIVATE, Modifier.FINAL)
				.build();
		// @formatter:on
	}

	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(onScrollListenerClassName)
				.addModifiers(Modifier.PUBLIC)
				.addType(getOnScrollListener())
				.addField(offsetField)
				.addField(linearLayoutManagerField)
				.addField(listenerField)
				.addMethod(getConstructor())
				.addMethod(getOnScrolled())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getOnScrolled() {
		// @formatter:off
		return MethodSpec.methodBuilder("onScrolled")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addAnnotation(Override.class)
					.addParameter(recyclerViewClassName, "recyclerView")
					.addParameter(int.class, "dx")
					.addParameter(int.class, "dy")
					.addStatement("super.onScrolled($N, $N, $N)", "recyclerView", "dx", "dy")
					.addStatement("$T $N = $N.getChildCount()", int.class, "visibleItems", "recyclerView")
					.addStatement("$T $N = $N.getItemCount()", int.class, "totalItems", "linearLayoutManager")
					.addStatement("$T $N = $N.findFirstVisibleItemPosition()", int.class, "firstVisible", "linearLayoutManager")
					.beginControlFlow("if ($N - $N <= $N + $N)", "totalItems", "visibleItems", "firstVisible", offsetField)
					.addStatement("$N.load()", listenerField)
					.endControlFlow()
					.build();
		// @formatter:on
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PUBLIC)
					.addParameter(linearLayoutManagerClassName, "linearLayoutManager")
					.addParameter(thisOnScrollListenerInterfaceClassName, "listener")
					.addStatement("this.$N = $N", linearLayoutManagerField, "linearLayoutManager")
					.addStatement("this.$N = $N", listenerField, "listener")
					.build();
		// @formatter:on
	}

	private TypeSpec getOnScrollListener() {
		// @formatter:off
		return TypeSpec.interfaceBuilder(thisOnScrollListenerInterfaceClassName)
					.addModifiers(Modifier.PUBLIC)
					.addMethod(
							MethodSpec.methodBuilder("load")
									.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
									.build())
					.build();
		// @formatter:on
	}
}