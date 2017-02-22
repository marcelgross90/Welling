package de.fhws.applab.gemara.welling.application.androidSpecifics;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAppCompatActivityClassName;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractActivityClass extends AbstractModelClass {

	public AbstractActivityClass(String packageName, String className) {
		super(packageName, className);
	}

	protected abstract List<ClassName> getSuperInterfaces();
	protected abstract Modifier[] getClassModifier();
	protected abstract List<FieldSpec> getFields();
	protected abstract List<MethodSpec> getMethods();

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(getAppCompatActivityClassName())
				.addSuperinterfaces(getSuperInterfaces())
				.addModifiers(getClassModifier())
				.addFields(getFields())
				.addMethods(getMethods())
				.build();
		// @formatter:on
		return JavaFile.builder(this.packageName, type).build();
	}
}
