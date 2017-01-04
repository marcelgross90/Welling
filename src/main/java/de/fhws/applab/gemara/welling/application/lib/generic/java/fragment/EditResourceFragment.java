package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;
import java.util.List;

public class EditResourceFragment extends ResourceInputFragment {

	private final FieldSpec resourceEditLink;

	public EditResourceFragment(String packageName) {
		super(packageName, "EditResourceFragment");

		ClassName linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		this.resourceEditLink = FieldSpec.builder(linkClassName, "resourceEditLink", Modifier.PROTECTED).build();
	}

	@Override
	protected MethodSpec getOnCreate() {
		return  getOnCreateBuilder()
				.addStatement("$N()", getLoadResource())
				.build();

	}

	@Override
	protected MethodSpec getSaveResource() {
		return MethodSpec.methodBuilder("saveResource")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addStatement("$T $N = $N.getResource()", resourceClassName, "resource", inputView)
				.beginControlFlow("if ($N != null)", "resource")
				.addStatement("$T $N = $N.serialize($N)", String.class, "resourceString", genson, "resource")
				.addStatement("$T $N = new $T(getActivity(), new $T().url($N.getHref()).put($N, $N.getType()))",
						networkClientClassName, "client", networkClientClassName, networkRequestClassName, resourceEditLink, "resourceString", resourceEditLink)
				.addStatement("$N.sendRequest($N)", "client", getGetSaveCallBack())
				.endControlFlow()
				.build();
	}

	@Override
	protected List<MethodSpec> getAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getGetLoadCallBack());
		methods.add(getGetSaveCallBack());
		methods.add(getLoadResource());

		return methods;
	}

	@Override
	protected List<FieldSpec> getAdditionalFields() {
		List<FieldSpec> fields = new ArrayList<>();
		fields.add(resourceEditLink);
		return fields;
	}

	private MethodSpec getGetLoadCallBack() {
		return MethodSpec.methodBuilder("getLoadCallBack")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
	}

	private MethodSpec getGetSaveCallBack() {
		return MethodSpec.methodBuilder("getSaveCallBack")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
	}

	private MethodSpec getLoadResource() {
		return MethodSpec.methodBuilder("loadResource")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addStatement("$T $N = new $T(getActivity(), new $T().url($N).acceptHeader($N))", networkClientClassName, "client", networkClientClassName, networkRequestClassName, url, mediaType)
				.addStatement("$N.sendRequest($N)", "client", getGetLoadCallBack())
				.build();
	}
}
