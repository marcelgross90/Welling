package de.fhws.applab.gemara.welling.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;

public class NetworkRequest extends AbstractModelClass {

	private final ClassName mediaTypeClassName = ClassName.get("okhttp3", "MediaType");
	private final ClassName requestClassName = ClassName.get("okhttp3", "Request");
	private final ClassName requestBuilderClassName = ClassName.get("okhttp3.Request", "Builder");
	private final ClassName requestBodyClassName = ClassName.get("okhttp3", "RequestBody");
	private final ClassName thisClass;

	private final FieldSpec requestBuilderField = FieldSpec.builder(requestBuilderClassName, "requestBuilder").build();

	public NetworkRequest(String packageName, String className) {
		super(packageName + ".generic.network", className);
		thisClass = ClassName.get(this.packageName, this.className);
	}

	public JavaFile javaFile() {

		MethodSpec constructor = MethodSpec.constructorBuilder()
				.addStatement("this.$N = new $T()", requestBuilderField, requestBuilderClassName)
				.build();

		MethodSpec url = MethodSpec.methodBuilder("url")
				.addModifiers(Modifier.PUBLIC).returns(thisClass)
				.addParameter(ParameterSpec.builder(String.class, "url").build())
				.addStatement("this.$N.url(url)", requestBuilderField)
				.addStatement("return this")
				.build();

		MethodSpec addHeader = MethodSpec.methodBuilder("addHeader")
				.addModifiers(Modifier.PUBLIC).returns(thisClass)
				.addParameter(ParameterSpec.builder(String.class, "header").build())
				.addParameter(ParameterSpec.builder(String.class, "value").build())
				.addStatement("this.$N.addHeader(header, value)", requestBuilderField)
				.addStatement("return this")
				.build();

		MethodSpec addHeaderShort = MethodSpec.methodBuilder("addHeader")
				.addModifiers(Modifier.PUBLIC).returns(thisClass)
				.addParameter(ParameterSpec.builder(String.class, "acceptHeader").build())
				.addStatement("return $N.addHeader(\"Accept\", acceptHeader)", requestBuilderField)
				.build();

		MethodSpec post = MethodSpec.methodBuilder("post")
				.addModifiers(Modifier.PUBLIC).returns(thisClass)
				.addParameter(ParameterSpec.builder(String.class, "payload").build())
				.addParameter(ParameterSpec.builder(String.class, "mediaType").build())
				.addStatement("$T body = $T.create($T.parse(mediaType), payload)", requestBodyClassName, requestBodyClassName, mediaTypeClassName)
				.addStatement("this.$N.requestBuilder.post(body)", requestBuilderField)
				.addStatement("return this")
				.build();

		MethodSpec put = MethodSpec.methodBuilder("put")
				.addModifiers(Modifier.PUBLIC).returns(thisClass)
				.addParameter(ParameterSpec.builder(String.class, "payload").build())
				.addParameter(ParameterSpec.builder(String.class, "mediaType").build())
				.addStatement("$T body = $T.create($T.parse(mediaType), payload)", requestBodyClassName, requestBodyClassName, mediaTypeClassName)
				.addStatement("this.$N.requestBuilder.put(body)", requestBuilderField)
				.addStatement("return this")
				.build();

		MethodSpec delete = MethodSpec.methodBuilder("delete")
				.addModifiers(Modifier.PUBLIC).returns(thisClass)
				.addStatement("this.$N.requestBuilder.delete()", requestBuilderField)
				.addStatement("return this")
				.build();

		MethodSpec buildRequest = MethodSpec.methodBuilder("buildRequest")
				.addModifiers(Modifier.PUBLIC).returns(requestClassName)
				.addStatement("return this.$N.requestBuilder.build()", requestBuilderField)
				.build();


		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addField(requestBuilderField)
				.addMethod(constructor)
				.addMethod(url)
				.addMethod(addHeader)
				.addMethod(addHeaderShort)
				.addMethod(post)
				.addMethod(put)
				.addMethod(delete)
				.addMethod(buildRequest)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
