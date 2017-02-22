package de.fhws.applab.gemara.welling.application.lib.generic.java.activity;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.application.androidSpecifics.AbstractActivityClass;
import de.fhws.applab.gemara.welling.application.androidSpecifics.LifecycleMethods;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getActionbarClassname;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAppCompatActivityClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentManagerField;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getOnBackStackChangedListenerClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getSavedInstanceStateParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getToastClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getToolbarClassname;

public class AbstractMainActivity extends AbstractActivityClass {

	private final ClassName thisClassName;
	private final ClassName rClassName;
	private final ClassName linkClassName;
	private final ClassName fragmentHandlerClassName;
	private final ClassName networkClientClassName;
	private final ClassName networkRequestClassName;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;

	private final FieldSpec fragmentManager = getFragmentManagerField(Modifier.PRIVATE);

	public AbstractMainActivity(String packageName) {
		super(packageName + ".generic.activity", "AbstractMainActivity");
		this.thisClassName = ClassName.get(this.packageName, this.className);
		this.linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		this.rClassName = ClassName.get(packageName, "R");
		this.fragmentHandlerClassName = ClassName.get(packageName + ".generic.util", "FragmentHandler");
		this.networkClientClassName = ClassName.get(packageName + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(packageName + ".generic.network", "NetworkRequest");
		this.networkCallBackClassName = ClassName.get(packageName + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(packageName + ".generic.network", "NetworkResponse");
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
		methods.add(getGetStartFragment());
		methods.add(getGetLoadErrorMessage());
		methods.add(getOnBackPressed());
		methods.add(getOnSupportNavigateUp());
		methods.add(getOnCreate());
		methods.add(getInitialNetworkRequest());
		methods.add(getInitToolbar());
		methods.add(getCanBack());
		return methods;
	}

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(getAppCompatActivityClassName())
				.addSuperinterfaces(getSuperInterfaces())
				.addModifiers(getClassModifier())
				.addFields(getFields())
				.addMethods(getMethods())
				.build();

		return JavaFile.builder(this.packageName, type)
				.addStaticImport(fragmentHandlerClassName, "replaceFragment")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetStartFragment() {
		// @formatter:off
		return MethodSpec.methodBuilder("getStartFragment")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getFragmentClassName())
				.build();
		// @formatter:on
	}

	private MethodSpec getGetLoadErrorMessage() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLoadErrorMessage")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getOnBackPressed() {
		// @formatter:off
		return MethodSpec.methodBuilder("onBackPressed")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.beginControlFlow("if ($N.getBackStackEntryCount() > 1)", fragmentManager)
				.addStatement("super.onBackPressed()")
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("finish()")
				.endControlFlow()
				.build();
		// @formatter:on
	}

	private MethodSpec getOnSupportNavigateUp() {
		// @formatter:off
		return MethodSpec.methodBuilder("onSupportNavigateUp")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(boolean.class)
				.addStatement("$N.popBackStack()", fragmentManager)
				.addStatement("return false")
				.build();
		// @formatter:on
	}

	private MethodSpec getOnCreate() {
		// @formatter:off
		return LifecycleMethods.getOnCreate()
				.addStatement("setContentView($T.layout.activity_main)", rClassName)
				.addStatement("$N = getSupportFragmentManager()", fragmentManager)
				.addStatement("$N()", getInitToolbar())
				.beginControlFlow("if ($N == null)", getSavedInstanceStateParam())
				.addStatement("$N()", getInitialNetworkRequest())
				.endControlFlow()
				.build();
		// @formatter:on
	}

	private MethodSpec getInitialNetworkRequest() {
		// @formatter:off
		return MethodSpec.methodBuilder("initialNetworkRequest")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addStatement("$T $N = new $T(this, new $T().url(getResources().getString($T.string.entry_url)))",
						networkClientClassName, "client", networkClientClassName, networkRequestClassName, rClassName)
				.addStatement("$N.sendRequest($L)", "client", getNetworkCallback())
				.build();
		// @formatter:on
	}

	private TypeSpec getNetworkCallback() {
		ParameterizedTypeName stringLinkMap = ParameterizedTypeName
				.get(ClassName.get(Map.class), ClassName.get(String.class), linkClassName);

		// @formatter:off
		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(
						MethodSpec.methodBuilder("onFailure")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addStatement("runOnUiThread($L)", getRunnable())
								.build())
				.addMethod(
						MethodSpec.methodBuilder("onSuccess")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addParameter(ParameterSpec.builder(networkResponseClassName, "response").build())
								.addStatement("$T $N = $N.getLinkHeader()", stringLinkMap, "linkHeader", "response")
								.addStatement("$T $N = $N.get(getResources().getString($T.string.rel_type_first_state))",
										linkClassName, "allResourceLink", "linkHeader", rClassName)
								.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName())
								.addStatement("$N.putString($S, $N.getHrefWithoutQueryParams())", "bundle", "url", "allResourceLink")
								.addStatement("$N.putString($S, $N.getType())", "bundle", "mediaType", "allResourceLink")
								.addStatement("$T $N = getStartFragment()", getFragmentClassName(), "fragment")
								.addStatement("$N.setArguments($N)", "fragment", "bundle")
								.addStatement("replaceFragment($N, $N)", fragmentManager, "fragment")
								.build()
				).build();
		// @formatter:on
	}

	private TypeSpec getRunnable() {
		// @formatter:off
		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addAnnotation(Override.class)
								.addStatement("$T.makeText($T.this, $N(), $T.LENGTH_SHORT).show()",
										getToastClassName(), thisClassName, getGetLoadErrorMessage(), getToastClassName())
								.build())
				.build();
		// @formatter:on
	}

	private MethodSpec getInitToolbar() {
		// @formatter:off
		return MethodSpec.methodBuilder("initToolbar")
				.addModifiers(Modifier.PRIVATE).returns(void.class)
				.addStatement("$T $N = ($T) findViewById($T.id.toolbar)",
						getToolbarClassname(), "toolbar", getToolbarClassname(), rClassName)
				.addStatement("setSupportActionBar($N)", "toolbar")
				.addStatement("$N.addOnBackStackChangedListener($L)", fragmentManager, getOnBackStackChangedListener())
				.addStatement("$N()", getCanBack())
				.build();
		// @formatter:on
	}

	private TypeSpec getOnBackStackChangedListener() {
		// @formatter:off
		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(getOnBackStackChangedListenerClassName())
				.addMethod(
						MethodSpec.methodBuilder("onBackStackChanged")
							.addModifiers(Modifier.PUBLIC)
							.returns(void.class)
							.addAnnotation(Override.class)
							.addStatement("$N()", getCanBack())
							.build())
				.build();
		// @formatter:on
	}

	private MethodSpec getCanBack() {
		// @formatter:off
		return MethodSpec.methodBuilder("canBack")
				.addModifiers(Modifier.PRIVATE).returns(void.class)
				.addStatement("$T $N = getSupportActionBar()", getActionbarClassname(), "actionBar")
				.beginControlFlow("if ($N != null)", "actionBar")
				.addStatement("$N.setDisplayHomeAsUpEnabled($N.getBackStackEntryCount() > 1)", "actionBar", fragmentManager)
				.endControlFlow()
				.build();
		// @formatter:on
	}
}