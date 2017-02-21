package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.visitors.ContainsImageVisitor;
import de.fhws.applab.gemara.welling.visitors.TitleVisitor;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

/**
 * If problem with the self resources is fixed you can delete @see de.fhws.applab.gemara.welling.application.app.java.fragment.ListFragmentSubResourceGenerator.
 * You can also change all protected modifier to private.
 */

public class ListFragmentGenerator extends AbstractModelClass {

	private final CardView cardView;
	protected final AppDescription appDescription;
	protected final String resourceName;
	protected final StateHolder stateHolder;

	private final ClassName resourceListAdapterClassName;
	private final ClassName specificResourceListAdapterClassName;
	private final ClassName thisClassName;
	private final ClassName newResourceFragmentClassName;
	protected final ClassName resourceListFragmentClassName;
	protected final ClassName rClassName;
	protected final ClassName linkClassName;
	protected final ClassName resourceClassName;
	protected final ClassName specificResourceClassName;
	protected final ClassName specificResourceDetailClassName;
	protected final ClassName networkCallBackClassName;
	protected final ClassName fragmentHandlerClassName;
	protected final ClassName networkResponseClassName;

	protected final FieldSpec specificResourceListAdapter;

	protected final ParameterizedTypeName linkMap;
	protected final ParameterizedTypeName resourceList;
	protected final ParameterizedTypeName genericType;
	protected final ParameterizedTypeName specificResourceList;

	protected boolean containsImage = false;

	public ListFragmentGenerator(StateHolder stateHolder, AppDescription appDescription, CardView cardView) {
		super(appDescription.getAppPackageName() + ".fragment", cardView.getResourceName() + "ListFragment");
		this.appDescription = appDescription;
		this.stateHolder = stateHolder;
		this.resourceName = cardView.getResourceName();
		this.cardView = cardView;
		String packageName = appDescription.getAppPackageName();

		ContainsImageVisitor visitor = new ContainsImageVisitor();
		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			resourceViewAttribute.accept(visitor);
			containsImage = visitor.isContainsImage();
			if (containsImage) {
				break;
			}
		}

		this.rClassName = ClassName.get(packageName, "R");
		this.resourceListAdapterClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.adapter", "ResourceListAdapter");
		this.specificResourceListAdapterClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.adapter", resourceName + "ListAdapter");
		if(containsImage) {
			this.specificResourceDetailClassName = ClassName.get(packageName, resourceName + "DetailActivity");
		} else {
			this.specificResourceDetailClassName = ClassName.get(packageName + ".fragment", resourceName + "DetailFragment");
		}
		this.thisClassName = ClassName.get(this.packageName, this.className);
		this.networkCallBackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");
		this.specificResourceClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.model", resourceName);
		this.resourceClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Resource");
		this.linkClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Link");
		this.resourceListFragmentClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.fragment", "ResourceListFragment");
		this.newResourceFragmentClassName = ClassName.get(this.packageName, "New" + resourceName + "Fragment");
		this.fragmentHandlerClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.util", "FragmentHandler");

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

