package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.application.androidSpecifics.LifecycleMethods;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DetailResourceFragment extends AbstractModelClass {

	private final StateHolder stateHolder;

	private final ClassName rClassName;
	private final ClassName thisClassName;
	private final ClassName fragmentHandlerClassName;
	private final ClassName deleteDialogFragmentClassName;
	private final ClassName deleteDialogListenerClassName;
	private final ClassName networkCallbackClassName;
	private final ClassName networkClientClassName;
	private final ClassName networkRequestClassName;
	private final ClassName bundleClassName;

	private final FieldSpec resourceUrl;
	private final FieldSpec mediaTyp;
	private final FieldSpec genson;
	private final FieldSpec currentResource;
	private final FieldSpec resourceDetailView;
	private final FieldSpec deleteLink;
	private final FieldSpec updateLink;

	public DetailResourceFragment(String packageName, StateHolder stateHolder) {
		super(packageName + ".generic.fragment", "DetailResourceFragment");

		this.stateHolder = stateHolder;

		this.rClassName = ClassName.get(packageName, "R");
		this.thisClassName = ClassName.get(this.packageName, this.className);
		this.fragmentHandlerClassName = ClassName.get(packageName + ".generic.util", "FragmentHandler");
		this.deleteDialogFragmentClassName = ClassName.get(this.packageName, "DeleteDialogFragment");
		this.deleteDialogListenerClassName = ClassName.get(this.packageName + ".DeleteDialogFragment", "DeleteDialogListener");
		this.networkCallbackClassName = ClassName.get(packageName + ".generic.network", "NetworkCallback");
		this.networkClientClassName = ClassName.get(packageName + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(packageName + ".generic.network", "NetworkRequest");
		this.bundleClassName = getBundleClassName();
		ClassName gensonBuilderClassName = ClassName.get(packageName + ".generic.util", "GensonBuilder");
		ClassName resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		ClassName resourceDetailViewClassName = ClassName.get(packageName + ".generic.customView", "ResourceDetailView");
		ClassName linkClassName = ClassName.get(packageName + ".generic.model", "Link");

		this.resourceUrl = FieldSpec.builder(String.class, "resourceUrl", Modifier.PRIVATE).build();
		this.mediaTyp = FieldSpec.builder(String.class, "mediaType", Modifier.PRIVATE).build();
		this.genson = FieldSpec.builder(getGensonClassName(), "genson", Modifier.PROTECTED, Modifier.FINAL).initializer("new $T().getDateFormatter()",
				gensonBuilderClassName).build();
		this.currentResource = FieldSpec.builder(resourceClassName, "currentResource", Modifier.PROTECTED).build();
		this.resourceDetailView = FieldSpec.builder(resourceDetailViewClassName, "resourceDetailView", Modifier.PROTECTED).build();
		this.deleteLink = FieldSpec.builder(linkClassName, "deleteLink", Modifier.PROTECTED).build();
		this.updateLink = FieldSpec.builder(linkClassName, "updateLink", Modifier.PROTECTED).build();
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
		type.superclass(getFragmentClassName());
		type.addField(updateLink);
		type.addField(deleteLink);

		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			type.addMethod(getGetEditFragment());
		}

		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			type.addSuperinterface(deleteDialogListenerClassName);
			type.addMethod(getGetResourceDeleteError());
			type.addMethod(getPrepareDeleteBundle());
			type.addMethod(getOnDialogClosed());
		}


		type.addField(resourceUrl);
		type.addField(mediaTyp);
		type.addField(genson);
		type.addField(currentResource);
		type.addField(resourceDetailView);



		type.addMethod(getGetLayout());
		type.addMethod(getInitializeView());

		type.addMethod(getGetCallback());

		type.addMethod(getOnCreate());
		type.addMethod(getOnSaveInstanceState());
		type.addMethod(getOnCreateView());
		type.addMethod(getOnCreateOptionsMenu());
		type.addMethod(getOnOptionsItemSelected());
		type.addMethod(getLoadResource());

		return JavaFile.builder(this.packageName, type.build()).build();
	}

	private MethodSpec getGetResourceDeleteError() {
		return MethodSpec.methodBuilder("getResourceDeleteError")
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
				.addParameter(getViewClassName(), "view")
				.returns(void.class)
				.build();
	}

	private MethodSpec getGetEditFragment() {
		return MethodSpec.methodBuilder("getEditFragment")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getFragmentClassName())
				.build();
	}

	private MethodSpec getPrepareDeleteBundle() {
		return MethodSpec.methodBuilder("prepareDeleteBundle")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getBundleClassName())
				.build();
	}

	private MethodSpec getGetCallback() {
		return MethodSpec.methodBuilder("getCallback")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
	}

	private MethodSpec getOnDialogClosed() {
		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
						.addModifiers(Modifier.PUBLIC)
						.addAnnotation(Override.class)
						.returns(void.class)
						.beginControlFlow("if ($N)", "successfullyDeleted")
						.addStatement("getFragmentManager().popBackStackImmediate()")
						.endControlFlow()
						.beginControlFlow("else")
						.addStatement("$T.makeText(getActivity(), $N(), $T.LENGTH_SHORT).show();", getToastClassName(), getGetResourceDeleteError(), getToastClassName())
						.endControlFlow()
						.build()
				).build();

		return MethodSpec.methodBuilder("onDialogClosed")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.returns(void.class)
				.addParameter(boolean.class, "successfullyDeleted", Modifier.FINAL)
				.addStatement("getActivity().runOnUiThread($L)", runnable)
				.build();
	}

	private MethodSpec getOnCreate() {
		return LifecycleMethods.getOnCreateFragment()
				.addStatement("setHasOptionsMenu(true)")
				.beginControlFlow("if ($N == null)", getSavedInstanceStateParam())
				.addStatement("$T $N = getArguments()", bundleClassName, "bundle")
				.addStatement("$N = $N.getString($S, $S)", resourceUrl, "bundle", "url", "")
				.addStatement("$N = $N.getString($S, $S)", mediaTyp, "bundle", "mediaType", "")
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("$N = $N.getString($S, $S)", resourceUrl, getSavedInstanceStateParam(), "url", "")
				.addStatement("$N = $N.getString($S, $S)", mediaTyp, getSavedInstanceStateParam(), "mediaType", "")
				.endControlFlow()
				.addStatement("$N()", getLoadResource())
				.build();
	}

	private MethodSpec getOnSaveInstanceState() {
		ParameterSpec outState = ParameterSpec.builder(getBundleClassName(), "outState").build();
		return MethodSpec.methodBuilder("onSaveInstanceState")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.returns(void.class)
				.addParameter(outState)
				.addStatement("$N.putString($S, $N)", outState, "url", resourceUrl)
				.addStatement("$N.putString($S, $N)", outState, "mediaType", mediaTyp)
				.addStatement("super.onSaveInstanceState($N)", outState)
				.build();
	}

	private MethodSpec getOnCreateView() {
		return MethodSpec.methodBuilder("onCreateView")
				.addAnnotation(Override.class)
				.addParameter(getLayoutInflaterClassName(), "inflater")
				.addParameter(getViewGroupClassName(), "container")
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(getViewClassName())
				.addStatement("$T $N = $N.inflate($N(), $N, false)", getViewClassName(), "view", "inflater", getGetLayout(), "container")
				.addStatement("$N($N)", getInitializeView(), "view")
				.addStatement("return $N", "view")
				.build();
	}

	private MethodSpec getOnCreateOptionsMenu() {
		MethodSpec.Builder method =  MethodSpec.methodBuilder("onCreateOptionsMenu");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addParameter(getMenuClassName(), "menu");
		method.addParameter(getMenuInflaterClassName(), "inflater");
		method.addStatement("$N.inflate($T.menu.$N, $N)", "inflater", rClassName, "detail_menu", "menu");

		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			method.addStatement("$T $N = $N.findItem($T.id.$N)", getMenuItemClassName(), "deleteItem", "menu", rClassName, "delete_item");
			method.addStatement("$N.setVisible($N != null)", "deleteItem", deleteLink);
		}
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			method.addStatement("$T $N = $N.findItem($T.id.$N)", getMenuItemClassName(), "editItem", "menu", rClassName, "edit_item");
			method.addStatement("$N.setVisible($N != null)", "editItem", updateLink);
		}
		return  method.build();
	}

	private MethodSpec getOnOptionsItemSelected() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("onOptionsItemSelected");
		method.addAnnotation(Override.class);
		method.addModifiers(Modifier.PUBLIC);
		method.returns(boolean.class);
		method.addParameter(getMenuItemClassName(), "item");
		method.addStatement("$T $N = $N.getItemId()", int.class, "i", "item");
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			method.beginControlFlow("if ($N == $T.id.$N)", "i", rClassName, "edit_item");
			method.addStatement("$T $N = new $T()", bundleClassName, "editBundle", bundleClassName);
			method.addStatement("$N.putString($S, $N.getHref())", "editBundle", "url", updateLink);
			method.addStatement("$N.putString($S, $N.getType())", "editBundle", "mediaType", updateLink);
			method.addStatement("$T $N = $N()", getFragmentClassName(), "fragment", getGetEditFragment());
			method.addStatement("$N.setArguments($N)", "fragment", "editBundle");
			method.addStatement("$T.replaceFragmentPopBackStack(getFragmentManager(), $N)", fragmentHandlerClassName, "fragment");
			method.endControlFlow();
		}
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			method.beginControlFlow(" if ($N == $T.id.$N)", "i", rClassName, "delete_item");
			method.addStatement("$T $N = $N()", bundleClassName, "bundle", getPrepareDeleteBundle());
			method.addStatement("$N.putString($S, $N.getHref())", "bundle", "url", deleteLink);
			method.addStatement("$T $N = new $T()", deleteDialogFragmentClassName, "deleteDialogFragment", deleteDialogFragmentClassName);
			method.addStatement("$N.setArguments($N)", "deleteDialogFragment", "bundle");
			method.addStatement("$N.setTargetFragment($T.this, 0)", "deleteDialogFragment", thisClassName);
			method.addStatement("$N.show(getFragmentManager(), null)", "deleteDialogFragment");
			method.endControlFlow();
		}
		method.addStatement("return super.onOptionsItemSelected($N)", "item");
		return method.build();
	}

	private MethodSpec getLoadResource() {
		return MethodSpec.methodBuilder("loadResource")
				.addModifiers(Modifier.PRIVATE).returns(void.class)
				.addStatement("$T $N = new $T(getActivity(), new $T().acceptHeader($N).url($N))",
						networkClientClassName, "client", networkClientClassName, networkRequestClassName, mediaTyp, resourceUrl)
				.addStatement("$N.sendRequest($N())", "client", getGetCallback())
				.build();
	}
}
