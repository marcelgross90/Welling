package de.fhws.applab.gemara.welling.application.lib.generic.java.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GensonBuilder extends AbstractModelClass {

	private final ClassName gensonClassName = ClassName.get("com.owlike.genson", "Genson");
	private final ClassName simpleDateFormatClassName = ClassName.get(SimpleDateFormat.class);
	private final ClassName localeClassName = ClassName.get(Locale.class);

	public GensonBuilder(String packageName) {
		super(packageName + ".generic.util", "GensonBuilder");
	}

	public JavaFile javaFile() {
		// @formatter:off
		MethodSpec getDateFormatter = MethodSpec.methodBuilder("getDateFormatter")
				.addModifiers(Modifier.PUBLIC)
				.returns(gensonClassName)
				.addCode("return new com.owlike.genson.GensonBuilder()\n"
						+ ".useDateAsTimestamp(false)\n"
						+ ".useDateFormat(new $T(\"yyyy-MM-dd'T'HH:mm:ss\", $T.GERMANY)) \n"
						+ ".create();\n",
						simpleDateFormatClassName, localeClassName)
				.build();

		TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.addMethod(getDateFormatter)
				.build();
		// @formatter:on

		return JavaFile.builder(this.packageName, type).build();
	}
}