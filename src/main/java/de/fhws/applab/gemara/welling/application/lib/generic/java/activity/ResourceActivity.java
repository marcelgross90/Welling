package de.fhws.applab.gemara.welling.application.lib.generic.java.activity;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.application.androidSpecifics.AbstractActivityClass;
import de.fhws.applab.gemara.welling.application.androidSpecifics.LifecycleMethods;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentManagerField;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getIntentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getSavedInstanceStateParam;

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
	protected List<ClassName> getSuperInterfaces() {
		return Collections.emptyList();
	}

	@Override
	protected Modifier[] getClassModifier() {
		return new Modifier[] { Modifier.PUBLIC, Modifier.ABSTRACT };
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
		// @formatter:off
		return MethodSpec.methodBuilder("handleIntentAndPrepareFragment")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getFragmentClassName())
				.addParameter(getIntentClassName(), "intent")
				.build();
		// @formatter:on
	}

	private MethodSpec getSetUpToolbar() {
		// @formatter:off
		return MethodSpec.methodBuilder("setUpToolbar")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(void.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getOnSupportNavigateUp() {
		// @formatter:off
		return MethodSpec.methodBuilder("onSupportNavigateUp")
				.addModifiers(Modifier.PUBLIC)
				.returns(boolean.class)
				.addAnnotation(Override.class)
				.addStatement("onBackPressed()")
				.addStatement("return false")
				.build();
		// @formatter:on
	}

	private MethodSpec getOnCreate() {
		// @formatter:off
		return LifecycleMethods.getOnCreate()
				.addStatement("setContentView($T.layout.activity_main)", rClassName)
				.addStatement("$N = getSupportFragmentManager()", fragmentManager)
				.addStatement("$N()", getSetUpToolbar())
				.addStatement("$T $N = getIntent()", getIntentClassName(), "intent")
				.beginControlFlow("if ($N == null && $N != null)", getSavedInstanceStateParam(), "intent")
				.addStatement("$T $N = $N($N)", getFragmentClassName(), "fragment", getHandleIntentAndPrepareFragment(), "intent")
				.addStatement("$T.replaceFragment($N, $N)", fragmentHandlerClassName, fragmentManager, "fragment")
				.endControlFlow()
				.build();
		// @formatter:on
	}
}