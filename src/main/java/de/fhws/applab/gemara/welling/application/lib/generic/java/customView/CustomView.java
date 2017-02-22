package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;

@SuppressWarnings("WeakerAccess")
public abstract class CustomView extends AbstractModelClass {

	protected final ParameterSpec defStyleAttr = ParameterSpec.builder(int.class, "defStyleAttr").build();
	private final ClassName superClass;

	public abstract Modifier[] addClassModifiers();
	public abstract List<MethodSpec> addConstructors();
	public abstract List<FieldSpec> addFields();
	public abstract MethodSpec getInitMethod();
	public abstract List<MethodSpec> addAdditionalMethods();

	public CustomView(String packageName, String className, ClassName superClass) {
		super(packageName, className);
		this.superClass = superClass;
	}

	protected MethodSpec getConstructorOne() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addStatement("super($N)", getContextParam())
				.addStatement("$N($N, null, 0)", getInitMethod(), getContextParam())
				.build();
		// @formatter:on
	}

	protected MethodSpec getConstructorTwo() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.addStatement("$N($N, $N, 0)", getInitMethod(), getContextParam(), getAttributeSetParam())
				.build();
		// @formatter:on
	}

	protected MethodSpec getConstructorThree() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), defStyleAttr)
				.addStatement("$N($N, $N, $N)", getInitMethod(), getContextParam(), getAttributeSetParam(), defStyleAttr)
				.build();
		// @formatter:on
	}

	protected List<MethodSpec> getStandardConstructors() {
		List<MethodSpec> constructors = new ArrayList<>();
		constructors.add(getConstructorOne());
		constructors.add(getConstructorTwo());
		constructors.add(getConstructorThree());

		return constructors;
	}

	protected MethodSpec.Builder getInitMethodSignature() {
		// @formatter:off
		return MethodSpec.methodBuilder("init")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(defStyleAttr);
		// @formatter:on
	}

	@Override
	public JavaFile javaFile() {
		TypeSpec.Builder type = TypeSpec.classBuilder(this.className);
		type.addModifiers(addClassModifiers());
		type.superclass(this.superClass);
		type.addFields(addFields());
		type.addMethods(addConstructors());
		if (getInitMethod() != null) {
			type.addMethod(getInitMethod());
		}
		type.addMethods(addAdditionalMethods());

		return JavaFile.builder(this.packageName, type.build()).build();
	}
}