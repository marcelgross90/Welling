package de.fhws.applab.gemara.welling.application.gradle;

import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

public abstract class ProguardRules extends GeneratedFile {

	private final String directoryName;

	@SuppressWarnings("WeakerAccess")
	public ProguardRules(String directoryName) {
		this.directoryName = directoryName;
	}

	@Override
	public void generate() {
		appendln("# Add project specific ProGuard rules here.\n");
		appendln("# By default, the flags in this file are appended to flags specified\n");
		appendln("# in /Users/username/Library/Android/sdk/tools/proguard/proguard-android.txt\n");
		appendln("# You can edit the include path and order by changing the proguardFiles\n");
		appendln("# directive in build.gradle.\n");
		appendln("#\n");
		appendln("# For more details, see\n");
		appendln("#   http://developer.android.com/guide/developing/tools/proguard.html\n");
		appendln("\n");
		appendln("# Add any project specific keep options here:\n");
		appendln("\n");
		appendln("# If your project uses WebView with JS, uncomment the following\n");
		appendln("# and specify the fully qualified class name to the JavaScript interface\n");
		appendln("# class:\n");
		appendln("#-keepclassmembers class fqcn.of.javascript.interface.for.webview {\n");
		appendln("#   public *;\n");
		appendln("#}");
	}

	@Override
	protected String getFileName() {
		return "proguard-rules.pro";
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}
}
