package de.fhws.applab.gemara.welling.application.app.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.attributes.sub.ResourceCollectionAttribute;
import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.Category;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModelExtension.AppAndroidManifest;
import de.fhws.applab.gemara.welling.visitors.ClickActionDetailViewVisitor;
import de.fhws.applab.gemara.welling.visitors.ContainsSubResourceVisitor;
import de.fhws.applab.gemara.welling.visitors.TitleVisitor;

import javax.lang.model.element.Modifier;
import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getIntentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getToolbarClassname;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewOnClickListenerClassName;

public class DetailActivityGenerator extends AbstractModelClass {

	private final DetailView detailView;
	private final AppDescription appDescription;
	private final StateHolder stateHolder;

	private final ClassName resourceDetailActivityClassName;
	private final ClassName resourceActivityClassName;
	private final ClassName specificResourceDetailViewClassName;
	private final ClassName mainActivityClassName;
	private final ClassName rClassName;
	private final ClassName thisClassName;
	private final ClassName specificResourceClassName;
	private final ClassName networkCallbackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName linkClassName;

	public DetailActivityGenerator(AppDescription appDescription, DetailView detailView, StateHolder stateHolder) {
		super(appDescription.getAppPackageName(), detailView.getResourceName() + "DetailActivity");

		this.detailView = detailView;
		this.appDescription = appDescription;
		this.stateHolder = stateHolder;

		this.resourceActivityClassName = ClassName.get(appDescription.getAppPackageName(), detailView.getResourceName() + "Activity");
		this.resourceDetailActivityClassName = ClassName
				.get(appDescription.getLibPackageName() + ".generic.activity", "ResourceDetailActivity");
		this.specificResourceDetailViewClassName = ClassName
				.get(appDescription.getLibPackageName() + ".specific.customView", detailView.getResourceName() + "DetailView");
		this.mainActivityClassName = ClassName.get(appDescription.getAppPackageName(), "MainActivity");
		this.rClassName = ClassName.get(appDescription.getAppPackageName(), "R");
		this.thisClassName = ClassName.get(this.packageName, this.className);
		this.specificResourceClassName = ClassName
				.get(appDescription.getLibPackageName() + ".specific.model", detailView.getResourceName());
		this.networkCallbackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");
		this.linkClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Link");
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
		TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.className);
		typeSpec.superclass(resourceDetailActivityClassName);
		typeSpec.addSuperinterface(getViewOnClickListenerClassName());
		typeSpec.addModifiers(Modifier.PUBLIC);
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			typeSpec.addMethod(getGetIntentForClose());
			typeSpec.addMethod(getGetDeleteErrorMessage());
		}
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			typeSpec.addMethod(getGetIntentForEdit());
			typeSpec.addMethod(getPrepareBundle());
		}
		typeSpec.addMethod(getGetLayout());
		typeSpec.addMethod(getInitializeView());
		typeSpec.addMethod(getGetEnterAnim());
		typeSpec.addMethod(getGetExitAnim());
		typeSpec.addMethod(getExtractTitleFromIntent());
		typeSpec.addMethod(getGetCallback());
		typeSpec.addMethod(getSetUp());
		typeSpec.addMethod(getOnClick());

		return JavaFile.builder(this.packageName, typeSpec.build()).build();
	}

	private MethodSpec getGetIntentForClose() {
		// @formatter:off
		return MethodSpec.methodBuilder("getIntentForClose")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getIntentClassName())
				.addStatement("return new $T($N, $T.class)", getIntentClassName(), "this", mainActivityClassName)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetDeleteErrorMessage() {
		addString(replaceIllegalCharacters(detailView.getResourceName().toLowerCase()) + "_delete_error",
				"Could not delete " + detailView.getResourceName().toLowerCase());

		// @formatter:off
		return MethodSpec.methodBuilder("getDeleteErrorMessage")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.string.$N",
						rClassName, replaceIllegalCharacters(detailView.getResourceName().toLowerCase()) + "_delete_error")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetLayout() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "activity_" + detailView.getResourceName().toLowerCase() + "_detail")
				.build();
		// @formatter:on
	}

	private MethodSpec getInitializeView() {
		// @formatter:off
		return MethodSpec.methodBuilder("initializeView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(void.class)
				.addStatement("$N = ($T) findViewById($T.id.$N)",
						"resourceDetailView", specificResourceDetailViewClassName, rClassName, "detail_view")
				.addStatement("$N = ($T) findViewById($T.id.$N)", "toolbar", getToolbarClassname(), rClassName, "toolbar")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetEnterAnim() {
		// @formatter:off
		return MethodSpec.methodBuilder("getEnterAnim")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.anim.$N", rClassName, "fade_out")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetExitAnim() {
		// @formatter:off
		return MethodSpec.methodBuilder("getExitAnim")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.anim.$N", rClassName, "fade_in")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetIntentForEdit() {
		// @formatter:off
		return MethodSpec.methodBuilder("getIntentForEdit")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getIntentClassName())
				.addStatement("return new $T($T.this, $T.class)", getIntentClassName(), thisClassName, resourceActivityClassName)
				.build();
		// @formatter:on
	}

	private MethodSpec getPrepareBundle() {
		TitleVisitor titleVisitor = new TitleVisitor(detailView.getResourceName());
		detailView.getTitle().accept(titleVisitor);

		// @formatter:off
		return MethodSpec.methodBuilder("prepareBundle")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getBundleClassName())
				.addStatement("$T $N = ($T) $N",
						specificResourceClassName, detailView.getResourceName().toLowerCase(), specificResourceClassName, "currentResource")
				.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName())
				.addStatement("$N.putString($S, $N)", "bundle", "name", titleVisitor.getTitle())
				.addStatement("return $N", "bundle")
				.build();
		// @formatter:on
	}

	private MethodSpec getExtractTitleFromIntent() {
		// @formatter:off
		return MethodSpec.methodBuilder("extractTitleFromIntent")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(String.class)
				.addParameter(getIntentClassName(), "intent")
				.addStatement("return getIntent().getExtras().getString($S)", "fullName")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetCallback() {
		// @formatter:off
		TypeSpec callback = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallbackClassName)
				.addMethod(
						MethodSpec.methodBuilder("onFailure")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.build())
				.addMethod(getOnSuccess())
				.build();

		return MethodSpec.methodBuilder("getCallback")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallbackClassName)
				.addStatement("return $L", callback)
				.build();
		// @formatter:on
	}

	private MethodSpec getOnSuccess() {
		ParameterSpec response = ParameterSpec.builder(networkResponseClassName, "response").build();
		ParameterizedTypeName stringLinkMap = ParameterizedTypeName
				.get(ClassName.get(Map.class), ClassName.get(String.class), linkClassName);

		// @formatter:off
		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.addStatement("$N(($T) $N)", getSetUp(), specificResourceClassName, "currentResource")
								.build())
				.build();
		// @formatter:on

		MethodSpec.Builder builder = MethodSpec.methodBuilder("onSuccess");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(void.class);
		builder.addParameter(response);
		builder.addStatement("$N = $N.deserialize($N.getResponseReader(), $T.class)", "currentResource", "genson", response,
				specificResourceClassName);
		builder.addStatement("$T $N = $N.getLinkHeader()", stringLinkMap, "linkHeader", response);
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			builder.addStatement("$N = $N.get($T.this.getString($T.string.$N))", "deleteLink", "linkHeader", thisClassName, rClassName,
					appDescription.getAppRestAPI().getRestApi().get(StateHolder.StateType.DELETE + "_" + detailView.getResourceName())
							.getKey());
		}
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			builder.addStatement("$N = $N.get($T.this.getString($T.string.$N))", "updateLink", "linkHeader", thisClassName, rClassName,
					appDescription.getAppRestAPI().getRestApi().get(StateHolder.StateType.PUT + "_" + detailView.getResourceName())
							.getKey());
		}
		builder.addStatement("runOnUiThread($L)", runnable);
		return builder.build();
	}

	private MethodSpec getSetUp() {
		// @formatter:off
		return MethodSpec.methodBuilder("setUp")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addParameter(specificResourceClassName, detailView.getResourceName().toLowerCase())
				.addStatement("invalidateOptionsMenu()")
				.addStatement("(($T) $N).setUpView($N, $N)", specificResourceDetailViewClassName, "resourceDetailView",
						detailView.getResourceName().toLowerCase(), "this")
				.build();
		// @formatter:on
	}

	private MethodSpec getOnClick() {
		ContainsSubResourceVisitor visitor = new ContainsSubResourceVisitor();
		MethodSpec.Builder method = MethodSpec.methodBuilder("onClick");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addParameter(getViewClassName(), "view");

		method.addStatement("$T $N = ($T) $N", specificResourceClassName, "current" + detailView.getResourceName(),
				specificResourceClassName, "currentResource");

		method.addStatement("$T $N = $N.getId()", int.class, "i", "view");

		for (Category category : detailView.getCategories()) {
			for (ResourceViewAttribute resourceViewAttribute : category.getResourceViewAttributes()) {
				resourceViewAttribute.accept(visitor);
				if (visitor.isContainsSubResource()) {
					TitleVisitor titleVisitor = new TitleVisitor("current" + detailView.getResourceName());
					detailView.getTitle().accept(titleVisitor);
					method.beginControlFlow("if ($N == $T.id.$N)", "i", rClassName,
							"tv" + getInputWithCapitalStart(visitor.getViewName()) + "Value");

					method.addStatement("$T $N = new $T($T.this, $T.class)", getIntentClassName(), "intent3", getIntentClassName(),
							thisClassName, getSubResourceActivityClassname(visitor.getViewName()));
					method.addStatement("$N.putExtra($S, $N)", "intent3", "name", titleVisitor.getTitle());
					method.addStatement("$N.putExtra($S, $N)", "intent3", "url",
							"current" + detailView.getResourceName() + ".get" + getInputWithCapitalStart(visitor.getViewName())
									+ "().getHref()");
					method.addStatement("$N.putExtra($S, $N)", "intent3", "mediaType",
							"current" + detailView.getResourceName() + ".get" + getInputWithCapitalStart(visitor.getViewName())
									+ "().getType()");
					method.addStatement("startActivity($N)", "intent3");
					method.endControlFlow();
				}
				resourceViewAttribute
						.accept(new ClickActionDetailViewVisitor(method, "i", rClassName, "current" + detailView.getResourceName(), false));
			}
		}

		return method.build();
	}

	private ClassName getSubResourceActivityClassname(String attributeName) {
		SingleResource resource = appDescription.getResource(detailView.getResourceName()).getResource();

		ResourceCollectionAttribute collectionAttribute = (ResourceCollectionAttribute) resource.getAttributeByName(attributeName);

		return ClassName.get(packageName, getInputWithCapitalStart(collectionAttribute.getDatatype().getResourceName()) + "Activity");
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(key, value);
	}

	private String getInputWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
}