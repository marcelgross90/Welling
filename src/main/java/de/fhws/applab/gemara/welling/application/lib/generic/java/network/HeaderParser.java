package de.fhws.applab.gemara.welling.application.lib.generic.java.network;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;

public class HeaderParser extends AbstractModelClass {

	private final ClassName linkClassName;
	private final ClassName stringClassName = ClassName.get(String.class);

	private final ParameterizedTypeName linkMap;
	private final ParameterizedTypeName list = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(String.class));

	public HeaderParser(String packageName) {
		super(packageName + ".generic.network", "HeaderParser");
		linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		linkMap = ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(String.class), linkClassName);
	}

	public JavaFile javaFile() {

		MethodSpec getLinks = MethodSpec.methodBuilder("getLinks").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(linkMap)
				.addParameter(ParameterSpec.builder(list, "linkHeader").build()).addStatement("$T linkHashMap = new $T()", linkMap, linkMap)
				.beginControlFlow("for ($T s : linkHeader)", stringClassName).addStatement("$T link = getLink(s)", linkClassName)
				.addStatement("linkHashMap.put(link.getRel(), link)").endControlFlow().addStatement("return linkHashMap").build();

		MethodSpec getLink = MethodSpec.methodBuilder("getLink").addModifiers(Modifier.PRIVATE, Modifier.STATIC).returns(linkClassName)
				.addParameter(stringClassName, "linkString").addStatement("$T[] links = linkString.split(\";\\\\s*\")", stringClassName)
				.addStatement("$T relType = \"\"", stringClassName).addStatement("$T mediaType = \"\"", stringClassName)
				.addStatement("$T url = \"\"", stringClassName).beginControlFlow("for ($T linkPart : links)", stringClassName)
				.beginControlFlow("if (linkPart.contains(\"rel=\"))")
				.addStatement("relType = linkPart.replace(\"rel=\", \"\").replace(\"\\\"\", \"\")").endControlFlow()
				.beginControlFlow("else if (linkPart.contains(\"type=\"))")
				.addStatement("mediaType = linkPart.replace(\"type=\", \"\").replace(\"\\\"\", \"\")").endControlFlow()
				.beginControlFlow("else if (linkPart.contains(\"<\"))")
				.addStatement("url = linkPart.replace(\"<\", \"\").replace(\">\", \"\")").endControlFlow().endControlFlow()
				.addStatement("return new $T(url, relType, mediaType)", linkClassName).build();

		TypeSpec typeSpec = TypeSpec.classBuilder(this.className).addModifiers(Modifier.PUBLIC).addMethod(getLinks).addMethod(getLink)
				.build();

		return JavaFile.builder(this.packageName, typeSpec).build();
	}
}
