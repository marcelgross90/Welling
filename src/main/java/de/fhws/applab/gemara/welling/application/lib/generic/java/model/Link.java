package de.fhws.applab.gemara.welling.application.lib.generic.java.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Link extends AbstractModelClass {

	private final ClassName thisClass;

	private final FieldSpec hrefField = FieldSpec.builder(String.class, "href").addModifiers(Modifier.PRIVATE).build();
	private final FieldSpec relField = FieldSpec.builder(String.class, "rel").addModifiers(Modifier.PRIVATE).build();
	private final FieldSpec typeField = FieldSpec.builder(String.class, "type").addModifiers(Modifier.PRIVATE).build();

	public Link(String packageName) {
		super(packageName + ".generic.model", "Link");
		thisClass = ClassName.get(this.packageName, this.className);
	}

	@Override
	public JavaFile javaFile() {

		MethodSpec standardConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();
		MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(String.class, "href").build())
				.addParameter(ParameterSpec.builder(String.class, "rel").build())
				.addParameter(ParameterSpec.builder(String.class, "type").build()).addStatement("this.$N = href", hrefField)
				.addStatement("this.$N = rel", relField).addStatement("this.$N = type", typeField).build();
		MethodSpec getHref = MethodSpec.methodBuilder("getHref").addModifiers(Modifier.PUBLIC).returns(String.class)
				.addStatement("return this.$N", hrefField).build();
		MethodSpec getHrefWithoutQueryParams = MethodSpec.methodBuilder("getHrefWithoutQueryParams").addModifiers(Modifier.PUBLIC)
				.returns(String.class).beginControlFlow("if (this.$N.contains(\"?\"))", hrefField)
				.addStatement("return this.$N.split(\"\\\\?\")[0]", hrefField).endControlFlow().addStatement("return this.$N", hrefField)
				.build();
		MethodSpec setHref = MethodSpec.methodBuilder("setHref").addModifiers(Modifier.PUBLIC).returns(void.class)
				.addParameter(ParameterSpec.builder(String.class, "href").build()).addStatement("this.$N = href", hrefField).build();

		MethodSpec getRel = MethodSpec.methodBuilder("getRel").addModifiers(Modifier.PUBLIC).returns(String.class)
				.addStatement("return this.$N", relField).build();
		MethodSpec setRel = MethodSpec.methodBuilder("setRel").addModifiers(Modifier.PUBLIC).returns(void.class)
				.addParameter(ParameterSpec.builder(String.class, "rel").build()).addStatement("this.$N = rel", relField).build();

		MethodSpec getType = MethodSpec.methodBuilder("getType").addModifiers(Modifier.PUBLIC).returns(String.class)
				.addStatement("return this.$N", typeField).build();
		MethodSpec setType = MethodSpec.methodBuilder("setType").addModifiers(Modifier.PUBLIC).returns(void.class)
				.addParameter(ParameterSpec.builder(String.class, "type").build()).addStatement("this.$N = type", typeField).build();

		MethodSpec equals = MethodSpec.methodBuilder("equals").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
				.returns(boolean.class).addParameter(ParameterSpec.builder(Object.class, "o").build()).beginControlFlow("if (this == o)")
				.addStatement("return true").endControlFlow().beginControlFlow("if (o == null || getClass() != o.getClass())")
				.addStatement("return false").endControlFlow().addStatement("$T link = ($T) o", thisClass, thisClass)
				.beginControlFlow("if (href != null ? !href.equals(link.href) : link.href != null)").addStatement("return false")
				.endControlFlow().beginControlFlow("if (rel != null ? !rel.equals(link.href) : link.rel != null)")
				.addStatement("return false").endControlFlow()
				.addStatement("return type != null ? type.equals(link.type) : link.type == null").build();

		MethodSpec hashCode = MethodSpec.methodBuilder("hashCode").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
				.returns(int.class).addStatement("int result = href != null ? href.hashCode() : 0")
				.addStatement("result = 31 * result + (rel != null ? rel.hashCode() : 0)")
				.addStatement("result = 31 * result + (type != null ? type.hashCode() : 0)").addStatement("return result").build();

		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unused").build())
				.addModifiers(Modifier.PUBLIC).addMethod(standardConstructor).addMethod(constructor).addMethod(getHref)
				.addField(hrefField)
				.addField(relField)
				.addField(typeField)
				.addMethod(getHrefWithoutQueryParams).addMethod(setHref).addMethod(getRel).addMethod(setRel).addMethod(getType)
				.addMethod(setType).addMethod(equals).addMethod(hashCode).addType(generateBuilder()).build();

		return JavaFile.builder(this.packageName, type).build();
	}

	private TypeSpec generateBuilder() {
		ClassName thisBuilder = ClassName.get(this.packageName + "." + this.className, "Builder");

		ParameterizedTypeName map = ParameterizedTypeName
				.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(String.class));
		FieldSpec baseUrl = FieldSpec.builder(String.class, "baseUrl").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
		FieldSpec generatedUrl = FieldSpec.builder(String.class, "generatedUrl").addModifiers(Modifier.PRIVATE).build();
		FieldSpec queryParamsWithWildcards = FieldSpec.builder(map, "queryParamsWithWildcards")
				.addModifiers(Modifier.PRIVATE, Modifier.FINAL).initializer("new $T()", ClassName.get(HashMap.class)).build();

		MethodSpec splitQueryParams = MethodSpec.methodBuilder("splitQueryParams").addModifiers(Modifier.PRIVATE).returns(void.class)
				.addParameter(ParameterSpec.builder(String.class, "queryParamsAsString").build())
				.addStatement("$T[] queryParams = queryParamsAsString.split(\"&\")", String.class)
				.beginControlFlow("for ($T queryParam : queryParams)", String.class)
				.addStatement("$T[] splitParam = queryParam.split(\"=\")", String.class)
				.addStatement("$N.put(splitParam[0], splitParam[0] + \"=\")", queryParamsWithWildcards).endControlFlow().build();

		MethodSpec constructorOne = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(String.class, "orgUrl").build())
				.addStatement("$T[] splitUrl = orgUrl.split(\"\\\\?\")", ClassName.get(String.class))
				.addStatement("this.$N = splitUrl[0]", baseUrl).addStatement("this.$N = baseUrl", generatedUrl)
				.addStatement("$N(splitUrl[1])", splitQueryParams).build();

		MethodSpec constructorTwo = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(thisClass, "link").build()).addStatement("this(link.getHref())").build();

		MethodSpec addQueryParam = MethodSpec.methodBuilder("addQueryParam").addModifiers(Modifier.PUBLIC).returns(thisBuilder)
				.addParameter(ParameterSpec.builder(String.class, "key").build())
				.addParameter(ParameterSpec.builder(String.class, "value").build())
				.addStatement("$T queryTemplate = $N.get(key)", String.class, queryParamsWithWildcards)
				.beginControlFlow("if (queryTemplate != null)").beginControlFlow("if(this.$N.contains(\"?\"))", generatedUrl)
				.addStatement("this.$N += \"&\" + queryTemplate + value", generatedUrl).endControlFlow().beginControlFlow("else")
				.addStatement("this.$N += \"?\" + queryTemplate + value", generatedUrl).endControlFlow().endControlFlow()
				.addStatement("return this").build();

		MethodSpec build = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC).returns(String.class)
				.addStatement("return this.$N", generatedUrl).build();

		return TypeSpec.classBuilder(thisBuilder)
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addField(baseUrl)
				.addField(generatedUrl)
				.addField(queryParamsWithWildcards)
				.addMethod(constructorOne)
				.addMethod(constructorTwo)
				.addMethod(addQueryParam)
				.addMethod(build)
				.addMethod(splitQueryParams)
				.build();
	}
}