	protected MethodSpec getOnResourceClickWithView() {
		TitleVisitor titleVisitor = new TitleVisitor(cardView.getResourceName());
		cardView.getTitle().accept(titleVisitor);

		MethodSpec.Builder method = MethodSpec.methodBuilder("onResourceClickWithView");
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addAnnotation(Override.class);
		method.addParameter(resourceClassName, "resource");
		method.addParameter(getViewClassName(), "view");

		if (!stateHolder.contains(StateHolder.StateType.GET_SINGLE)) {
			return method.addCode("//no detailView\n").build();
		}


		if (!containsImage) {
			method.addCode("//not needed here\n");
		} else {
			method.addStatement("$T $N = ($T) $N", specificResourceClassName, resourceName.toLowerCase(), specificResourceClassName, "resource");
			method.addStatement("$T $N = new $T(getActivity(), $T.class)", getIntentClassName(), "intent", getIntentClassName(),
					specificResourceDetailClassName);
			method.addStatement("$N.putExtra($S, $N.getSelf().getHref())", "intent", "selfUrl", resourceName.toLowerCase());
			method.addStatement("$N.putExtra($S, $N.getSelf().getType())", "intent", "mediaType", resourceName.toLowerCase());
			method.addStatement("$N.putExtra($S, $N)", "intent", "fullName", titleVisitor.getTitle());

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

	protected MethodSpec getOnResourceClick() {

		MethodSpec.Builder method = MethodSpec.methodBuilder("onResourceClick");
		method.addModifiers(Modifier.PUBLIC);
		method.returns(void.class);
		method.addAnnotation(Override.class);
		method.addParameter(resourceClassName, "resource");

		if (!stateHolder.contains(StateHolder.StateType.GET_SINGLE)) {
			return method.addCode("//no detailView\n").build();
		}

		if (containsImage) {
			method.addCode("//not needed here\n");
		} else {
			method.addStatement("$T $N = ($T) $N", specificResourceClassName, resourceName.toLowerCase(), specificResourceClassName, "resource");
			method.addStatement("$T $N = new $T()", getFragmentClassName(), "fragment", specificResourceDetailClassName);
			method.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName());
			method.addStatement("$N.putString($S, $N.getSelf().getHref())", "bundle", "url", resourceName.toLowerCase());
			method.addStatement("$N.putString($S, $N.getSelf().getType())", "bundle", "url", resourceName.toLowerCase());
			method.addStatement("$N.setArguments($N)", "fragment", "bundle");
			method.addStatement("$T.replaceFragment(getFragmentManager(), $N)", fragmentHandlerClassName, "fragment");
		}
		return method.build();
	}

	protected MethodSpec getGetCallBack() {
		TypeSpec onFailure = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(MethodSpec.methodBuilder("run")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addStatement("$T.makeText(getActivity(), $T.string.$N, $T.LENGTH_SHORT).show()",
						getToastClassName(), rClassName, "load_" + replaceIllegalCharacters(resourceName.toLowerCase()) + "_error", getToastClassName())
				.build()).build();

		TypeSpec networkCallback = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallBackClassName)
				.addMethod(MethodSpec.methodBuilder("onFailure")
						.addAnnotation(Override.class)
						.addModifiers(Modifier.PUBLIC)
						.addStatement("getActivity().runOnUiThread($L)", onFailure)
						.returns(void.class)
						.build())
				.addMethod(getOnSuccess())
				.build();


		return MethodSpec.methodBuilder("getCallBack")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(networkCallBackClassName)
				.addStatement("return $L", networkCallback)
				.build();

	}

	protected MethodSpec getOnSuccess() {
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

		MethodSpec.Builder onSuccessMethod = MethodSpec.methodBuilder("onSuccess");
		onSuccessMethod.addAnnotation(Override.class);
		onSuccessMethod.addModifiers(Modifier.PUBLIC);
		onSuccessMethod.addParameter(networkResponseClassName, "response");
		onSuccessMethod.addStatement("final $T $N = $N.deserialize($N.getResponseReader(), new $T(){})", specificResourceList,
						resourceName.toLowerCase() + "s", "genson", "response", genericType);
		onSuccessMethod.addStatement("final $T $N = new $T<>()", resourceList, "resources", ClassName.get(ArrayList.class));
		onSuccessMethod.beginControlFlow("for ($T $N : $N)", specificResourceClassName, resourceName.toLowerCase(),
				resourceName.toLowerCase() + "s");
		onSuccessMethod.addStatement("$N.add($N)", "resources", resourceName.toLowerCase());
		onSuccessMethod.endControlFlow();
		onSuccessMethod.addStatement("$T $N = $N.getLinkHeader()", linkMap, "linkHeader", "response");
		onSuccessMethod.addStatement("$T $N = $N.get(getActivity().getString($T.string.$N))",
						linkClassName, "nextLink", "linkHeader", rClassName, appDescription.getAppRestAPI().getRestApi().get("next").getKey());
		if (stateHolder.contains(StateHolder.StateType.POST)) {

			onSuccessMethod.addStatement("$N = $N.get(getActivity().getString($T.string.$N))",
					"createNewResourceLink", "linkHeader", rClassName, appDescription.getAppRestAPI().getRestApi().get(StateHolder.StateType.POST + "_" + resourceName).getKey());
		}
		onSuccessMethod.beginControlFlow("if ($N != null)", "nextLink");
		onSuccessMethod.addStatement("$N = $N.getHref()", "nextUrl", "nextLink");
		onSuccessMethod.endControlFlow();
		onSuccessMethod.beginControlFlow("else");
		onSuccessMethod.addStatement("$N = \"\"", "nextUrl");
		onSuccessMethod.endControlFlow();
		onSuccessMethod.addStatement("getActivity().runOnUiThread($L)", onSuccess).returns(void.class);


		return onSuccessMethod.build();
	}

	protected MethodSpec getGetAdapter() {
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

	protected MethodSpec getGetFragment() {
		if (stateHolder.contains(StateHolder.StateType.POST)) {
			return MethodSpec.methodBuilder("getFragment")
					.addModifiers(Modifier.PROTECTED)
					.returns(getFragmentClassName())
					.addAnnotation(Override.class)
					.addStatement("return new $T()", newResourceFragmentClassName)
					.build();
		} else {return MethodSpec.methodBuilder("getFragment")
				.addModifiers(Modifier.PROTECTED)
				.returns(getFragmentClassName())
				.addAnnotation(Override.class)
				.addStatement("return null")
				.build();
		}
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}

}
