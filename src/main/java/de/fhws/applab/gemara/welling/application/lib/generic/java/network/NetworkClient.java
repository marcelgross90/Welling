package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.io.IOException;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLogClass;

public class NetworkClient extends AbstractModelClass {

	private final ClassName callClassName = ClassName.get("okhttp3", "Call");
	private final ClassName callBackClassName = ClassName.get("okhttp3", "Callback");
	private final ClassName okHttpClientClassName = ClassName.get("okhttp3", "OkHttpClient");
	private final ClassName responseClassName = ClassName.get("okhttp3", "Response");
	private final ClassName networkRequestClassName;
	private final ClassName networkResponseClassName;
	private final ClassName okHttpSingletonClassName;
	private final ClassName networkCallbackClassName;

	private final FieldSpec clientField =
			FieldSpec.builder(okHttpClientClassName, "client").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	private final FieldSpec requestField;

	public NetworkClient(String packageName) {
		super(packageName + ".generic.network", "NetworkClient");
		this.networkRequestClassName = ClassName.get(this.packageName, "NetworkRequest");
		this.networkResponseClassName = ClassName.get(this.packageName, "NetworkResponse");
		this.okHttpSingletonClassName = ClassName.get(this.packageName, "OKHttpSingleton");
		this.networkCallbackClassName = ClassName.get(this.packageName, "NetworkCallback");
		this.requestField = FieldSpec.builder(networkRequestClassName, "request").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	}

	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addField(clientField)
				.addField(requestField)
				.addMethod(getConstructor())
				.addMethod(getSendRequest())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getSendRequest() {
		// @formatter:off
		return MethodSpec.methodBuilder("sendRequest")
				.addModifiers(Modifier.PUBLIC)
				.addParameter(
						ParameterSpec.builder(networkCallbackClassName, "callback")
								.addModifiers(Modifier.FINAL)
								.build())
				.addCode("$N.newCall($N.buildRequest()).enqueue($L);\n", clientField, requestField, getCallBack())
				.build();
		// @formatter:on
	}

	private TypeSpec getCallBack() {
		// @formatter:off
			return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(callBackClassName)
				.addMethod(getOnFailure())
				.addMethod(getOnResponse())
				.build();
		// @formatter:on
	}

	private MethodSpec getOnResponse() {
		// @formatter:off
		//todo add throws ioException
		return MethodSpec.methodBuilder("onResponse")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(callClassName, "call")
				.addParameter(responseClassName, "response")
				.beginControlFlow("if (!$N.isSuccessful())", "response")
				.addStatement("$T.d($S, $N.toString())", getLogClass(), "Request failure", "response")
				.addStatement("$N.onFailure()", "callback")
				.endControlFlow()
				.addStatement("$N.onSuccess( new $T($N.body().charStream(), $N.headers().toMultimap()))",
						"callback", networkResponseClassName, "response", "response")
				.build();
		// @formatter:on
	}

	private MethodSpec getOnFailure() {
		// @formatter:off
		return MethodSpec.methodBuilder("onFailure")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(callClassName, "call")
				.addParameter(IOException.class, "e")
				.addStatement("e.printStackTrace()")
				.addStatement("$N.onFailure()", "callback")
				.build();
		// @formatter:on
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(ParameterSpec.builder(networkRequestClassName, "request").build())
				.addStatement("this.$N = $T.getCacheInstance($N).getClient()", clientField, okHttpSingletonClassName, getContextParam())
				.addStatement("this.$N = request", this.requestField)
				.build();
		// @formatter:on
	}
}