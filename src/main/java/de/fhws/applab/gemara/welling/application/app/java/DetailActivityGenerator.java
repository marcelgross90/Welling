package de.fhws.applab.gemara.welling.application.app.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.AppDetailCardView;
import de.fhws.applab.gemara.welling.metaModel.view.TitleVisitorImpl;
import de.fhws.applab.gemara.welling.metaModel.view.ViewObject;

import javax.lang.model.element.Modifier;

import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DetailActivityGenerator extends AbstractModelClass {

	private final AppResource appResource;

	private final ClassName resourceDetailActivityClassName;
	private final ClassName specificResourceDetailViewClassName;
	private final ClassName mainActivityClassName;
	private final ClassName rClassName;
	private final ClassName thisClassName;
	private final ClassName specificResourceClassName;
	private final ClassName networkCallbackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName linkClassName;

	public DetailActivityGenerator(String packageName, AppResource appResource, String appName) {
		super(packageName, appResource.getResourceName() + "DetailActivity");

		this.appResource = appResource;

		this.resourceDetailActivityClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.activity", "ResourceDetailActivity");
		this.specificResourceDetailViewClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.specific.customView", appResource.getResourceName() + "DetailView");
		this.mainActivityClassName = ClassName.get(packageName, "MainActivity");
		this.rClassName = ClassName.get(packageName, "R");
		this.thisClassName = ClassName.get(this.packageName, this.className);
		this.specificResourceClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.specific.model", appResource.getResourceName());
		this.networkCallbackClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkResponse");
		this.linkClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.model", "Link");
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec typeSpec = TypeSpec.classBuilder(this.className)
				.superclass(resourceDetailActivityClassName)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getGetIntentForClose())
				.addMethod(getGetDeleteErrorMessage())
				.addMethod(getGetLayout())
				.addMethod(getInitializeView())
				.addMethod(getGetEnterAnim())
				.addMethod(getGetExitAnim())
				.addMethod(getGetIntentForEdit())
				.addMethod(getPrepareBundle())
				.addMethod(getExtractTitleFromIntent())
				.addMethod(getGetCallback())
				.addMethod(getSetUp())
				.build();
		return JavaFile.builder(this.packageName, typeSpec).build();
	}

	private MethodSpec getGetIntentForClose() {
		return MethodSpec.methodBuilder("getIntentForClose")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getIntentClassName())
				.addStatement("return new $T($N, $T.class)", getIntentClassName(), "this", mainActivityClassName)
				.build();
	}

	private MethodSpec getGetDeleteErrorMessage() {
		return MethodSpec.methodBuilder("getDeleteErrorMessage")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.string.$N", rClassName, appResource.getResourceName().toLowerCase() + "_delete_error")
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "activity_" + appResource.getResourceName().toLowerCase() + "_detail")
				.build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(void.class)
				.addStatement("$N = ($T) findViewById($T.id.$N)", "resourceDetailView", specificResourceDetailViewClassName, rClassName,
						"detail_view")
				.addStatement("$N = ($T) findViewById($T.id.$N)", "toolbar", getToolbarClassname(), rClassName, "toolbar")
				.build();
	}

	private MethodSpec getGetEnterAnim() {
		return MethodSpec.methodBuilder("getEnterAnim")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.anim.$N", rClassName, "fade_out")
				.build();
	}

	private MethodSpec getGetExitAnim() {
		return MethodSpec.methodBuilder("getExitAnim")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.anim.$N", rClassName, "fade_in")
				.build();
	}

	private MethodSpec getGetIntentForEdit() {
		//todo
		return MethodSpec.methodBuilder("getIntentForEdit")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getIntentClassName())
				.addStatement("return null")
				//.addStatement("return new $T($T.this, $T.class)", getIntentClassName(), thisClassName, appResource.getResourceName() + "Activity")
				.build();
	}

	private MethodSpec getPrepareBundle() {
		AppDetailCardView appDetailCardView = appResource.getAppDetailCardView();
		ViewObject title = appDetailCardView.getTitle();
		TitleVisitorImpl visitor = new TitleVisitorImpl();
		return MethodSpec.methodBuilder("prepareBundle")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getBundleClassName())
				.addStatement("$T $N = ($T) $N", specificResourceClassName, appResource.getResourceName().toLowerCase(), specificResourceClassName, "currentResource")
				.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName())
				.addStatement("$N.putString($S, $N)", "bundle", "name", title.getTitleForMethodSpec(visitor, appResource))
				.addStatement("return $N", "bundle")
				.build();

	}

	private MethodSpec getExtractTitleFromIntent() {
		return MethodSpec.methodBuilder("extractTitleFromIntent")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(String.class)
				.addParameter(getIntentClassName(), "intent")
				.addStatement("return getIntent().getExtras().getString($S)", "fullName")
				.build();
	}

	private MethodSpec getGetCallback() {
		ParameterSpec response = ParameterSpec.builder(networkResponseClassName, "response").build();
		ParameterizedTypeName stringLinkMap = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), linkClassName);

		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(MethodSpec.methodBuilder("run").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class).addStatement("$N(($T) $N)", getSetUp(), specificResourceClassName, "currentResource").build())
				.build();

		TypeSpec callback = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallbackClassName)
				.addMethod(
						MethodSpec.methodBuilder("onFailure")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.build())
				.addMethod(
						MethodSpec.methodBuilder("onSuccess")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addParameter(response)
						.addStatement("$N = $N.deserialize($N.getResponseReader(), $T.class)", "currentResource", "genson", response, specificResourceClassName)
						.addStatement("$T $N = $N.getLinkHeader()", stringLinkMap, "linkHeader", response)
						.addStatement("$N = $N.get($T.this.getString($T.string.$N))", "deleteLink", "linkHeader", thisClassName, rClassName, "rel_type_delete_" + appResource.getResourceName().toLowerCase())
						.addStatement("$N = $N.get($T.this.getString($T.string.$N))", "updateLink", "linkHeader", thisClassName, rClassName, "rel_type_update_" + appResource.getResourceName().toLowerCase())
						.addStatement("runOnUiThread($L)", runnable)
						.build())
				.build();

		return MethodSpec.methodBuilder("getCallback")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallbackClassName)
				.addStatement("return $L", callback)
				.build();
	}

	private MethodSpec getSetUp() {
		return MethodSpec.methodBuilder("setUp")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addParameter(specificResourceClassName, appResource.getResourceName().toLowerCase())
				.addStatement("invalidateOptionsMenu()")
						//todo add clickListener
						//.addStatement("(($T) $N).setUpView($N, $N)", specificResourceDetailViewClassName, "resourceDetailView", appResource.getResourceName().toLowerCase(), "this")
				.addStatement("(($T) $N).setUpView($N, $N)", specificResourceDetailViewClassName, "resourceDetailView", appResource.getResourceName().toLowerCase(), "null")
				.build();
	}

}
