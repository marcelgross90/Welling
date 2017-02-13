package de.fhws.applab.gemara.welling.generator.depricated;

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

	public AndroidApplicationGenerator(AndroidMetaModel model) {
		this.model = model;

		this.libGenerator = new LibGenerator(model);
	}

	public void generate() {
		generateLib();
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
