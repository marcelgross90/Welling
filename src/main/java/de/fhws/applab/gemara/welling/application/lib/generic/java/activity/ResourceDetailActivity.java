package de.fhws.applab.gemara.welling.application.lib.generic.java.activity;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import de.fhws.applab.gemara.welling.AbstractActivityClass;
import de.fhws.applab.gemara.welling.androidMethods.LifecycleMethods;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class ResourceDetailActivity extends AbstractActivityClass {

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


	public ResourceDetailActivity(String packageName, String className) {
		super(packageName + ".generic.activity", className);
		this.rClassName = ClassName.get(this.packageName, "R");
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
	protected List<ClassName> getSuperinterfaces() {
		List<ClassName> interfaces = new ArrayList<>();
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
		methods.add(getGetIntentForClose());
		methods.add(getGetDeleteErrorMessage());
		methods.add(getGetLayout());
		methods.add(getInitializeView());
		methods.add(getGetEnterAnim());
		methods.add(getGetExitAnim());
		methods.add(getGetIntentForEdit());
		methods.add(getPrepareBundle());
		methods.add(getExtractTitleFromIntent());
		methods.add(getGetCallback());
		methods.add(getOnDialogClosed());
		methods.add(getOnCreate());
		methods.add(getOnCreateOptionsMenu());
		methods.add(getOnOptionsItemSelected());
		methods.add(getSetUpToolbar());
		methods.add(getLoadResource());
		return methods;
	}

	private MethodSpec getGetIntentForClose() {
		return MethodSpec.methodBuilder("getIntentForClose")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getIntentClassName())
				.build();
	}

	private MethodSpec getGetDeleteErrorMessage() {
		return MethodSpec.methodBuilder("getDeleteErrorMessage")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(void.class)
				.build();
	}

	private MethodSpec getGetEnterAnim() {
		return MethodSpec.methodBuilder("getEnterAnim")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
	}

	private MethodSpec getGetExitAnim() {
		return MethodSpec.methodBuilder("getExitAnim")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(int.class)
				.build();
	}

	private MethodSpec getGetIntentForEdit() {
		return MethodSpec.methodBuilder("getIntentForEdit")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getIntentClassName())
				.build();
	}

	private MethodSpec getPrepareBundle() {
		return MethodSpec.methodBuilder("prepareBundle")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getBundleClassName())
				.build();
	}

	private MethodSpec getExtractTitleFromIntent() {
		return MethodSpec.methodBuilder("extractTitleFromIntent")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(String.class)
				.addParameter(getIntentClassName(), "intent")
				.build();
	}

	private MethodSpec getGetCallback() {
		return MethodSpec.methodBuilder("getCallback")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
	}

	private MethodSpec getOnDialogClosed() {
		ParameterSpec successfullyDeleted = ParameterSpec.builder(boolean.class, "successfullyDeleted").build();
		return MethodSpec.methodBuilder("onDialogClosed")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(successfullyDeleted)
				.beginControlFlow("if ($N)", successfullyDeleted)
				.addStatement("startActivity($N)", getGetIntentForClose())
				.addStatement("finish()")
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("$T.makeText(this, $N(), $T.LENGTH_SHORT).show()", getToastClassName(), getGetDeleteErrorMessage(), getToastClassName())
				.endControlFlow()
				.build();
	}

	private MethodSpec getOnCreate() {
		return LifecycleMethods.getOnCreate()
				.addStatement("setContentView($N())", getGetLayout())
				.addStatement("$N()", getInitializeView())
				.addStatement("$N()", getSetUpToolbar())
				.addStatement("$N()", getLoadResource())
				.build();
	}

	private MethodSpec getOnCreateOptionsMenu() {
		ParameterSpec menu = ParameterSpec.builder(getMenuClassName(), "menu").build();

		return MethodSpec.methodBuilder("onCreateOptionsMenu")
				.addModifiers(Modifier.PUBLIC).returns(boolean.class)
				.addAnnotation(Override.class)
				.addParameter(menu)
				.addStatement("$T $N = getMenuInflater()", getMenuInflaterClassName(), "inflater")
				.addStatement("$N.inflate($T.menu.detail_menu, $N)", "inflater", rClassName, menu)
				.addStatement("$T $N = §N.findItem($T.id.delete_item)", getMenuItemClassName(), "deleteItem", rClassName)
				.addStatement("$T $N = §N.findItem($T.id.edit_item)", getMenuItemClassName(), "updateItem", rClassName)
				.addStatement("$N.setVisible($N != null)", "deleteItem", deleteLink)
				.addStatement("$N.setVisible($N != null)", "updateItem", updateLink)
				.addStatement("return super.onCreateOptionsMenu($N)", menu)
				.build();
	}

	private MethodSpec getOnOptionsItemSelected() {
		ParameterSpec item = ParameterSpec.builder(getMenuItemClassName(), "item").build();

		return MethodSpec.methodBuilder("onOptionsItemSelected")
				.addModifiers(Modifier.PUBLIC).returns(boolean.class)
				.addAnnotation(Override.class)
				.addParameter(item)
				.addStatement("$T $N = $N.getItemId()", int.class, "i", item)
				.beginControlFlow("if ($N == android.R.id.home)", "i")
				.addStatement("onBackPressed()")
				.addStatement("overridePendingTransition($N, $N)", getGetEnterAnim(), getGetExitAnim())
				.addStatement("return true")
				.endControlFlow()
				.beginControlFlow("else if ($N == $T.id.edit_item)", "i", rClassName)
				.addStatement("$T $N = $N", getIntentClassName(), "intent", getGetIntentForEdit())
				.addStatement("$N.putExtra($S, $N.getHref())", "intent", "url", updateLink)
				.addStatement("$N.putExtra($S, $N.getType())", "intent", "mediaType", updateLink)
				.addStatement("startActivity($N)", "intent")
				.addStatement("return true")
				.endControlFlow()
				.beginControlFlow("else if ($N == $T.id.delete_item)", "i", rClassName)
				.addStatement("$T $N = $N", getBundleClassName(), "bundle", getPrepareBundle())
				.addStatement("$N.putString($S, $N.getHref())", "bundle", "url", deleteLink)
				.addStatement("$T $N = new $T()", deleteDialogFragmentClassName, "deleteDialogFragment", deleteDialogFragmentClassName)
				.addStatement("$N.setArguments($N)", "deleteDialogFragment", "bundle")
				.addStatement("$N.show(getSupportFragmentManager(), null)", "deleteDialogFragment")
				.addStatement("return true;")
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("return super.onOptionsItemSelected($N)", item)
				.endControlFlow()
				.build();
	}

	private MethodSpec getSetUpToolbar() {
		return MethodSpec.methodBuilder("setUpToolbar")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.beginControlFlow("if ($N != null)", toolbar)
				.addStatement("setSupportActionBar($N)", toolbar)
				.endControlFlow()
				.addStatement("$T $N = getSupportActionBar()", getActionbarClassname(), "actionBar")
				.beginControlFlow("if ($N != null)", "actionBar")
				.addStatement("$N.setDisplayHomeAsUpEnabled(true)", "actionBar")
				.endControlFlow()
				.addStatement("setTitle($N(getIntent()))", getExtractTitleFromIntent())
				.build();
	}

	private MethodSpec getLoadResource() {
		return MethodSpec.methodBuilder("loadResource")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addStatement("$T $N = getIntent()", getIntentClassName(), "intent")
				.addStatement("$T $N = $N.getExtras().getString($S, $S)", String.class, "selfUrl", "intent", "selfUrl", "")
				.addStatement("$T $N = $N.getExtras().getString($S, $S)", String.class, "mediaType", "intent", "mediaType", "")
				.addStatement("$T $N = new $T(this, new $T().acceptHeader($N).url($N))", networkClientClassName, "client", networkClientClassName, networkRequestClassName, "mediaType", "selfUrl")
				.addStatement("$N.sendRequest($N())", "client", getGetCallback())
				.build();
	}

}
