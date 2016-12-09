package de.fhws.applab.gemara.welling.lib.generic.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.io.IOException;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class NetworkClient extends AbstractModelClass {

	private final ClassName iOExceptionClassName = ClassName.get(IOException.class);
	private final ClassName callClassName = ClassName.get("okhttp3", "Call");
	private final ClassName callBackClassName = ClassName.get("okhttp3", "Callback");
	private final ClassName okHttpClientClassName = ClassName.get("okhttp3", "OkHttpClient");
	private final ClassName responseClassName = ClassName.get("okhttp3", "Response");
	private final ClassName networkRequestClassName;
	private final ClassName networkResponseClassName;
	private final ClassName okHttpSingletonClassName;
	private final ClassName networkCallbackClassName;

	private final FieldSpec clientField = FieldSpec.builder(okHttpClientClassName, "client").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	private final FieldSpec requestField;


	public NetworkClient(String packageName, String className) {
		super(packageName + ".generic.network", className);
		this.networkRequestClassName = ClassName.get(this.packageName, "NetworkRequest");
		this.networkResponseClassName = ClassName.get(this.packageName, "NetworkResponse");
		this.okHttpSingletonClassName = ClassName.get(this.packageName, "OKHttpSingleton");
		this.networkCallbackClassName = ClassName.get(this.packageName, "NetworkCallback");
		this.requestField = FieldSpec.builder(networkRequestClassName, "request").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	}

	public JavaFile javaFile() {

		MethodSpec constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(ParameterSpec.builder(networkRequestClassName, "request").build())
				.addStatement("this.$N = $T.getCacheInsance($N).getClient()", clientField, okHttpSingletonClassName, getContextParam())
				.addStatement("this.$N = request", this.requestField)
				.build();

		MethodSpec onFailure = MethodSpec.methodBuilder("onFailure")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(ParameterSpec.builder(callClassName, "call").build())
				.addParameter(ParameterSpec.builder(iOExceptionClassName, "e").build())
				.addStatement("e.printStackTrace()")
				.addStatement("callback.onFailure()")
				.build();

		//todo add throws ioException
		MethodSpec onResponse = MethodSpec.methodBuilder("onResponse")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(ParameterSpec.builder(callClassName, "call").build())
				.addParameter(ParameterSpec.builder(responseClassName, "response").build())
				.beginControlFlow("if (!response.isSuccessful())")
				.addStatement("$T.d(\"Request failure\", response.toString())", getLogClass())
				.addStatement("callback.onFailure()")
				.endControlFlow()
				.addStatement("callback.onSuccess( new $T(response.body().charStream(), response.headers().toMultimap())", networkResponseClassName)
				.build();

		TypeSpec callBackClass = TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(callBackClassName)
				.addMethod(onFailure)
				.addMethod(onResponse)
				.build();

		MethodSpec sendRequest = MethodSpec.methodBuilder("sendRequest")
				.addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(networkCallbackClassName, "callback").addModifiers(Modifier.FINAL).build())
				.addCode("$N.newCallback($N.buildRequest()).enqueue($L);\n", clientField, requestField, callBackClass)
				.build();

		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addField(clientField)
				.addField(requestField)
				.addMethod(constructor)
				.addMethod(sendRequest)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
