package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

public class NetworkRequest extends AbstractModelClass {

	private final ClassName mediaTypeClassName = ClassName.get("okhttp3", "MediaType");
	private final ClassName requestClassName = ClassName.get("okhttp3", "Request");
	private final ClassName requestBuilderClassName = ClassName.get("okhttp3.Request", "Builder");
	private final ClassName requestBodyClassName = ClassName.get("okhttp3", "RequestBody");
	private final ClassName thisClass;

	private final FieldSpec requestBuilderField = FieldSpec.builder(requestBuilderClassName, "requestBuilder").build();

	public NetworkRequest(String packageName) {
		super(packageName + ".generic.network", "NetworkRequest");
		thisClass = ClassName.get(this.packageName, this.className);
	}

	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addField(requestBuilderField)
				.addMethod(getConstructor())
				.addMethod(getUrl())
				.addMethod(getAddHeader())
				.addMethod(getAcceptHeader())
				.addMethod(getPost())
				.addMethod(getPut())
				.addMethod(getDelete())
				.addMethod(getBuildRequest())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getBuildRequest() {
		// @formatter:off
		return MethodSpec.methodBuilder("buildRequest")
				.addModifiers(Modifier.PUBLIC)
				.returns(requestClassName)
				.addStatement("return this.$N.build()", requestBuilderField)
				.build();
		// @formatter:on
	}

	private MethodSpec getDelete() {
		// @formatter:off
		return MethodSpec.methodBuilder("delete")
					.addModifiers(Modifier.PUBLIC)
					.returns(thisClass)
					.addStatement("this.$N.delete()", requestBuilderField)
					.addStatement("return this")
					.build();
		// @formatter:on
	}

	private MethodSpec getPut() {
		// @formatter:off
		return MethodSpec.methodBuilder("put")
					.addModifiers(Modifier.PUBLIC).returns(thisClass)
					.addParameter(String.class, "payload")
					.addParameter(String.class, "mediaType")
					.addStatement("$T $N = $T.create($T.parse($N), $N)",
							requestBodyClassName, "body", requestBodyClassName, mediaTypeClassName, "mediaType", "payload")
					.addStatement("this.$N.put(body)", requestBuilderField)
					.addStatement("return this")
					.build();
		// @formatter:on
	}

	private MethodSpec getPost() {
		// @formatter:off
		return MethodSpec.methodBuilder("post")
					.addModifiers(Modifier.PUBLIC)
					.returns(thisClass)
					.addParameter(String.class, "payload")
					.addParameter(String.class, "mediaType")
					.addStatement("$T $N = $T.create($T.parse($N), $N)",
							requestBodyClassName, "body", requestBodyClassName, mediaTypeClassName, "mediaType", "payload")
					.addStatement("this.$N.post(body)", requestBuilderField)
					.addStatement("return this")
					.build();
		// @formatter:on
	}

	private MethodSpec getAcceptHeader() {
		// @formatter:off
		return MethodSpec.methodBuilder("acceptHeader")
					.addModifiers(Modifier.PUBLIC)
					.returns(thisClass)
					.addParameter(String.class, "acceptHeader")
					.addStatement("return $N($S, $N)", getAddHeader(), "Accept", "acceptHeader")
					.build();
		// @formatter:on
	}

	private MethodSpec getAddHeader() {
		// @formatter:off
		return MethodSpec.methodBuilder("addHeader")
					.addModifiers(Modifier.PUBLIC)
					.returns(thisClass)
					.addParameter(String.class, "header")
					.addParameter(String.class, "value")
					.addStatement("this.$N.addHeader($N, $N)", requestBuilderField, "header", "value")
					.addStatement("return this")
					.build();
		// @formatter:on
	}

	private MethodSpec getUrl() {
		// @formatter:off
		return MethodSpec.methodBuilder("url")
					.addModifiers(Modifier.PUBLIC)
					.returns(thisClass)
					.addParameter(String.class, "url")
					.addStatement("this.$N.url($N)", requestBuilderField, "url")
					.addStatement("return this")
					.build();
		// @formatter:on
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PUBLIC)
					.addStatement("this.$N = new $T()", requestBuilderField, requestBuilderClassName)
					.build();
		// @formatter:on
	}
}