package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class NetworkResponse extends AbstractModelClass {

	private final ClassName readerClassName = ClassName.get(Reader.class);
	private final ClassName headerParserClassName;

	private final ParameterizedTypeName map = ParameterizedTypeName
			.get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(List.class, String.class));
	private final ParameterizedTypeName linkMap;

	private final FieldSpec readerField = FieldSpec.builder(readerClassName, "responseReader")
			.addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
	private final FieldSpec headerField = FieldSpec.builder(map, "headerField").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

	private final ParameterSpec reader = ParameterSpec.builder(readerClassName, "responseReader").build();
	private final ParameterSpec header = ParameterSpec.builder(map, "header").build();

	public NetworkResponse(String packageName) {
		super(packageName + ".generic.network", "NetworkResponse");
		headerParserClassName = ClassName.get(this.packageName, "HeaderParser");
		linkMap = ParameterizedTypeName
				.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(packageName + ".generic.model", "Link"));
	}

	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec type = TypeSpec.classBuilder(this.className)
					.addModifiers(Modifier.PUBLIC)
					.addField(readerField)
					.addField(headerField)
					.addMethod(getConstructor())
					.addMethod(getResponseReader())
					.addMethod(getGetHeader())
					.addMethod(getGetLinkHeader())
					.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}

	private MethodSpec getGetLinkHeader() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLinkHeader")
					.addModifiers(Modifier.PUBLIC)
					.returns(linkMap)
					.addStatement("return $T.getLinks($N.get($S))", headerParserClassName, headerField, "link")
					.build();
		// @formatter:on
	}

	private MethodSpec getGetHeader() {
		// @formatter:off
		return MethodSpec.methodBuilder("getHeader")
					.addModifiers(Modifier.PUBLIC).returns(map)
					.addStatement("return $N", headerField)
					.build();
		// @formatter:on
	}

	private MethodSpec getResponseReader() {
		// @formatter:off
		return MethodSpec.methodBuilder("getResponseReader")
					.addModifiers(Modifier.PUBLIC).returns(readerClassName)
					.addStatement("return $N", readerField)
					.build();
		// @formatter:on
	}

	private MethodSpec getConstructor() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
					.addModifiers(Modifier.PUBLIC)
					.addParameter(reader)
					.addParameter(header)
					.addStatement("this.$N = $N", readerField, reader)
					.addStatement("this.$N = $N", headerField, header)
					.build();
		// @formatter:on
	}
}