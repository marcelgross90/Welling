package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextClass;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;

public class OKHttpSingleton extends AbstractModelClass {

	private final ClassName okHttpClientClassName = ClassName.get("okhttp3", "OkHttpClient");
	private final ClassName cacheClassName = ClassName.get("okhttp3", "Cache");
	private final ClassName thisClass;

	private final FieldSpec instance;
	private final FieldSpec client;

	public OKHttpSingleton(String packageName) {
		super(packageName + ".generic.network", "OKHttpSingleton");
		thisClass = ClassName.get(this.packageName, this.className);
		instance = FieldSpec.builder(thisClass, "instance").addModifiers(Modifier.PRIVATE, Modifier.STATIC).build();
		client = FieldSpec.builder(okHttpClientClassName, "client").addModifiers(Modifier.PRIVATE, Modifier.STATIC).build();
	}

	public JavaFile javaFile() {
		// @formatter:off
		final TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addField(instance)
				.addField(client)
				.addMethod(getGetInstance())
				.addMethod(getGetCacheInstance())
				.addMethod(getGetClient())
				.addMethod(getConstructor())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PRIVATE)
					.addParameter(getContextClass(), "context")
					.addParameter(boolean.class, "cached")
					.beginControlFlow("if ($N)", "cached")
					.addStatement("$T $N = ($T) ((Runtime.getRuntime().maxMemory() / 1024) / 8)", int.class, "cacheSize", int.class)
					.addStatement("$T $N = new $T($N.getCacheDir(), cacheSize)",
							cacheClassName, "cache", cacheClassName, getContextParam())
					.addStatement("$N = new $T.Builder().cache($N).build()", client, okHttpClientClassName, "cache")
					.endControlFlow()
					.beginControlFlow("else")
					.addStatement("$N = new $T()", client, okHttpClientClassName)
					.endControlFlow()
					.build();
		// @formatter:on
	}

	private MethodSpec getGetClient() {
		// @formatter:off
		return MethodSpec.methodBuilder("getClient")
					.addModifiers(Modifier.PUBLIC).returns(okHttpClientClassName)
					.addStatement("return $N", client)
					.build();
		// @formatter:on
	}

	private MethodSpec getGetCacheInstance() {
		// @formatter:off
		return MethodSpec.methodBuilder("getCacheInstance")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(thisClass)
					.addParameter(getContextClass(), "context")
					.addStatement("return $N($N, true)", getGetInstance(), "context")
					.build();
		// @formatter:on
	}

	private MethodSpec getGetInstance() {
		// @formatter:off
		return MethodSpec.methodBuilder("getInstance")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(thisClass)
					.addParameter(getContextClass(), "context")
					.addParameter(boolean.class, "cached")
					.beginControlFlow("if ($N == null)", instance)
					.addStatement("$N = new $T($N, $N)", instance, thisClass, "context", "cached")
					.endControlFlow()
					.addStatement("return $N", instance)
					.build();
		// @formatter:on
	}
}