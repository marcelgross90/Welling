package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.visitors.TitleVisitor;

import javax.lang.model.element.Modifier;

import java.util.Map;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getMenuClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getMenuInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getMenuItemClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;

public class DetailFragmentGenerator extends AbstractModelClass {

	private final DetailView detailView;
	private final AppDescription appDescription;
	private final StateHolder stateHolder;

	private final ClassName rClassName;
	private final ClassName specificResourceClassName;
	private final ClassName specificResourceDetailViewClassName;
	private final ClassName resourceDetailFragmentClassName;
	private final ClassName networkCallbackClassName;
	private final ClassName networkResponseClassName;
	private final ClassName linkClassName;


	public DetailFragmentGenerator(AppDescription appDescription, DetailView detailView, StateHolder stateHolder) {
		super(appDescription.getAppPackageName() + ".fragment", detailView.getResourceName() + "DetailFragment");

		this.detailView = detailView;
		this.appDescription = appDescription;
		this.stateHolder = stateHolder;

		this.resourceDetailFragmentClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.fragment", "DetailResourceFragment");
		this.rClassName = ClassName.get(appDescription.getAppPackageName(), "R");
		this.specificResourceDetailViewClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.customView", detailView.getResourceName() + "DetailView");
		this.specificResourceClassName = ClassName.get(appDescription.getLibPackageName() + ".specific.model",
				detailView.getResourceName());
		this.networkCallbackClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkCallback");
		this.networkResponseClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.network", "NetworkResponse");
		this.linkClassName = ClassName.get(appDescription.getLibPackageName() + ".generic.model", "Link");
	}

