package de.fhws.applab.gemara.welling.application.app.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import javax.lang.model.element.Modifier;

public class EditSpecificResourceFragment extends AbstractModelClass {

	private final String resourceName;
	private final AppResource appResource;
	private final ClassName rClassName;

	public EditSpecificResourceFragment(String packageName, AppResource appResource) {
		super(packageName + ".fragment", "Edit" + appResource.getResourceName() + "Fragment");
		this.appResource = appResource;
		this.resourceName = appResource.getResourceName();

		this.rClassName = ClassName.get(packageName, "R");
	}

	@Override
	public JavaFile javaFile() {
		return null;
	}


	private MethodSpec getGetLayout() {
		return MethodSpec.methodBuilder("getLayout")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.returns(int.class)
				.addStatement("return $T.layout.$N", rClassName, "fragment_edit_" + resourceName.toLowerCase())
				.build();
	}
}
