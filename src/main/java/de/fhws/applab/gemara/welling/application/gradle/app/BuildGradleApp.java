package de.fhws.applab.gemara.welling.application.gradle.app;

import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

/**
 * Only Update Dependencies in this Class
 */
public class BuildGradleApp extends GeneratedFile {

	private final String directoryName;
	private final String packageName;
	private final String libName;

	public BuildGradleApp(String directoryName, String packageName, String libName) {
		this.directoryName = directoryName;
		this.packageName = packageName;
		this.libName = libName;
	}

	@Override
	public void generate() {
		appendln("apply plugin: 'com.android.application'\n");
		appendln("\n");
		appendln("android {\n");
		appendln("\tcompileSdkVersion 25\n");
		appendln("\tbuildToolsVersion \"25.0.0\"\n");
		appendln("\n");
		appendln("\tdefaultConfig {\n");
		appendln("\t\tapplicationId \"" + packageName + "\"\n");
		appendln("\t\tminSdkVersion 16\n");
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
		appendln("\tlintOptions {\n");
		appendln("\t\tabortOnError false\n");
		appendln("\t}\n");
		appendln("}\n");
		appendln("\n");
		appendln("dependencies {\n");
		appendln("\tcompile fileTree(dir: 'libs', include: ['*.jar'])\n");
		appendln("\n");
		appendln("\tcompile project(':" + libName + "')\n");
		appendln("\tcompile 'com.android.support:appcompat-v7:25.1.0'\n");
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