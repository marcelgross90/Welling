package de.fhws.applab.gemara.welling;

import de.fhws.applab.gemara.welling.generator.AppGenerator;
import de.fhws.applab.gemara.welling.generator.LibGenerator;
import de.fhws.applab.gemara.welling.generator.ProjectGenerator;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AndroidApplicationGenerator {

	private final AndroidMetaModel model;
	
	private final String startDir = "gemara/android/src-gen/generated/";
	private final ProjectGenerator projectGenerator;
	private final LibGenerator libGenerator;
	private final AppGenerator appGenerator;

	public AndroidApplicationGenerator(AndroidMetaModel model) {
		this.model = model;

		this.libGenerator = new LibGenerator(model);
		this.projectGenerator = new ProjectGenerator(model.getApplicationName().toLowerCase() + "_lib");
		this.appGenerator = new AppGenerator(model);
	}

	public void generate() {
		generateProjectFiles();
		generateApp();
		generateLib();
	}

	private void generateProjectFiles() {
		writeGeneratedFiles(projectGenerator.getGradleClasses());
	}

	private void generateApp() {
		writeJavaAppClasses();
		writeXMLAppClasses();
		writeGradleAppClasses();
	}

	private void generateLib() {
		writeJavaLibClasses();
		writeXMLLibClasses();
		writeGradleLibClasses();
	}

	private void writeGradleLibClasses() {
		writeGeneratedFiles(libGenerator.getGradleClasses());
	}

	private void writeXMLLibClasses() {
		writeGeneratedFiles(libGenerator.getXMLClasses());
	}

	private void writeJavaLibClasses() {
		writeJavaFiles(libGenerator.getJavaClasses(), startDir + model.getApplicationName().toLowerCase() + "_lib/src/main/java/");
	}

	private void writeJavaAppClasses() {
		writeJavaFiles(appGenerator.getJavaClasses(), startDir + "app/src/main/java/");
	}

	private void writeGradleAppClasses() {
		writeGeneratedFiles(appGenerator.getGradleClasses());
	}

	private void writeXMLAppClasses() {
		writeGeneratedFiles(appGenerator.getXMLClasses());
	}

	private void writeJavaFiles(List<AbstractModelClass> classes, String dir) {
		for (AbstractModelClass aClass : classes) {
			File file = new File(dir);
			try {
				aClass.javaFile().writeTo(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void writeGeneratedFiles(List<GeneratedFile> files) {
		files.forEach(de.fhws.applab.gemara.welling.GeneratedFile::generateAndWrite);
	}

}
