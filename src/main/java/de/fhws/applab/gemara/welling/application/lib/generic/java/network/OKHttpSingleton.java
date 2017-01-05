package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class OKHttpSingleton extends AbstractModelClass {

	private final ClassName okHttpClientClassName = ClassName.get("okhttp3", "OkHttpClient");
	private final ClassName cacheClassName = ClassName.get("okhttp3", "Cache");
	private final ClassName thisClass;

	private final FieldSpec instance;
	private final FieldSpec client;

	public OKHttpSingleton(String packageName) {
		super(packageName + ".generic.network", "OKHttpSingleton");
		thisClass = ClassName.get(this.packageName, this.className);
		instance = FieldSpec.builder(thisClass, "instance")
				.addModifiers(Modifier.PRIVATE, Modifier.STATIC)
				.build();
		client = FieldSpec.builder(okHttpClientClassName, "client")
				.addModifiers(Modifier.PRIVATE, Modifier.STATIC)
				.build();
	}

	public JavaFile javaFile() {
		MethodSpec getInstance = MethodSpec.methodBuilder("getInstance")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(thisClass)
				.addParameter(getContextClass(), "context")
				.addParameter(boolean.class, "cached")
				.beginControlFlow("if ($N == null)", instance)
				.addStatement("$N = new $T(context, cached)", instance, thisClass)
				.endControlFlow()
				.addStatement("return $N", instance)
				.build();

		MethodSpec getCacheInstance = MethodSpec.methodBuilder("getCacheInstance")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(thisClass)
				.addParameter(getContextClass(), "context")
				.addStatement("return $N(context, true)", getInstance)
				.build();

		MethodSpec getClient = MethodSpec.methodBuilder("getClient")
				.addModifiers(Modifier.PUBLIC).returns(okHttpClientClassName)
				.addStatement("return $N", client)
				.build();

		MethodSpec constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PRIVATE)
				.addParameter(getContextClass(), "context")
				.addParameter(boolean.class, "cached")
				.beginControlFlow("if (cached)")
				.addStatement("int cacheSize = (int) ((Runtime.getRuntime().maxMemory() / 1024) / 8)")
				.addStatement("$T cache = new $T($N.getCacheDir(), cacheSize)", cacheClassName, cacheClassName, getContextParam())
				.addStatement("$N = new $T.Builder().cache(cache).build()", client, okHttpClientClassName)
				.endControlFlow()
				.beginControlFlow("else")
				.addStatement("$N = new $T()", client, okHttpClientClassName)
				.endControlFlow()
				.build();

		final TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addField(instance)
				.addField(client)
				.addMethod(getInstance)
				.addMethod(getCacheInstance)
				.addMethod(getClient)
				.addMethod(constructor)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
