package de.fhws.applab.gemara.welling.generator.preparation;

import de.fhws.applab.gemara.welling.application.gradle.SettingsGradle;
import de.fhws.applab.gemara.welling.application.gradle.app.AppProguardRules;
import de.fhws.applab.gemara.welling.application.gradle.app.BuildGradleApp;
import de.fhws.applab.gemara.welling.application.gradle.lib.BuildGradleLib;
import de.fhws.applab.gemara.welling.application.gradle.lib.LibProguardRules;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.Copy;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.ArrayList;
import java.util.List;

public class PrepareGradleGenerator extends Preparation {

	public PrepareGradleGenerator(AppDescription appDescription) {
		super(appDescription);
		copyGradleFiles();
	}

	@Override
	public void generate() {
		FileWriter.writeGeneratedFiles(getGradleClasses());
	}

	private List<GeneratedFile> getGradleClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.addAll(getProjectGradleClasses());
		classes.addAll(getAppGradleClasses());
		classes.addAll(getLibGradleClasses());

		return classes;
	}

	private List<GeneratedFile> getProjectGradleClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.add(new SettingsGradle(appDescription.getLibName()));

		return classes;
	}

	private List<GeneratedFile> getLibGradleClasses() {
		String baseDir = "generated/" + appDescription.getLibName() + "/";
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new BuildGradleLib(baseDir));
		classes.add(new LibProguardRules(baseDir));

		return classes;
	}

	private List<GeneratedFile> getAppGradleClasses() {
		String baseDir = "generated/app/";
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new BuildGradleApp(baseDir, appDescription.getAppPackageName(), appDescription.getLibName()));
		classes.add(new AppProguardRules(baseDir));

		return classes;
	}

	private void copyGradleFiles() {
		Copy copy = new Copy();
		copy.copyFile("gradle/gradlew.bat", "gradlew.bat");
		copy.copyFile("gradle/gradlew", "gradlew");
		copy.copyFile("gradle/build.gradle", "build.gradle");
		copy.copyFolder("gradle/wrapper", "gradle/wrapper");
	}
}