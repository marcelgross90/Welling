package de.fhws.applab.gemara.welling.application.lib.generic.java.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Link extends AbstractModelClass {

	private final ClassName thisClass;

	private final ParameterizedTypeName map = ParameterizedTypeName
			.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(String.class));

	private final FieldSpec hrefField = FieldSpec.builder(String.class, "href").addModifiers(Modifier.PRIVATE).build();
	private final FieldSpec relField = FieldSpec.builder(String.class, "rel").addModifiers(Modifier.PRIVATE).build();
	private final FieldSpec typeField = FieldSpec.builder(String.class, "type").addModifiers(Modifier.PRIVATE).build();
	private final FieldSpec baseUrl = FieldSpec.builder(String.class, "baseUrl", Modifier.PRIVATE, Modifier.FINAL).build();
	private final FieldSpec generatedUrl = FieldSpec.builder(String.class, "generatedUrl", Modifier.PRIVATE).build();
	private final FieldSpec queryParamsWithWildcards = FieldSpec.builder(map, "queryParamsWithWildcards", Modifier.PRIVATE, Modifier.FINAL)
			.initializer("new $T()", ClassName.get(HashMap.class)).build();

	public Link(String packageName) {
		super(packageName + ".generic.model", "Link");
		thisClass = ClassName.get(this.packageName, this.className);
	}

	@Override
	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addAnnotation(
						AnnotationSpec.builder(SuppressWarnings.class)
								.addMember("value", "$S", "unused")
								.build())
				.addModifiers(Modifier.PUBLIC)
				.addField(hrefField)
				.addField(relField)
				.addField(typeField)
				.addMethod(getStandardConstructor())
				.addMethod(getConstructor())
				.addMethods(getGetterSetterHref())
				.addMethods(getGetterSetterRel())
				.addMethods(getGetterSetterType())
				.addMethod(getEquals())
				.addMethod(getHashCode())
				.addType(generateBuilder())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getHashCode() {
		// @formatter:off
		return MethodSpec.methodBuilder("hashCode")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(int.class)
				.addStatement("$T $N = $N != null ? $N.hashCode() : 0", int.class, "result", hrefField, hrefField)
				.addStatement("$N = 31 * $N + ($N != null ? $N.hashCode() : 0)", "result", "result", relField, relField)
				.addStatement("$N = 31 * $N + ($N != null ? $N.hashCode() : 0)", "result", "result", typeField, typeField)
				.addStatement("return $N", "result")
				.build();
		// @formatter:on
	}

	private MethodSpec getEquals() {
		// @formatter:off
		return MethodSpec.methodBuilder("equals")
					.addAnnotation(Override.class)
					.addModifiers(Modifier.PUBLIC)
					.returns(boolean.class)
					.addParameter(Object.class, "o")
					.beginControlFlow("if (this == $N)", "o")
					.addStatement("return true")
					.endControlFlow()
					.beginControlFlow("if ($N == null || getClass() != $N.getClass())", "o", "o")
					.addStatement("return false")
					.endControlFlow()
					.addStatement("$T $N = ($T) $N", thisClass, "link", thisClass, "o")
					.beginControlFlow("if ($N != null ? !$N.equals($N.href) : $N.href != null)", hrefField, hrefField, "link", "link")
					.addStatement("return false")
					.endControlFlow()
					.beginControlFlow("if ($N != null ? !$N.equals($N.href) : $N.rel != null)", relField, relField, "link", "link")
					.addStatement("return false")
					.endControlFlow()
					.addStatement("return $N != null ? $N.equals($N.type) : $N.type == null", typeField, typeField, "link", "link")
					.build();
		// @formatter:on
	}

	private MethodSpec getStandardConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.build();
		// @formatter:on
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(String.class, "href")
				.addParameter(String.class, "rel")
				.addParameter(String.class, "type")
				.addStatement("this.$N = $N", hrefField, "href")
				.addStatement("this.$N = $N", relField, "rel")
				.addStatement("this.$N = $N", typeField, "type")
				.build();
		// @formatter:on
	}

	private List<MethodSpec> getGetterSetterHref() {
		// @formatter:off
		MethodSpec getHref = MethodSpec.methodBuilder("getHref")
				.addModifiers(Modifier.PUBLIC)
				.returns(String.class)
				.addStatement("return this.$N", hrefField)
				.build();

		MethodSpec getHrefWithoutQueryParams = MethodSpec.methodBuilder("getHrefWithoutQueryParams")
				.addModifiers(Modifier.PUBLIC)
				.returns(String.class)
				.beginControlFlow("if (this.$N.contains(\"?\"))", hrefField)
				.addStatement("return this.$N.split(\"\\\\?\")[0]", hrefField)
				.endControlFlow()
				.addStatement("return this.$N", hrefField)
				.build();

		MethodSpec setHref = MethodSpec.methodBuilder("setHref")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(String.class, "href")
				.addStatement("this.$N = $N", hrefField, "href")
				.build();
		// @formatter:on

		List<MethodSpec> methodSpecs = new ArrayList<>();
		methodSpecs.add(getHref);
		methodSpecs.add(getHrefWithoutQueryParams);
		methodSpecs.add(setHref);

		return methodSpecs;
	}

	private List<MethodSpec> getGetterSetterRel() {
		// @formatter:off
		MethodSpec getRel = MethodSpec.methodBuilder("getRel")
				.addModifiers(Modifier.PUBLIC)
				.returns(String.class)
				.addStatement("return this.$N", relField)
				.build();

		MethodSpec setRel = MethodSpec.methodBuilder("setRel")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(String.class, "rel")
				.addStatement("this.$N = $N", relField, "rel")
				.build();
		// @formatter:on

		List<MethodSpec> methodSpecs = new ArrayList<>();
		methodSpecs.add(getRel);
		methodSpecs.add(setRel);

		return methodSpecs;
	}

	private List<MethodSpec> getGetterSetterType() {
		// @formatter:off
		MethodSpec getType = MethodSpec.methodBuilder("getType")
				.addModifiers(Modifier.PUBLIC)
				.returns(String.class)
				.addStatement("return this.$N", typeField)
				.build();

		MethodSpec setType = MethodSpec.methodBuilder("setType")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(String.class, "type")
				.addStatement("this.$N = $N", typeField, "type")
				.build();
		// @formatter:on

		List<MethodSpec> methodSpecs = new ArrayList<>();
		methodSpecs.add(getType);
		methodSpecs.add(setType);

		return methodSpecs;
	}

	private TypeSpec generateBuilder() {
		ClassName thisBuilder = ClassName.get(this.packageName + "." + this.className, "Builder");

		// @formatter:off
		return TypeSpec.classBuilder(thisBuilder)
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addField(baseUrl)
				.addField(generatedUrl)
				.addField(queryParamsWithWildcards)
				.addMethod(getConstructorOne())
				.addMethod(getConstructorTwo())
				.addMethod(getAddQueryParam(thisBuilder))
				.addMethod(getBuild())
				.addMethod(getSplitQueryParams())
				.build();
		// @formatter:on
	}

	private MethodSpec getBuild() {
		// @formatter:off
		return MethodSpec.methodBuilder("build")
					.addModifiers(Modifier.PUBLIC)
					.returns(String.class)
					.addStatement("return this.$N", generatedUrl)
					.build();
		// @formatter:on
	}

	private MethodSpec getAddQueryParam(ClassName thisBuilder) {
		// @formatter:off
		return MethodSpec.methodBuilder("addQueryParam")
					.addModifiers(Modifier.PUBLIC)
					.returns(thisBuilder)
					.addParameter(String.class, "key")
					.addParameter(String.class, "value")
					.addStatement("$T $N = $N.get(key)", String.class, "queryTemplate", queryParamsWithWildcards)
					.beginControlFlow("if ($N != null)", "queryTemplate")
					.beginControlFlow("if(this.$N.contains(\"?\"))", generatedUrl)
					.addStatement("this.$N += \"&\" + $N + $N", generatedUrl, "queryTemplate", "value")
					.endControlFlow()
					.beginControlFlow("else")
					.addStatement("this.$N += \"?\" + $N + $N", generatedUrl, "queryTemplate", "value")
					.endControlFlow()
					.endControlFlow()
					.addStatement("return this")
					.build();
		// @formatter:on
	}

	private MethodSpec getConstructorTwo() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PUBLIC)
					.addParameter(thisClass, "link")
					.addStatement("this(link.getHref())")
					.build();
		// @formatter:on
	}

	private MethodSpec getConstructorOne() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PUBLIC)
					.addParameter(String.class, "orgUrl")
					.addStatement("$T[] $N = $N.split(\"\\\\?\")", String.class, "splitUrl", "orgUrl")
					.addStatement("this.$N = $N[0]", baseUrl, "splitUrl")
					.addStatement("this.$N = $N", generatedUrl, "baseUrl")
					.addStatement("$N($N[1])", getSplitQueryParams(), "splitUrl")
					.build();
		// @formatter:on
	}

	private MethodSpec getSplitQueryParams() {
		// @formatter:off
		return MethodSpec.methodBuilder("splitQueryParams")
					.addModifiers(Modifier.PRIVATE)
					.returns(void.class)
					.addParameter(String.class, "queryParamsAsString")
					.addStatement("$T[] $N = $N.split(\"&\")", String.class, "queryParams", "queryParamsAsString")
					.beginControlFlow("for ($T $N : $N)", String.class, "queryParam", "queryParams")
					.addStatement("$T[] $N = $N.split(\"=\")", String.class, "splitParam", "queryParam")
					.addStatement("$N.put($N[0], $N[0] + \"=\")", queryParamsWithWildcards, "splitParam", "splitParam")
					.endControlFlow()
					.build();
		// @formatter:on
	}
}