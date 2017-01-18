package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.cardViews.visitor.TitleVisitorImpl;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class ListFragmentGenerator extends AbstractModelClass {

	private final String resourceName;
	private final AppResource appResource;

	private final ClassName rClassName;
	private final ClassName resourceListAdapterClassName;
	private final ClassName specificResourceListAdapterClassName;
	private final ClassName specificResourceDetailActivityClassName;
	private final ClassName thisClassName;
	private final ClassName networkCallBackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName specificResourceClassName;
	private final ClassName resourceClassName;
	private final ClassName linkClassName;
	private final ClassName resourceListFragmentClassName;

	private final FieldSpec specificResourceListAdapter;

	private final ParameterizedTypeName genericType;
	private final ParameterizedTypeName specificResourceList;
	private final ParameterizedTypeName resourceList;
	private final ParameterizedTypeName linkMap;

	public ListFragmentGenerator(String packageName, AppResource appResource, String appName) {
		super(packageName + ".fragment", appResource.getResourceName() + "ListFragment");
		this.resourceName = appResource.getResourceName();
		this.appResource = appResource;

		this.rClassName = ClassName.get(packageName, "R");
		this.resourceListAdapterClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.adapter", "ResourceListAdapter");
		this.specificResourceListAdapterClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.specific.adapter", resourceName + "ListAdapter");
		this.specificResourceDetailActivityClassName = ClassName.get(packageName, resourceName + "DetailActivity");
		this.thisClassName = ClassName.get(this.packageName, this.className);
		this.networkCallBackClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.network", "NetworkResponse");
		this.specificResourceClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.specific.model", resourceName);
		this.resourceClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.model", "Resource");
		this.linkClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.model", "Link");
		this.resourceListFragmentClassName = ClassName.get(packageName + "." + appName.toLowerCase() + "_lib.generic.fragment", "ResourceListFragment");

		this.specificResourceListAdapter = FieldSpec.builder(specificResourceListAdapterClassName, resourceName.toLowerCase() + "ListAdapter", Modifier.PRIVATE).build();

		this.specificResourceList = ParameterizedTypeName.get(ClassName.get(List.class), specificResourceClassName);
		this.resourceList = ParameterizedTypeName.get(ClassName.get(List.class), resourceClassName);
		this.genericType = ParameterizedTypeName.get(getGenericTypeClassName(), specificResourceList);
		this.linkMap = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), linkClassName);
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(resourceListFragmentClassName)
				.addField(specificResourceListAdapter)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getOnResourceClickWithView())
				.addMethod(getOnResourceClick())
				.addMethod(getGetCallBack())
				.addMethod(getGetAdapter())
				.addMethod(getGetFragment())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getOnResourceClickWithView() {
		TitleVisitorImpl visitor =  new TitleVisitorImpl();
		MethodSpec.Builder method = MethodSpec.methodBuilder("onResourceClickWithView");
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addAnnotation(Override.class);
		method.addParameter(resourceClassName, "resource");
		method.addParameter(getViewClassName(), "view");
		if (!appResource.isContainsImage()) {
			method.addCode("//not needed here\n");
		} else {
			method.addStatement("$T $N = ($T) $N", specificResourceClassName, resourceName.toLowerCase(), specificResourceClassName, "resource");
			method.addStatement("$T $N = new $T(getActivity(), $T.class)", getIntentClassName(), "intent", getIntentClassName(), specificResourceDetailActivityClassName);
			method.addStatement("$N.putExtra($S, $N.getSelf().getHref())", "intent", "selfUrl", resourceName.toLowerCase());
			method.addStatement("$N.putExtra($S, $N.getSelf().getType())", "intent", "mediaType", resourceName.toLowerCase());
			method.addStatement("$N.putExtra($S, $N)", "intent", "fullName", appResource.getAppDetailCardView().getTitle().getTitleForMethodSpec(visitor, appResource));

			method.beginControlFlow("if ($T.VERSION.SDK_INT >= $T.VERSION_CODES.LOLLIPOP)", getBuildClassName(), getBuildClassName());
			method.addStatement("$T $N = $T.makeSceneTransitionAnimation(getActivity(), $N, $S)", getActivityOptionsClassName(), "options", getActivityOptionsClassName(), "view", "pic");
			method.addStatement("getActivity().startActivity($N, $N.toBundle())", "intent", "options");
			method.endControlFlow();
			method.beginControlFlow("else");
			method.addStatement("getActivity().startActivity($N)", "intent");
			method.addStatement("getActivity().overridePendingTransition($T.anim.$N, $T.anim.$N)", rClassName, "fade_out", rClassName, "fade_in");
			method.endControlFlow();

		}

		return method.build();
	}

	private MethodSpec getOnResourceClick() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("onResourceClick");
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addAnnotation(Override.class);
		method.addParameter(resourceClassName, "resource");
		if (appResource.isContainsImage()) {
			method.addCode("//not needed here\n");
		} else {
			//todo
		}
		return method.build();
	}

	private MethodSpec getGetCallBack() {
		TypeSpec onFailure = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(MethodSpec.methodBuilder("run")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addStatement("$T.makeText(getActivity(), $T.string.$N, $T.LENGTH_SHORT).show()",
						getToastClassName(), rClassName, "load_" + resourceName.toLowerCase() + "_error", getToastClassName())
				.build()).build();

		TypeSpec onSuccess = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(MethodSpec.methodBuilder("run")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.returns(void.class)
						.addStatement("setHasOptionsMenu($N != null)", "createNewResourceLink")
						.addStatement("showProgressBar(false)")
						.addStatement("$N().addResource($N)", getGetAdapter(), "resources")
						.build()).build();

		MethodSpec onSuccessMethod = MethodSpec.methodBuilder("onSuccess")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(networkResponseClassName, "response")
				.addStatement("final $T $N = $N.deserialize($N.getResponseReader(), new $T(){})", specificResourceList,
						resourceName.toLowerCase() + "s", "genson", "response", genericType)
				.addStatement("final $T $N = new $T<>()", resourceList, "resources", ClassName.get(ArrayList.class))
				.beginControlFlow("for ($T $N : $N)", specificResourceClassName, resourceName.toLowerCase(),
						resourceName.toLowerCase() + "s")
				.addStatement("$N.add($N)", "resources", resourceName.toLowerCase())
				.endControlFlow()
				.addStatement("$T $N = $N.getLinkHeader()", linkMap, "linkHeader", "response")
				.addStatement("$T $N = $N.get(getActivity().getString($T.string.$N))",
						linkClassName, "nextLink", "linkHeader", rClassName, "rel_type_next")
				.addStatement("$N = $N.get(getActivity().getString($T.string.$N))",
						"createNewResourceLink", "linkHeader", rClassName, "rel_type_create_new_" + resourceName.toLowerCase())
				.beginControlFlow("if ($N != null)", "nextLink")
				.addStatement("$N = $N.getHref()", "nextUrl", "nextLink")
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("$N = \"\"", "nextUrl")
				.endControlFlow()
				.addStatement("getActivity().runOnUiThread($L)", onSuccess).returns(void.class).build();

		TypeSpec networkCallback = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(MethodSpec.methodBuilder("onFailure")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.addStatement("getActivity().runOnUiThread($L)", onFailure)
						.returns(void.class)
						.build())
				.addMethod(onSuccessMethod)
				.build();


		return MethodSpec.methodBuilder("getCallBack")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(networkCallBackClassName)
				.addStatement("return $L", networkCallback)
				.build();

	}

	private MethodSpec getGetAdapter() {
		return MethodSpec.methodBuilder("getAdapter")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(resourceListAdapterClassName)
				.beginControlFlow("if ($N == null)", specificResourceListAdapter)
				.addStatement("$N = new $T($T.this)", specificResourceListAdapter, specificResourceListAdapterClassName, thisClassName)
				.endControlFlow()
				.addStatement("return $N", specificResourceListAdapter)
				.build();

	}

	private MethodSpec getGetFragment() {
		//todo add statement for new resource
		return MethodSpec.methodBuilder("getFragment")
				.addModifiers(Modifier.PROTECTED)
				.returns(getFragmentClassName())
				.addAnnotation(Override.class)
				/*.addStatement("return new $T()", newLecturerFragmentClassName)*/
				.addStatement("return null")
				.build();
	}
}