	@Override
	public JavaFile javaFile() {

		TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.className);
		typeSpec.superclass(resourceDetailFragmentClassName);
		typeSpec.addModifiers(Modifier.PUBLIC);
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			typeSpec.addMethod(getGetResourceDeleteError());
		}
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			typeSpec.addMethod(getGetEditFragment());
			typeSpec.addMethod(getPrepareBundle());
		}
		typeSpec.addMethod(getOnCreateOptionsMenu());
		typeSpec.addMethod(getGetLayout());
		typeSpec.addMethod(getInitializeView());
		typeSpec.addMethod(getGetCallback());
		typeSpec.addMethod(getSetUp());

		return JavaFile.builder(this.packageName, typeSpec.build()).build();
	}

	private MethodSpec getGetResourceDeleteError() {
		addString(replaceIllegalCharacters(detailView.getResourceName().toLowerCase()) + "_delete_error", "Could not delete " + detailView.getResourceName().toLowerCase());
		return MethodSpec.methodBuilder("getResourceDeleteError").addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.string.$N", rClassName, replaceIllegalCharacters(detailView.getResourceName().toLowerCase()) + "_delete_error")
				.build();
	}

	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "fragment_" + detailView.getResourceName().toLowerCase() + "_detail")
				.build();
	}

	private MethodSpec getInitializeView() {
		return MethodSpec.methodBuilder("initializeView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.addParameter(getViewClassName(), "view")
				.returns(void.class)
				.addStatement("$N = ($T) $N.findViewById($T.id.$N)", "resourceDetailView", specificResourceDetailViewClassName, "view", rClassName,
						"detail_view")
				.build();
	}

	private MethodSpec getGetEditFragment() {
		return MethodSpec.methodBuilder("getEditFragment")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(getFragmentClassName())
				.addStatement("return new $N()", "Edit" + detailView.getResourceName() + "Fragment")
				.build();
	}

	private MethodSpec getPrepareBundle() {
		TitleVisitor titleVisitor = new TitleVisitor(detailView.getResourceName());
		detailView.getTitle().accept(titleVisitor);

		return MethodSpec.methodBuilder("prepareBundle").addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
				.returns(getBundleClassName())
				.addStatement("$T $N = ($T) $N", specificResourceClassName, detailView.getResourceName().toLowerCase(),
						specificResourceClassName, "currentResource")
				.addStatement("$T $N = new $T()", getBundleClassName(), "bundle", getBundleClassName())
				.addStatement("$N.putString($S, $N)", "bundle", "name", titleVisitor.getTitle()).addStatement("return $N", "bundle")
				.build();
	}


	private MethodSpec getGetCallback() {
		TypeSpec callback = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(networkCallbackClassName)
				.addMethod(
						MethodSpec.methodBuilder("onFailure")
								.addAnnotation(Override.class)
								.addModifiers(Modifier.PUBLIC)
								.returns(void.class)
								.build())
				.addMethod(
						getOnSuccess())
				.build();

		return MethodSpec.methodBuilder("getCallback")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(networkCallbackClassName)
				.addStatement("return $L", callback)
				.build();
	}

	private MethodSpec getOnSuccess() {
		ParameterSpec response = ParameterSpec.builder(networkResponseClassName, "response").build();
		ParameterizedTypeName stringLinkMap = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class),
				linkClassName);

		TypeSpec runnable = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(Runnable.class)
				.addMethod(MethodSpec.methodBuilder("run").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class).addStatement("$N(($T) $N)", getSetUp(), specificResourceClassName, "currentResource").build())
				.build();

		MethodSpec.Builder builder = MethodSpec.methodBuilder("onSuccess");
		builder.addAnnotation(Override.class);
		builder.addModifiers(Modifier.PUBLIC);
		builder.returns(void.class);
		builder.addParameter(response);
		builder.addStatement("$N = $N.deserialize($N.getResponseReader(), $T.class)", "currentResource", "genson", response, specificResourceClassName);
		builder.addStatement("$T $N = $N.getLinkHeader()", stringLinkMap, "linkHeader", response);
		if (stateHolder.contains(StateHolder.StateType.DELETE)) {
			builder.addStatement("$N = $N.get(getActivity().getString($T.string.$N))", "deleteLink", "linkHeader", rClassName, appDescription.getAppRestAPI().getRestApi().get(StateHolder.StateType.DELETE + "_" + detailView.getResourceName()).getKey());
		}
		if (stateHolder.contains(StateHolder.StateType.PUT)) {
			builder.addStatement("$N = $N.get(getActivity().getString($T.string.$N))", "updateLink", "linkHeader", rClassName, appDescription.getAppRestAPI().getRestApi().get(StateHolder.StateType.PUT + "_" + detailView.getResourceName()).getKey());
		}
		builder.addStatement("getActivity().runOnUiThread($L)", runnable);
		return builder.build();
	}


//todo here
private MethodSpec getOnCreateOptionsMenu() {
	ParameterSpec menu = ParameterSpec.builder(getMenuClassName(), "menu").build();
	ParameterSpec inflater = ParameterSpec.builder(getMenuInflaterClassName(), "inflater").build();

	return MethodSpec.methodBuilder("onCreateOptionsMenu")
			.addModifiers(Modifier.PUBLIC).returns(void.class)
			.addAnnotation(Override.class)
			.addParameter(menu)
			.addParameter(inflater)
			.addStatement("$N.inflate($T.menu.detail_menu, $N)", "inflater", rClassName, menu)
			.addStatement("$T $N = $N.findItem($T.id.delete_item)", getMenuItemClassName(), "deleteItem", menu, rClassName)
			.addStatement("$T $N = $N.findItem($T.id.edit_item)", getMenuItemClassName(), "updateItem", menu, rClassName)
			.addStatement("$N.setVisible($N != null)", "deleteItem", "deleteLink")
			.addStatement("$N.setVisible($N != null)", "updateItem", "updateLink")
			.build();
}

	private MethodSpec getSetUp() {
		return MethodSpec.methodBuilder("setUp").addModifiers(Modifier.PRIVATE).returns(void.class)
				.addParameter(specificResourceClassName, detailView.getResourceName().toLowerCase()).addStatement("getActivity().invalidateOptionsMenu()")
				.addStatement("(($T) $N).setUpView($N)", specificResourceDetailViewClassName, "resourceDetailView",
						detailView.getResourceName().toLowerCase())
				.build();
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(key, value);
	}



}
