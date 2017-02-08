package de.fhws.applab.gemara.welling.generator.depricated;

import de.fhws.applab.gemara.welling.generator.depricated.AppGenerator;
import de.fhws.applab.gemara.welling.generator.depricated.LibGenerator;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AndroidApplicationGenerator {

	private final AndroidMetaModel model;
	
	private final String startDir = "gemara/android/src-gen/generated/";
	private final LibGenerator libGenerator;
	private final AppGenerator appGenerator;

	public AndroidApplicationGenerator(AndroidMetaModel model) {
		this.model = model;

		this.libGenerator = new LibGenerator(model);
		this.appGenerator = new AppGenerator(model);
	}

	public void generate() {
		generateApp();
		generateLib();
	}


	private void generateApp() {
		writeJavaAppClasses();
		writeXMLAppClasses();
		writeGradleAppClasses();
	}

	private void generateLib() {
		writeJavaLibClasses();
		writeXMLLibClasses();
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
		files.forEach(GeneratedFile::generateAndWrite);
	}

}
