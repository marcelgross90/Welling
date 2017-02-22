package de.fhws.applab.gemara.welling.application.gradle.lib;

import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

/**
 * Only Update Dependencies in this Class
 */
public class BuildGradleLib extends GeneratedFile {

	private final String directoryName;

	public BuildGradleLib(String directoryName) {
		this.directoryName = directoryName;
	}

	@Override
	public void generate() {
		appendln("apply plugin: 'com.android.library'\n");
		appendln("\n");
		appendln("android {\n");
		appendln("\tcompileSdkVersion 25\n");
		appendln("\tbuildToolsVersion \"25.0.0\"\n");
		appendln("\n");
		appendln("\tdefaultConfig {\n");
		appendln("\t\tminSdkVersion 12\n");
		appendln("\t\ttargetSdkVersion 25\n");
		appendln("\t\tversionCode 1\n");
		appendln("\t\tversionName \"1.0\"\n");
		appendln("\n");
		appendln("\t}\n");
		appendln("\tbuildTypes {\n");
		appendln("\t\trelease {\n");
		appendln("\t\t\tminifyEnabled false\n");
		appendln("\t\t\tproguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'\n");
		appendln("\t\t}\n");
		appendln("\t}\n");
		appendln("}\n");
		appendln("\n");
		appendln("dependencies {\n");
		appendln("\tcompile fileTree(dir: 'libs', include: ['*.jar'])\n");
		appendln("\n");
		appendln("\tcompile 'com.android.support:appcompat-v7:25.1.0'\n");
		appendln("\tcompile 'com.android.support:cardview-v7:25.1.0'\n");
		appendln("\tcompile 'com.android.support:recyclerview-v7:25.1.0'\n");
		appendln("\tcompile 'com.squareup.picasso:picasso:2.5.2'\n");
		appendln("\tcompile 'com.squareup.okhttp3:okhttp:3.4.1'\n");
		appendln("\tcompile 'com.owlike:genson:1.4'\n");
		appendln("\tcompile 'com.android.support:support-v4:25.1.0'\n");
		appendln("\tcompile 'com.android.support:design:25.1.0'\n");
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