package de.fhws.applab.gemara.welling;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

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
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.superclass(getAppCompatActivityClassName())
				.addSuperinterfaces(getSuperInterfaces())
				.addModifiers(getClassModifier())
				.addFields(getFields())
				.addMethods(getMethods())
				.build();
		return JavaFile.builder(this.packageName, type).build();
	}
}
