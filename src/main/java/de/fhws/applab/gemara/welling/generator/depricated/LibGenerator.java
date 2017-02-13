package de.fhws.applab.gemara.welling.generator.depricated;

import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.DateTimeView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DateTimePickerFragment;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;

import java.util.ArrayList;
import java.util.List;

public class LibGenerator {

	private final AndroidMetaModel model;
	private final String baseDir;
	private final String resDir;

	public LibGenerator(AndroidMetaModel model) {
		this.model = model;
		this.baseDir = "generated/" + model.getApplicationName().toLowerCase() + "_lib/";
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
		classes.add(new DateTimeView(model.getPackageNameLib()));

		return classes;
	}

	private List<AbstractModelClass> getGenericFragments() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new DateTimePickerFragment(model.getPackageNameLib()));
		return classes;
	}

}
