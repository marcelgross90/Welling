package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;
import de.fhws.applab.gemara.welling.androidMethods.LifecycleMethods;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getGensonClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getLinearLayoutManagerClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getMenuClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getMenuInflaterClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getMenuItemClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getProgressBarClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getRecyclerViewClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getSavedInstanceStateParam;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getViewGroupClassName;

public class ResourceListFragment extends AbstractModelClass {

	private final ClassName progressBarClassName;
	private final ClassName rClassName;
	private final ClassName scrollListenerClassName;
	private final ClassName onScrollListenerClassName;
	private final ClassName resourceClassName;
	private final ClassName fragmentHandlerClassName;
	private final ClassName networkCallbackClassName;
	private final ClassName networkClientClassName;
	private final ClassName networkRequestClassName;
	private final ClassName resourceListAdapterClassName;
	private final ClassName onResourceClickListenerClassName;

	private final FieldSpec genson;
	private final FieldSpec progressBar;
	private final FieldSpec url;
	private final FieldSpec mediaType;
	private final FieldSpec nextUrl;
	private final FieldSpec createNewResourceLink;

	public ResourceListFragment(String packageName) {
		super(packageName + ".generic.fragment", "ResourceListFragment");

		this.rClassName = ClassName.get(packageName, "R");
		this.progressBarClassName = getProgressBarClassName();
		this.scrollListenerClassName = ClassName.get(packageName + ".generic.util", "ScrollListener");
		this.onScrollListenerClassName = ClassName.get(packageName + ".generic.util.ScrollListener", "OnScrollListener");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		this.fragmentHandlerClassName = ClassName.get(packageName + ".generic.util", "FragmentHandler");
		this.networkCallbackClassName = ClassName.get(packageName + ".generic.network", "NetworkCallback");
		this.networkClientClassName = ClassName.get(packageName + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(packageName + ".generic.network", "NetworkRequest");
		this.resourceListAdapterClassName = ClassName.get(packageName + ".generic.adapter", "ResourceListAdapter");
		this.onResourceClickListenerClassName = ClassName.get(packageName + ".generic.adapter.ResourceListAdapter", "OnResourceClickListener");
		ClassName gensonBuilderClassName = ClassName.get(packageName + ".generic.util", "GensonBuilder");
		ClassName linkClassName = ClassName.get(packageName + ".generic.model", "Link");

		this.genson = FieldSpec.builder(getGensonClassName(), "genson", Modifier.PROTECTED, Modifier.FINAL).initializer("new $T().getDateFormatter()",
				gensonBuilderClassName).build();
		this.progressBar = FieldSpec.builder(progressBarClassName, "progressBar", Modifier.PRIVATE).build();
		this.url = FieldSpec.builder(String.class, "url", Modifier.PRIVATE).build();
		this.mediaType = FieldSpec.builder(String.class, "mediaType", Modifier.PRIVATE).build();
		this.nextUrl = FieldSpec.builder(String.class, "nextUrl", Modifier.PROTECTED).build();
		this.createNewResourceLink = FieldSpec.builder(linkClassName, "createNewResourceLink", Modifier.PROTECTED).build();
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.superclass(getFragmentClassName())
				.addSuperinterface(onResourceClickListenerClassName)
				.addField(genson)
				.addField(progressBar)
				.addField(url)
				.addField(mediaType)
				.addField(nextUrl)
				.addField(createNewResourceLink)
				.addMethod(getOnCreate())
				.addMethod(getOnCreateView())
				.addMethod(getOnResourceClickWithView())
				.addMethod(getOnResourceClick())
				.addMethod(getOnCreateOptionsMenu())
				.addMethod(getOnOptionsItemSelected())
				.addMethod(getShowProgressBar())
				.addMethod(getLoadResources())
				.addMethod(getGetCallBack())
				.addMethod(getGetAdapter())
				.addMethod(getGetFragment())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getOnCreate() {
		return LifecycleMethods.getOnCreateFragment()
				.addStatement("$T $N = getArguments()", getBundleClassName(), "bundle")
				.addStatement("$N = $N.getString($S, $S)", url, "bundle", "url", "")
				.addStatement("$N = $N.getString($S, $S)", mediaType, "bundle", "mediaType", "")
				.build();
	}

	private MethodSpec getOnCreateView() {
		TypeSpec scrollListener = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(onScrollListenerClassName)
				.addMethod(
						MethodSpec.methodBuilder("load")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.returns(void.class)
						.beginControlFlow("if ($N != null && !$N.isEmpty())", nextUrl, nextUrl)
						.addStatement("$N($N)", getLoadResources(), nextUrl)
						.endControlFlow()
						.build()
				).build();

		return MethodSpec.methodBuilder("onCreateView")
				.addAnnotation(Override.class)
				.addParameter(getLayoutInflaterClassName(), "inflater")
				.addParameter(getViewGroupClassName(), "container")
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC)
				.returns(getViewClassName())
				.addStatement("$T $N = $N.inflate($T.layout.$N, $N, false)", getViewClassName(), "view", "inflater", rClassName,
						"fragment_resource_list", "container")
				.addStatement("$N = ($T) $N.findViewById($T.id.$N)", progressBar, progressBarClassName, "view", rClassName, "progressBar")
				.addStatement("$T $N = ($T) $N.findViewById($T.id.$N)", getRecyclerViewClassName(), "recyclerView", getRecyclerViewClassName(), "view", rClassName, "resource_recycler_view")
				.addStatement("$T $N = new $T(getContext())", getLinearLayoutManagerClassName(), "linearLayoutManager", getLinearLayoutManagerClassName())
				.addStatement("$N.setLayoutManager($N)", "recyclerView", "linearLayoutManager")
				.addStatement("$N.setAdapter($N())", "recyclerView", getGetAdapter())
				.addStatement("$N.addOnScrollListener(new $T($N, $L))", "recyclerView", scrollListenerClassName, "linearLayoutManager", scrollListener)
				.addStatement("$N($N)", getLoadResources(), url)
				.addStatement("return $N", "view")
				.build();
	}

	private MethodSpec getOnResourceClickWithView() {
		return MethodSpec.methodBuilder("onResourceClickWithView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.returns(void.class)
				.addParameter(resourceClassName, "resource")
				.addParameter(getViewClassName(), "view")
				.build();
	}

	private MethodSpec getOnResourceClick() {
		return MethodSpec.methodBuilder("onResourceClick")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.returns(void.class)
				.addParameter(resourceClassName, "resource")
				.build();
	}

	private MethodSpec getOnCreateOptionsMenu() {
		return MethodSpec.methodBuilder("onCreateOptionsMenu")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(getMenuClassName(), "menu")
				.addParameter(getMenuInflaterClassName(), "inflater")
				.addStatement("$N.inflate($T.menu.$N, $N)", "inflater", rClassName, "list_menu", "menu")
				.build();
	}

	private MethodSpec getOnOptionsItemSelected() {
		return MethodSpec.methodBuilder("onOptionsItemSelected")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(boolean.class)
				.addParameter(getMenuItemClassName(), "item")
				.beginControlFlow("if ($N.getItemId() == $T.id.$N)", "item", rClassName, "add")
				.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName())
				.beginControlFlow("if ($N != null)", createNewResourceLink)
				.addStatement("$N.putString($S, $N.getHref())", "bundle", "url", createNewResourceLink)
				.addStatement("$N.putString($S, $N.getType())", "bundle", "mediaType", createNewResourceLink)
				.endControlFlow()
				.addStatement("$T $N = $N()", getFragmentClassName(), "fragment", getGetFragment())
				.addStatement("$N.setArguments($N)", "fragment", "bundle")
				.addStatement("$T.replaceFragment(getFragmentManager(), $N)", fragmentHandlerClassName, "fragment")
				.endControlFlow()
				.addStatement("return super.onOptionsItemSelected($N)", "item")
				.build();
	}

	private MethodSpec getShowProgressBar() {
		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(
						MethodSpec.methodBuilder("run")
						.addModifiers(Modifier.PUBLIC)
						.returns(void.class)
						.addAnnotation(Override.class)
						.addStatement("$N.setVisibility($N ? $T.VISIBLE : $T.INVISIBLE)", progressBar, "show", getViewClassName(), getViewClassName())
						.build()
				).build();

		return MethodSpec.methodBuilder("showProgressBar")
				.addModifiers(Modifier.PROTECTED)
				.returns(void.class)
				.addParameter(boolean.class, "show", Modifier.FINAL)
				.beginControlFlow("if (isAdded())")
				.addStatement("getActivity().runOnUiThread($L)", runnable)
				.endControlFlow()
				.build();
	}

	private MethodSpec getLoadResources() {
		return MethodSpec.methodBuilder("loadResources")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addParameter(String.class, "url")
				.addStatement("$N(true)", getShowProgressBar())
				.addStatement("$T $N = new $T(getActivity(), new $T().url($N).acceptHeader($N))",
						networkClientClassName, "client", networkClientClassName, networkRequestClassName, url, mediaType)
				.addStatement("$N.sendRequest($N())", "client", getGetCallBack())
				.build();
	}

	private MethodSpec getGetCallBack() {
		return MethodSpec.methodBuilder("getCallBack")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
	}

	private MethodSpec getGetAdapter() {
		return MethodSpec.methodBuilder("getAdapter")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(resourceListAdapterClassName)
				.build();
	}

	private MethodSpec getGetFragment() {
		return MethodSpec.methodBuilder("getFragment")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(getFragmentClassName())
				.build();
	}
}
