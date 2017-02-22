package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;

public class HeaderParser extends AbstractModelClass {

	private final ClassName linkClassName;

	private final ParameterizedTypeName linkMap;
	private final ParameterizedTypeName list = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(String.class));

	public HeaderParser(String packageName) {
		super(packageName + ".generic.network", "HeaderParser");
		linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		linkMap = ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(String.class), linkClassName);
	}

	public JavaFile javaFile() {
		// @formatter:off
		TypeSpec typeSpec = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getGetLinks())
				.addMethod(getGetLink())
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, typeSpec).build();
	}

	private MethodSpec getGetLinks() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLinks")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(linkMap)
				.addParameter(list, "linkHeader")
				.addStatement("$T $N = new $T()", linkMap, "linkHashMap", linkMap)
				.beginControlFlow("for ($T s : $N)", String.class, "linkHeader")
				.addStatement("$T $N = $N(s)", linkClassName, "link", getGetLink())
				.addStatement("$N.put($N.getRel(), $N)", "linkHashMap", "link", "link")
				.endControlFlow()
				.addStatement("return $N", "linkHashMap")
				.build();
		// @formatter:on
	}

	private MethodSpec getGetLink() {
		// @formatter:off
		return MethodSpec.methodBuilder("getLink")
				.addModifiers(Modifier.PRIVATE, Modifier.STATIC)
				.returns(linkClassName)
				.addParameter(String.class, "linkString")
				.addStatement("$T[] $N = $N.split($S)", String.class, "links", "linkString", ";\\s*")
				.addStatement("$T $N = $S", String.class, "relType", "")
				.addStatement("$T $N = $S", String.class, "mediaType", "")
				.addStatement("$T $N = $S", String.class, "url", "")
				.beginControlFlow("for ($T $N : $N)", String.class, "linkPart", "links")
				.beginControlFlow("if ($N.contains($S))", "linkPart", "rel=")
				.addStatement("$N = $N.replace($S, $S).replace($S, $S)", "relType", "linkPart", "rel=", "", "\"", "")
				.endControlFlow()
				.beginControlFlow("else if ($N.contains($S))", "linkPart", "type=")
				.addStatement("$N = $N.replace($S, $S).replace($S, $S)", "mediaType", "linkPart", "type=", "", "\"", "")
				.endControlFlow()
				.beginControlFlow("else if ($N.contains($S))", "linkPart", "<")
				.addStatement("$N = $N.replace($S, $S).replace($S, $S)", "url", "linkPart", "<", "", ">", "")
				.endControlFlow()
				.endControlFlow()
				.addStatement("return new $T($N, $N, $N)", linkClassName, "url", "relType", "mediaType")
				.build();
		// @formatter:on
	}
}