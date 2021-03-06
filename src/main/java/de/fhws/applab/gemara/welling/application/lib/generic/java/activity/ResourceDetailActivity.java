package de.fhws.applab.gemara.welling.application.lib.generic.java.activity;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import de.fhws.applab.gemara.welling.application.androidSpecifics.AbstractActivityClass;
import de.fhws.applab.gemara.welling.application.androidSpecifics.LifecycleMethods;
import de.fhws.applab.gemara.welling.generator.StateHolder;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class ResourceDetailActivity extends AbstractActivityClass {

	private final StateHolder stateHolder;

	private final ClassName rClassName;
	private final ClassName deleteDialogListenerClassName;
	private final ClassName networkCallbackClassName;
	private final ClassName networkClientClassName;
	private final ClassName networkRequestClassName;
	private final ClassName deleteDialogFragmentClassName;

	private final FieldSpec resourceDetailView;
	private final FieldSpec genson;
	private final FieldSpec deleteLink;
	private final FieldSpec updateLink;
	private final FieldSpec toolbar;
	private final FieldSpec currentResource;


	public ResourceDetailActivity(StateHolder stateHolder, String packageName) {
		super(packageName + ".generic.activity", "ResourceDetailActivity");

		this.stateHolder = stateHolder;

		this.rClassName = ClassName.get(packageName, "R");
		this.deleteDialogListenerClassName = ClassName.get(packageName + ".generic.fragment.DeleteDialogFragment", "DeleteDialogListener");
		this.networkCallbackClassName = ClassName.get(packageName + ".generic.network", "NetworkCallback");
		this.networkClientClassName = ClassName.get(packageName + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(packageName + ".generic.network", "NetworkRequest");
		this.deleteDialogFragmentClassName = ClassName.get(packageName + ".generic.fragment", "DeleteDialogFragment");
		ClassName linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		ClassName resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		ClassName resourceDetailViewClassName = ClassName.get(packageName + ".generic.customView", "ResourceDetailView");
		ClassName gensonClassName = getGensonClassName();

		resourceDetailView = FieldSpec.builder(resourceDetailViewClassName, "resourceDetailView", Modifier.PROTECTED).build();
		genson = FieldSpec.builder(gensonClassName, "genson", Modifier.PROTECTED, Modifier.FINAL).initializer("new $T()", gensonClassName).build();
		deleteLink = FieldSpec.builder(linkClassName, "deleteLink", Modifier.PROTECTED).build();
		updateLink = FieldSpec.builder(linkClassName, "updateLink", Modifier.PROTECTED).build();
		toolbar = FieldSpec.builder(getToolbarClassname(), "toolbar", Modifier.PROTECTED).build();
		currentResource = FieldSpec.builder(resourceClassName, "currentResource", Modifier.PROTECTED).build();

	}

	@Override
	protected List<ClassName> getSuperInterfaces() {
		List<ClassName> interfaces = new ArrayList<>();
		if (stateHolder.contains(StateHolder.StateType.DELETE))
			interfaces.add(deleteDialogListenerClassName);
		return interfaces;
	}

	@Override
	protected Modifier[] getClassModifier() {
		return new Modifier[]{Modifier.PUBLIC, Modifier.ABSTRACT};
	}

	@Override
	protected List<FieldSpec> getFields() {
		List<FieldSpec> fields = new ArrayList<>();
		fields.add(resourceDetailView);
		fields.add(genson);
		fields.add(deleteLink);
		fields.add(updateLink);
		fields.add(toolbar);
		fields.add(currentResource);
		return fields;
	}

	@Override
	protected List<MethodSpec> getMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getGetLayout());
		methods.add(getInitializeView());
		methods.add(getGetEnterAnim());
		methods.add(getGetExitAnim());
		methods.add(getExtractTitleFromIntent());
		methods.add(getGetCallback());
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			methods.add(getOnDialogClosed());
			methods.add(getGetIntentForClose());
			methods.add(getGetDeleteErrorMessage());
		}
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			methods.add(getGetIntentForEdit());
			methods.add(getPrepareBundle());
		}
		methods.add(getOnCreate());
		methods.add(getOnCreateOptionsMenu());
		methods.add(getOnOptionsItemSelected());
		methods.add(getSetUpToolbar());
		methods.add(getLoadResource());
		return methods;
	}

	private MethodSpec getGetIntentForClose() {
		// @formatter:off
		return MethodSpec.methodBuilder("getIntentForClose")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getIntentClassName())
				.build();
		// @formatter:on
	}

	private MethodSpec getGetDeleteErrorMessage() {
		// @formatter:off
		return MethodSpec.methodBuilder("getDeleteErrorMessage")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetLayout() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getInitializeView() {
		// @formatter:off
		return MethodSpec.methodBuilder("initializeView")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(void.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetEnterAnim() {
		// @formatter:off
		return MethodSpec.methodBuilder("getEnterAnim")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetExitAnim() {
		// @formatter:off
		return MethodSpec.methodBuilder("getExitAnim")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetIntentForEdit() {
		// @formatter:off
		return MethodSpec.methodBuilder("getIntentForEdit")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getIntentClassName())
				.build();
		// @formatter:on
	}

	private MethodSpec getPrepareBundle() {
		// @formatter:off
		return MethodSpec.methodBuilder("prepareBundle")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getBundleClassName())
				.build();
		// @formatter:on
	}

	private MethodSpec getExtractTitleFromIntent() {
		// @formatter:off
		return MethodSpec.methodBuilder("extractTitleFromIntent")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(String.class)
				.addParameter(getIntentClassName(), "intent")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetCallback() {
		// @formatter:off
		return MethodSpec.methodBuilder("getCallback")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
		// @formatter:on
	}

	private MethodSpec getOnDialogClosed() {
		ParameterSpec successfullyDeleted = ParameterSpec.builder(boolean.class, "successfullyDeleted").build();

		// @formatter:off
		return MethodSpec.methodBuilder("onDialogClosed")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(successfullyDeleted)
				.beginControlFlow("if ($N)", successfullyDeleted)
				.addStatement("startActivity($N())", getGetIntentForClose())
				.addStatement("finish()")
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("$T.makeText(this, $N(), $T.LENGTH_SHORT).show()",
						getToastClassName(), getGetDeleteErrorMessage(), getToastClassName())
				.endControlFlow()
				.build();
		// @formatter:on
	}

	private MethodSpec getOnCreate() {
		// @formatter:off
		return LifecycleMethods.getOnCreate()
				.addStatement("setContentView($N())", getGetLayout())
				.addStatement("$N()", getInitializeView())
				.addStatement("$N()", getSetUpToolbar())
				.addStatement("$N()", getLoadResource())
				.build();
		// @formatter:on
	}

	private MethodSpec getOnCreateOptionsMenu() {
		ParameterSpec menu = ParameterSpec.builder(getMenuClassName(), "menu").build();

		// @formatter:off
		return MethodSpec.methodBuilder("onCreateOptionsMenu")
				.addModifiers(Modifier.PUBLIC)
				.returns(boolean.class)
				.addAnnotation(Override.class)
				.addParameter(menu)
				.addStatement("$T $N = getMenuInflater()", getMenuInflaterClassName(), "inflater")
				.addStatement("$N.inflate($T.menu.detail_menu, $N)", "inflater", rClassName, menu)
				.addStatement("$T $N = $N.findItem($T.id.delete_item)", getMenuItemClassName(), "deleteItem", menu, rClassName)
				.addStatement("$T $N = $N.findItem($T.id.edit_item)", getMenuItemClassName(), "updateItem", menu, rClassName)
				.addStatement("$N.setVisible($N != null)", "deleteItem", deleteLink)
				.addStatement("$N.setVisible($N != null)", "updateItem", updateLink)
				.addStatement("return super.onCreateOptionsMenu($N)", menu)
				.build();
		// @formatter:on
	}

	private MethodSpec getOnOptionsItemSelected() {
		ParameterSpec item = ParameterSpec.builder(getMenuItemClassName(), "item").build();

		MethodSpec.Builder method = MethodSpec.methodBuilder("onOptionsItemSelected");
		method.addModifiers(Modifier.PUBLIC).returns(boolean.class);
		method.addAnnotation(Override.class);
		method.addParameter(item);
		method.addStatement("$T $N = $N.getItemId()", int.class, "i", item);
		method.beginControlFlow("if ($N == android.R.id.home)", "i");
		method.addStatement("onBackPressed()");
		method.addStatement("overridePendingTransition($N(), $N())", getGetEnterAnim(), getGetExitAnim());
		method.addStatement("return true");
		method.endControlFlow();
		if (stateHolder.contains(StateHolder.StateType.PUT)){
			method.beginControlFlow("else if ($N == $T.id.edit_item)", "i", rClassName);
			method.addStatement("$T $N = $N()", getIntentClassName(), "intent", getGetIntentForEdit());
			method.addStatement("$N.putExtra($S, $N.getHref())", "intent", "url", updateLink);
			method.addStatement("$N.putExtra($S, $N.getType())", "intent", "mediaType", updateLink);
			method.addStatement("startActivity($N)", "intent");
			method.addStatement("return true");
			method.endControlFlow();
		}
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			method.beginControlFlow("else if ($N == $T.id.delete_item)", "i", rClassName);
			method.addStatement("$T $N = $N()", getBundleClassName(), "bundle", getPrepareBundle());
			method.addStatement("$N.putString($S, $N.getHref())", "bundle", "url", deleteLink);
			method.addStatement("$T $N = new $T()", deleteDialogFragmentClassName, "deleteDialogFragment", deleteDialogFragmentClassName);
			method.addStatement("$N.setArguments($N)", "deleteDialogFragment", "bundle");
			method.addStatement("$N.show(getSupportFragmentManager(), null)", "deleteDialogFragment");
			method.addStatement("return true");
			method.endControlFlow();
		}

		method.beginControlFlow("else");
		method.addStatement("return super.onOptionsItemSelected($N)", item);
		method.endControlFlow();

		return method.build();
	}

	private MethodSpec getSetUpToolbar() {
		// @formatter:off
		return MethodSpec.methodBuilder("setUpToolbar")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.beginControlFlow("if ($N != null)", toolbar)
				.addStatement("setSupportActionBar($N)", toolbar)
				.endControlFlow()
				.addStatement("$T $N = getSupportActionBar()", getActionbarClassname(), "actionBar")
				.beginControlFlow("if ($N != null)", "actionBar")
				.addStatement("$N.setDisplayHomeAsUpEnabled(true)", "actionBar")
				.endControlFlow()
				.addStatement("setTitle($N(getIntent()))", getExtractTitleFromIntent())
				.build();
		// @formatter:on
	}

	private MethodSpec getLoadResource() {
		// @formatter:off
		return MethodSpec.methodBuilder("loadResource")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addStatement("$T $N = getIntent()", getIntentClassName(), "intent")
				.addStatement("$T $N = $N.getExtras().getString($S, $S)", String.class, "selfUrl", "intent", "selfUrl", "")
				.addStatement("$T $N = $N.getExtras().getString($S, $S)", String.class, "mediaType", "intent", "mediaType", "")
				.addStatement("$T $N = new $T(this, new $T().acceptHeader($N).url($N))",
						networkClientClassName, "client", networkClientClassName, networkRequestClassName, "mediaType", "selfUrl")
				.addStatement("$N.sendRequest($N())", "client", getGetCallback())
				.build();
		// @formatter:on
	}
}