package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileWriter {

	public static void writeJavaFiles(List<AbstractModelClass> classes, String dir) {
		for (AbstractModelClass aClass : classes) {
			writeJavaFiles(aClass, dir);
		}
	}

	public static void writeJavaFiles(AbstractModelClass aClass, String dir) {
		File file = new File(dir);
		try {
			aClass.javaFile().writeTo(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void writeGeneratedFiles(List<GeneratedFile> files) {
		files.forEach(GeneratedFile::generateAndWrite);
	}

	public static void writeGeneratedFiles(GeneratedFile file) {
		file.generateAndWrite();
	}
}