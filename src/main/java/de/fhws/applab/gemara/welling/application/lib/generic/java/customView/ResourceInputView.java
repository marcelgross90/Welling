package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class ResourceInputView extends de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView {

	private final ClassName resourceClassName;

	private final FieldSpec context = FieldSpec.builder(getContextClass(), "context", Modifier.PRIVATE, Modifier.FINAL).build();

	public ResourceInputView(String packageName) {
		super(packageName, "ResourceInputView", getScrollViewClassName());
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
	}

	@Override
	public Modifier[] addClassModifiers() {
		return new Modifier[]{Modifier.PUBLIC, Modifier.ABSTRACT};
	}

	@Override
	public List<MethodSpec> addConstructors() {
		return getStandardConstructors();
	}

	@Override
	public List<FieldSpec> addFields() {
		List<FieldSpec> fields = new ArrayList<>();
		fields.add(context);
		return fields;
	}

	@Override
	public MethodSpec getInitMethod() {
		return getInitMethodSignature()
				.addStatement("$T $N = ($T) $N.getSystemService($T.LAYOUT_INFLATER_SERVICE)", getLayoutInflaterClassName(), "inflater",
						getLayoutInflaterClassName(), getContextParam(), getContextClass())
				.addStatement("this.addView($N.inflate($N(), this, false))", "inflater", getGetLayout())
				.addStatement("$T $N = $N.getTheme().obtainStyledAttributes(attributeSet, $N(), $N, 0)",
						getTypedArrayClassName(), "typedArray", getContextParam(), getGetStyleable(), defStyleAttr)
				.beginControlFlow("try")
				.addStatement("$N", getInitializeView())
				.endControlFlow()
				.beginControlFlow("finally")
				.addStatement("$N.recycle()", "typedArray")
				.endControlFlow()
				.build();
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getSetResource());
		methods.add(getGetResource());
		methods.add(getInitializeView());
		methods.add(getGetLayout());
		methods.add(getGetStyleable());
		return methods;
	}

	@Override
	protected MethodSpec getConstructorOne() {
		return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(getContextParam())
				.addStatement("super($N)", getContextParam()).addStatement("$N($N, null, 0)", getInitMethod(), getContextParam())
				.addStatement("this.$N = $N", context, getContextParam()).build();
	}

	@Override
	protected MethodSpec getConstructorTwo() {
		return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(getContextParam())
				.addParameter(getAttributeSetParam()).addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.addStatement("$N($N, $N, 0)", getInitMethod(), getContextParam(), getAttributeSetParam())
				.addStatement("this.$N = $N", context, getContextParam()).build();
	}

	@Override
	protected MethodSpec getConstructorThree() {

		return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(getContextParam())
				.addParameter(getAttributeSetParam()).addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), defStyleAttr)
				.addStatement("$N($N, $N, $N)", getInitMethod(), getContextParam(), getAttributeSetParam(), defStyleAttr)
				.addStatement("this.$N = $N", context, getContextParam()).build();
	}

	private MethodSpec getSetResource() {
		return MethodSpec.methodBuilder("setResource")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(void.class)
				.addParameter(resourceClassName, "resource")
				.build();
	}

	private MethodSpec getGetResource() {
		return MethodSpec.methodBuilder("getResource")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(resourceClassName)
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout").addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(int.class).build();
	}

	private MethodSpec getGetStyleable() {
		return MethodSpec.methodBuilder("getStyleable").addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(int[].class).build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView").addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(void.class).build();
	}
}
