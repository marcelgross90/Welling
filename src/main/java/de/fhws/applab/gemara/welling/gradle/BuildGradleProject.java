package de.fhws.applab.gemara.welling.gradle;

import de.fhws.applab.gemara.welling.GeneratedFile;

/**
 * Do NOT touch this class.
 * If you want to update Gradle Change the value of "GRADLE_VERSION"
 */
public class BuildGradleProject extends GeneratedFile {

	private static final String GRADLE_VERSION = "2.2.3";
	@SuppressWarnings("FieldCanBeLocal")
	private final String directoryName = "generated/";

	@Override
	public void generate() {
		appendln("buildscript {");
		appendln("repositories {");
		appendln("jcenter()");
		appendln("}");
		appendln("dependencies {");
		appendln("classpath 'com.android.tools.build:gradle:" + GRADLE_VERSION + "'");
		appendln("}");
		appendln("}");
		appendln("allprojects {");
		appendln("repositories {");
		appendln("jcenter()");
		appendln("}");
		appendln("}");
		appendln("task clean(type: Delete) {");
		appendln("delete rootProject.buildDir");
		appendln("}");

	}

	@Override
	protected String getFileName() {
		return "build.gradle";
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}
}