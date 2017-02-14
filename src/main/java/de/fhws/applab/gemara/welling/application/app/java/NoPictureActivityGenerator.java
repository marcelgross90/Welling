package de.fhws.applab.gemara.welling.application.app.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModelExtension.AppAndroidManifest;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getActionbarClassname;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getIntentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getOnBackStackChangedListenerClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getToolbarClassname;

public class NoPictureActivityGenerator extends AbstractModelClass {

	private final AppDescription appDescription;

	private final ClassName rClassName;
	private final ClassName resourceListFragmentClassName;
	private final ClassName resourceActivityClassName;

	public NoPictureActivityGenerator(AppDescription appDescription, String resourceName) {
		super(appDescription.getAppPackageName(), resourceName + "Activity");

		this.appDescription = appDescription;

		this.rClassName = ClassName.get(appDescription.getAppPackageName(), "R");
		this.resourceListFragmentClassName = ClassName.get(appDescription.getAppPackageName() + ".fragment", resourceName + "ListFragment");
		this.resourceActivityClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.activity", "ResourceActivity");

		addString("edit", "Edit");
		addActivityToManifest();
	}

	private void addActivityToManifest() {
		AppAndroidManifest manifest = appDescription.getAppManifest();
		AppAndroidManifest.Application application = manifest.getApplication();

		application.addActivities(new AppAndroidManifest.Activity(this.className));

		manifest.setApplication(application);

		appDescription.setManifestApplication(application);
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className).superclass(resourceActivityClassName).addModifiers(Modifier.PUBLIC)
				.addMethod(getOnBackPressed()).addMethod(getHandleIntentAndPrepareFragment()).addMethod(getSetUpToolbar())
				.addMethod(getCanBack()).build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getOnBackPressed() {
		return MethodSpec.methodBuilder("onBackPressed").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class)
				.beginControlFlow("if ($N.getBackStackEntryCount() > 1)", "fragmentManager").addStatement("super.onBackPressed()")
				.endControlFlow().beginControlFlow("else").addStatement("finish()")
				.addStatement("overridePendingTransition($T.anim.$N, $T.anim.$N)", rClassName, "fade_out", rClassName, "fade_in")
				.endControlFlow().build();
	}

	private MethodSpec getHandleIntentAndPrepareFragment() {
		ParameterSpec intent = ParameterSpec.builder(getIntentClassName(), "intent").build();

		return MethodSpec.methodBuilder("handleIntentAndPrepareFragment").addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
				.returns(getFragmentClassName()).addParameter(intent)
				.addStatement("$T $N = $N.getStringExtra($S)", String.class, "name", intent, "name").addStatement("setTitle($N)", "name")
				.addStatement("$T $N = new $T()", getFragmentClassName(), "fragment", resourceListFragmentClassName)
				.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName())
				.addStatement("$N.putString($S, $N.getStringExtra($S))", "bundle", "url", intent, "url")
				.addStatement("$N.putString($S, $N.getStringExtra($S))", "bundle", "mediaType", intent, "mediaType")
				.addStatement("$N.setArguments($N)", "fragment", "bundle").addStatement("return $N", "fragment").build();
	}

	private MethodSpec getSetUpToolbar() {
		return MethodSpec.methodBuilder("setUpToolbar").addAnnotation(Override.class).addModifiers(Modifier.PROTECTED).returns(void.class)

				.addStatement("$T $N = ($T) findViewById($T.id.$N)", getToolbarClassname(), "toolbar", getToolbarClassname(), rClassName,
						"toolbar").beginControlFlow("if ($N != null)", "toolbar").addStatement("setSupportActionBar($N)", "toolbar")
				.endControlFlow()

				.addStatement("$N.addOnBackStackChangedListener($L)", "fragmentManager", getOnBackStackChangedListener())
				.addStatement("$N()", getCanBack()).build();
	}

	private TypeSpec getOnBackStackChangedListener() {
		return TypeSpec.anonymousClassBuilder("").superclass(getOnBackStackChangedListenerClassName()).addMethod(
				MethodSpec.methodBuilder("onBackStackChanged").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.returns(void.class).addStatement("$N()", getCanBack()).build()).build();
	}

	private MethodSpec getCanBack() {
		return MethodSpec.methodBuilder("canBack")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addStatement("$T $N = getSupportActionBar()", getActionbarClassname(), "actionbar")
				.beginControlFlow("if ($N != $L)", "actionbar", null)
				.addStatement("$N.setDisplayHomeAsUpEnabled($N.getBackStackEntryCount() > 0)", "actionbar", "fragmentManager")
				.endControlFlow()
				.build();
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(key, value);
	}
}