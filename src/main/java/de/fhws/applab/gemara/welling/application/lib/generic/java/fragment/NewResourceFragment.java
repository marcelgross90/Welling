package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewResourceFragment extends ResourceInputFragment {

	public NewResourceFragment(String packageName) {
		super(packageName, "NewResourceFragment");
	}

	@Override
	protected MethodSpec getOnCreate() {
		return getOnCreateBuilder().build();
	}

	@Override
	protected MethodSpec getSaveResource() {
		return MethodSpec.methodBuilder("saveResource")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addStatement("$T $N = $N.getResource()", resourceClassName, "resource", inputView)
				.beginControlFlow("if ($N != null)", "resource")
				.addStatement("$T $N = $N.serialize($N)", String.class, "resourceString", genson, "resource")
				.addStatement("$T $N = new $T(getActivity(), new $T().url($N).post($N, $N))",
						networkClientClassName, "client", networkClientClassName, networkRequestClassName, url, "resourceString", mediaType)
				.addStatement("$N.sendRequest($N)", "client", getGetCallback())
				.endControlFlow()
				.build();
	}

	@Override
	protected List<MethodSpec> getAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getGetCallback());
		return methods;
	}

	@Override
	protected List<FieldSpec> getAdditionalFields() {
		return Collections.emptyList();
	}

	private MethodSpec getGetCallback() {
		return MethodSpec.methodBuilder("getCallback")
				.addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
				.returns(networkCallbackClassName)
				.build();
	}
}
