package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.application.androidSpecifics.LifecycleMethods;

import javax.lang.model.element.Modifier;

import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBundleClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getFragmentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getGensonClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getMenuClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getMenuInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getMenuItemClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getSavedInstanceStateParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getViewGroupClassName;

public abstract class ResourceInputFragment extends AbstractModelClass {

	protected final ClassName networkCallbackClassName;
	protected final ClassName networkClientClassName;
	protected final ClassName networkRequestClassName;
	protected final ClassName resourceClassName;
	private final ClassName rClassName;

	protected final FieldSpec genson;
	protected final FieldSpec inputView;
	protected final FieldSpec url;
	protected final FieldSpec mediaType;

	public ResourceInputFragment(String packageName, String className) {
		super(packageName + ".generic.fragment", className);

		this.rClassName = ClassName.get(packageName, "R");
		this.networkCallbackClassName = ClassName.get(packageName + ".generic.network", "NetworkCallback");
		this.networkClientClassName = ClassName.get(packageName + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(packageName + ".generic.network", "NetworkRequest");
		this.resourceClassName = ClassName.get(packageName + ".generic.model", "Resource");
		ClassName resourceInputViewClassName = ClassName.get(packageName + ".generic.customView", "ResourceInputView");
		ClassName gensonBuilderClassName = ClassName.get(packageName + ".generic.util", "GensonBuilder");

		this.genson = FieldSpec.builder(getGensonClassName(), "genson", Modifier.PROTECTED, Modifier.FINAL).initializer("new $T().getDateFormatter()",
				gensonBuilderClassName).build();
		this.inputView = FieldSpec.builder(resourceInputViewClassName, "inputView", Modifier.PROTECTED).build();
		this.url = FieldSpec.builder(String.class, "url", Modifier.PRIVATE).build();
		this.mediaType = FieldSpec.builder(String.class, "mediaType", Modifier.PRIVATE).build();
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.superclass(getFragmentClassName())
				.addField(genson)
				.addField(inputView)
				.addField(url)
				.addField(mediaType)
				.addFields(getAdditionalFields())
				.addMethod(getGetLayout())
				.addMethod(getInitializeView())
				.addMethod(getOnCreate())
				.addMethod(getOnCreateView())
				.addMethod(getOnCreateOptionsMenu())
				.addMethod(getOnOptionsItemSelected())
				.addMethod(getSaveResource())
				.addMethods(getAdditionalMethods())
				.build();

		return JavaFile.builder(this.packageName, type).build();
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

	protected MethodSpec.Builder getOnCreateBuilder() {
		return LifecycleMethods.getOnCreateFragment()
				.addStatement("setHasOptionsMenu(true)")
				.beginControlFlow("if ($N == null)", getSavedInstanceStateParam())
				.addStatement("$T $N = getArguments()", getBundleClassName(), "bundle")
				.addStatement("$N = $N.getString($S, $S)", url, "bundle", "url", "")
				.addStatement("$N = $N.getString($S, $S)", mediaType, "bundle", "mediaType", "")
				.endControlFlow();
	}


	private MethodSpec getOnCreateView() {
		return MethodSpec.methodBuilder("onCreateView")
				.addAnnotation(Override.class)
				.addParameter(getLayoutInflaterClassName(), "inflater")
				.addParameter(getViewGroupClassName(), "container")
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC)
				.returns(getViewClassName())
				.addStatement("$T $N = $N.inflate($N(), $N, false)", getViewClassName(), "view", "inflater", getGetLayout(), "container")
				.addStatement("$N($N)", getInitializeView(), "view")
				.addStatement("return $N", "view")
				.build();
	}

	private MethodSpec getOnCreateOptionsMenu() {
		return MethodSpec.methodBuilder("onCreateOptionsMenu")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(getMenuClassName(), "menu")
				.addParameter(getMenuInflaterClassName(), "inflater")
				.addStatement("$N.inflate($T.menu.$N, $N)", "inflater", rClassName, "save_menu", "menu")
				.build();
	}

	private MethodSpec getOnOptionsItemSelected() {
		return MethodSpec.methodBuilder("onOptionsItemSelected")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(boolean.class)
				.addParameter(getMenuItemClassName(), "item")
				.beginControlFlow("if ($N.getItemId() == $T.id.$N)", "item", rClassName, "saveItem")
				.addStatement("$N()", getSaveResource())
				.endControlFlow()
				.addStatement("return super.onOptionsItemSelected($N)", "item")
				.build();
	}

	protected abstract MethodSpec getOnCreate();

	protected abstract MethodSpec getSaveResource();

	protected abstract List<MethodSpec> getAdditionalMethods();

	protected abstract List<FieldSpec> getAdditionalFields();
}
