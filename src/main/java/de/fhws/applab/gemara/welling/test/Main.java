package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.welling.AbstractModelClass;
import de.fhws.applab.gemara.welling.AppCompatActivityClass;
import de.fhws.applab.gemara.welling.GeneratedFile;
import de.fhws.applab.gemara.welling.application.app.fragment.ListFragmentGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.adapter.ListAdapterGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.customView.ResourceCardViewGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.ResourceGenerator;
import de.fhws.applab.gemara.welling.application.lib.specific.java.viewholder.ListViewHolderGenerator;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.metaModel.InputException;
import de.fhws.applab.gemara.welling.test.generators.LibGenerator;
import de.fhws.applab.gemara.welling.test.generators.ProjectGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static final String startDir = "gemara/android/src-gen/generated/";
	private static ProjectGenerator projectGenerator;
	private static LibGenerator libGenerator;
	private static AndroidMetaModel model;

	public static void main(String[] args) throws InputException {
		model = MetaModelGenerator.generateMetaModel();
		libGenerator = new LibGenerator(model);
		projectGenerator = new ProjectGenerator(model.getApplicationName().toLowerCase() + "_lib");
		writeFiles();
	}

	private static List<AbstractModelClass> getJavaClasses() {

		List<AbstractModelClass> list = new ArrayList<>();
		list.add(new AppCompatActivityClass(model.getPackageName(), "MainActivity"));
		list.add(new ResourceGenerator(model.getPackageName(), model.getAppResources().get(0)));
		list.add(new ListAdapterGenerator(model.getPackageName(), model.getAppResources().get(0).getResourceName()));
		list.add(new ListViewHolderGenerator(model.getPackageName(), model.getAppResources().get(0)));
		list.add(new ResourceCardViewGenerator(model.getPackageName(), model.getAppResources().get(0)));
		list.add(new ListFragmentGenerator(model.getPackageName(), model.getAppResources().get(0)));
		list.add(new ListViewHolderGenerator(model.getPackageName(), model.getAppResources().get(0)));
		return list;
	}

	private static void writeFiles() {
		writeProjectFiles();
		writeApp();
		writeLib();
	}

	private static void writeProjectFiles() {
		writeGeneratedFiles(projectGenerator.getGradleClasses());
	}

	private static void writeApp() {
		writeJavaAppClasses();
	}

	private static void writeLib() {
		writeJavaLibClasses();
		writeXMLLibClasses();
		writeGradleLibClasses();
	}

	private static void writeGradleLibClasses() {
		writeGeneratedFiles(libGenerator.getGradleClasses());
	}

	private static void writeXMLLibClasses() {
		writeGeneratedFiles(libGenerator.getXMLClasses());
	}

	private static void writeJavaLibClasses() {
		writeJavaFiles(libGenerator.getJavaClasses(), startDir + model.getApplicationName().toLowerCase() + "_lib/src/main/java/");
	}

	private static void writeJavaAppClasses() {
		writeJavaFiles(getJavaClasses(), startDir + "app/src/main/java/");
	}

	private static void writeJavaFiles(List<AbstractModelClass> classes, String dir) {
		for (AbstractModelClass aClass : classes) {
			File file = new File(dir);
			try {
				aClass.javaFile().writeTo(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void writeGeneratedFiles(List<GeneratedFile> files) {
		files.forEach(de.fhws.applab.gemara.welling.GeneratedFile::generateAndWrite);
	}

}
