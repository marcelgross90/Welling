package de.fhws.applab.gemara.welling.application.lib.generic.java.activity;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.AbstractActivityClass;
import de.fhws.applab.gemara.welling.androidMethods.LifecycleMethods;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class ResourceActivity extends AbstractActivityClass {

	private final ClassName rClassName;
	private final ClassName fragmentHandlerClassName;

	private final FieldSpec fragmentManager = getFragmentManagerField(Modifier.PROTECTED);

	public ResourceActivity(String packageName) {
		super(packageName + ".generic.activity", "ResourceActivity");
		this.rClassName = ClassName.get(packageName, "R");
		this.fragmentHandlerClassName = ClassName.get(packageName + ".generic.util", "FragmentHandler");
	}

	@Override
	protected List<ClassName> getSuperinterfaces() {
		return Collections.emptyList();
	}

	@Override
	protected Modifier[] getClassModifier() {
		return new Modifier[]{Modifier.PUBLIC, Modifier.ABSTRACT};
	}

	@Override
	protected List<FieldSpec> getFields() {
		List<FieldSpec> fields = new ArrayList<>();
		fields.add(fragmentManager);
		return fields;
	}

	@Override
	protected List<MethodSpec> getMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getHandleIntentAndPrepareFragment());
		methods.add(getSetUpToolbar());
		methods.add(getOnSupportNavigateUp());
		methods.add(getOnCreate());
		return methods;
	}

	private MethodSpec getHandleIntentAndPrepareFragment() {
		return MethodSpec.methodBuilder("handleIntentAndPrepareFragment")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(getFragmentClassName())
				.addParameter(getIntentClassName(), "intent")
				.build();
	}

	private MethodSpec getSetUpToolbar() {
		return MethodSpec.methodBuilder("setUpToolbar")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT).returns(void.class)
				.build();
	}

	private MethodSpec getOnSupportNavigateUp() {
		return MethodSpec.methodBuilder("onSupportNavigateUp")
				.addModifiers(Modifier.PUBLIC).returns(Boolean.class)
				.addAnnotation(Override.class)
				.addStatement("onBackPressed()")
				.addStatement("return false")
				.build();
	}

	private MethodSpec getOnCreate() {
		return LifecycleMethods.getOnCreate()
				.addStatement("setContentView($T.layout.activity_main", rClassName)
				.addStatement("$N = getSupportFragmentManager()", fragmentManager)
				.addStatement("$N()", getSetUpToolbar())
				.addStatement("$T $N = getIntent()", getIntentClassName(), "intent")
				.beginControlFlow("if ($N == null && $N != null)", getSavedInstanceStateParam(), "intent")
				.addStatement("$T $N = $N($N)", getFragmentClassName(), "fragment", getHandleIntentAndPrepareFragment(), "intent")
				.addStatement("$T.replaceFragment($N, $N)", fragmentHandlerClassName, fragmentManager, "fragment")
				.endControlFlow()
				.build();
	}
}