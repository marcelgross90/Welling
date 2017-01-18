package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getCardViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextClass;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTypedArrayClassName;


public class ResourceCardView extends CustomView {

	private final ClassName resourceClassName;

	public ResourceCardView(String packageName) {
		super(packageName + ".generic.customView", "ResourceCardView", getCardViewClassName());
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
		return Collections.emptyList();
	}

	@Override
	public MethodSpec getInitMethod() {
		return getInitMethodSignature()
				.addStatement("$T inflater = ($T) $N.getSystemService($T.LAYOUT_INFLATER_SERVICE)", getLayoutInflaterClassName(),
						getLayoutInflaterClassName(), getContextParam(), getContextClass())
				.addStatement("this.addView($N.inflate($N(), this, false))", "inflater", getGetLayout())
				.addStatement("$T typedArray = $N.getTheme().obtainStyledAttributes(attributeSet, $N(), $N, 0)",
						getTypedArrayClassName(), getContextParam(), getGetStyleable(), defStyleAttr)
				.beginControlFlow("try")
				.addStatement("$N()", getInitializeView())
				.endControlFlow().beginControlFlow("finally").addStatement("typedArray.recycle()").endControlFlow()
				.build();
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getGetLayout());
		methods.add(getGetStyleable());
		methods.add(getInitializeView());
		methods.add(getSetUpView());
		return methods;
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(int.class)
				.build();
	}

	private MethodSpec getGetStyleable() {
		return MethodSpec.methodBuilder("getStyleable")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(int[].class)
				.build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(void.class)
				.build();
	}

	private MethodSpec getSetUpView() {
		return MethodSpec.methodBuilder("setUpView")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(void.class)
				.addParameter(ParameterSpec.builder(resourceClassName, "resource").build())
				.build();
	}
}
