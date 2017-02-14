package de.fhws.applab.gemara.welling.test.depricated;

import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.DateTimeView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DateTimePickerFragment;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;

import java.util.ArrayList;
import java.util.List;

public class LibGenerator {

	private final String baseDir;
	private final String resDir;

	public LibGenerator() {
		this.baseDir = "generated/" + "APPLICATION_NAME" + "_lib/";
		String xmlBaseDir = baseDir + "src/main/";
		this.resDir = xmlBaseDir + "res/";

	}

	public List<AbstractModelClass> getJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getGenericAndroidClasses());

		return classes;
	}

	public List<AbstractModelClass> getGenericAndroidClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getGenericCustomViews());
		classes.addAll(getGenericFragments());

		return classes;
	}

	private List<AbstractModelClass> getGenericCustomViews() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new DateTimeView("LIBPACKAGE_NAME"));

		return classes;
	}

	private List<AbstractModelClass> getGenericFragments() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new DateTimePickerFragment("LIBPACKAGE_NAME"));
		return classes;
	}

}
