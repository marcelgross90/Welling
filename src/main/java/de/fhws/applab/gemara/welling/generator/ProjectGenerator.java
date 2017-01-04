package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.GeneratedFile;
import de.fhws.applab.gemara.welling.gradle.BuildGradleProject;
import de.fhws.applab.gemara.welling.gradle.SettingsGradle;

import java.util.ArrayList;
import java.util.List;

public class ProjectGenerator {

	private final String libName;

	public ProjectGenerator(String libName) {
		this.libName = libName;
		copyGradleFiles();
	}

	public List<GeneratedFile> getGradleClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.add(new BuildGradleProject());
		classes.add(new SettingsGradle(libName));

		return classes;
	}

	private void copyGradleFiles() {
		Copy copy = new Copy();

		copy.copyFile("gradle/gradlew.bat", "gradlew.bat");
		copy.copyFile("gradle/gradlew", "gradlew");
		copy.copyFolder("gradle/wrapper", "gradle/wrapper");
	}
}
