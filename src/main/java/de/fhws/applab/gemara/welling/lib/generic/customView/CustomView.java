package de.fhws.applab.gemara.welling.lib.generic.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;
public abstract class CustomView extends AbstractModelClass {

	protected final ParameterSpec defStyleAttr = ParameterSpec.builder(int.class, "defStyleAttr").build();
	protected final ClassName superClass;
	protected final ClassName rClass;

	public CustomView(String packageName, String className, ClassName superClass) {
		super(packageName + ".generic.customView", className);
		this.superClass = superClass;
		this.rClass = ClassName.get(packageName, "R");
	}

	protected MethodSpec getConstructorOne() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addStatement("super($N)", getContextParam())
				.addStatement("$N($N, null, 0)", getInitMethod(), getContextParam())
				.build();
	}

	protected MethodSpec getConstructorTwo() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.addStatement("$N($N, $N, 0)", getInitMethod(), getContextParam(), getAttributeSetParam())
				.build();
	}

	protected MethodSpec getConstructorThree() {

		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), defStyleAttr)
				.addStatement("$N($N, $N, $N)", getInitMethod(), getContextParam(), getAttributeSetParam(), defStyleAttr)
				.build();
	}

	protected List<MethodSpec> getStandardConstructors() {
		List<MethodSpec> constructors = new ArrayList<>();
		constructors.add(getConstructorOne());
		constructors.add(getConstructorTwo());
		constructors.add(getConstructorThree());

		return constructors;
	}

	protected MethodSpec.Builder getInitMethodSignature() {
		return MethodSpec.methodBuilder("init")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(defStyleAttr);
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(addClassModifiers())
				.superclass(this.superClass)
				.addFields(addFields())
				.addMethods(addConstructors())
				.addMethod(getInitMethod())
				.addMethods(addAdditionalMethods())
				.build();
		return JavaFile.builder(this.packageName, type).build();
	}

	public abstract Modifier[] addClassModifiers();
	public abstract List<MethodSpec> addConstructors();
	public abstract List<FieldSpec> addFields();
	public abstract MethodSpec getInitMethod();
	public abstract List<MethodSpec> addAdditionalMethods();
}
