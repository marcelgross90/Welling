package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;

/**
 * This class is needed, because of the problem with the self-links of sub-resources.
 * It provides a workaround to access the template-url in the link-header.
 */

public class ListFragmentSubResourceGenerator extends ListFragmentGenerator {

	private final FieldSpec detailTemplateUrl;

	public ListFragmentSubResourceGenerator(StateHolder stateHolder, AppDescription appDescription, CardView cardView) {
		super(stateHolder, appDescription, cardView);

		this.detailTemplateUrl = FieldSpec.builder(String.class, "detail" + cardView.getResourceName() + "TemplateUrl", Modifier.PRIVATE).build();

	}

	@Override
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

			method.addStatement("$N.putString($S, $N.replace($S, $T.valueOf($N.getId())))", "bundle", "url", detailTemplateUrl, "{id}", String.class, resourceName.toLowerCase());

			method.addStatement("$N.putString($S, $N.getSelf().getType())", "bundle", "mediaType", resourceName.toLowerCase());
			method.addStatement("$N.setArguments($N)", "fragment", "bundle");
			method.addStatement("$T.replaceFragment(getFragmentManager(), $N)", fragmentHandlerClassName, "fragment");
		}
		return method.build();
	}

	@Override
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
		onSuccessMethod.addStatement("$T $N = $N.get(getActivity().getString($T.string.$N))", linkClassName, "one" + resourceName + "Link", "linkHeader", rClassName, appDescription.getAppRestAPI().getRestApi().get(StateHolder.StateType.GET_SINGLE + "_" + resourceName).getKey());

		onSuccessMethod.beginControlFlow("if ($N != null)", "one" + resourceName + "Link");
		onSuccessMethod.addStatement("$N = $N.getHref()", detailTemplateUrl, "one" + resourceName + "Link");
		onSuccessMethod.endControlFlow();
		onSuccessMethod.beginControlFlow("else");
		onSuccessMethod.addStatement("$N = $S", detailTemplateUrl, "");
		onSuccessMethod.endControlFlow();

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

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(resourceListFragmentClassName)
				.addField(specificResourceListAdapter)
				.addField(detailTemplateUrl)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getOnResourceClickWithView())
				.addMethod(getOnResourceClick())
				.addMethod(getGetCallBack())
				.addMethod(getGetAdapter())
				.addMethod(getGetFragment())
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}


}
