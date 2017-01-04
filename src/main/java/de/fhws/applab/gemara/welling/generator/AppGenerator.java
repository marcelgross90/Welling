package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.AbstractModelClass;
import de.fhws.applab.gemara.welling.GeneratedFile;
import de.fhws.applab.gemara.welling.application.app.java.MainActivity;
import de.fhws.applab.gemara.welling.application.app.java.fragment.ListFragmentGenerator;
import de.fhws.applab.gemara.welling.application.app.res.anim.FadeIn;
import de.fhws.applab.gemara.welling.application.app.res.anim.FadeOut;
import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Colors;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Strings;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Styles;
import de.fhws.applab.gemara.welling.gradle.app.AppProguardRules;
import de.fhws.applab.gemara.welling.gradle.app.BuildGradleApp;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppGenerator {

	private final AndroidMetaModel model;
	private final String baseDir;
	private final String xmlBaseDir;
	private final String resDir;

	public AppGenerator(AndroidMetaModel model) {
		this.model = model;
		this.baseDir = "generated/app/";
		this.xmlBaseDir = baseDir + "src/main/";
		this.resDir = xmlBaseDir + "res/";

		copyMipMapFolder();
	}

	public List<AbstractModelClass> getJavaClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getAndroidClasses());

		return classes;
	}

	public List<GeneratedFile> getXMLClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.addAll(getResClasses());

		return classes;
	}


	public List<GeneratedFile> getGradleClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new BuildGradleApp(baseDir, model.getPackageName(), model.getApplicationName().toLowerCase() + "_lib"));
		classes.add(new AppProguardRules(baseDir));

		return classes;
	}

	private List<AbstractModelClass> getAndroidClasses() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.addAll(getFragments());
		classes.addAll(getActivities());

		return classes;
	}

	private List<AbstractModelClass> getFragments() {
		return model.getAppResources().stream()
				.map(appResource -> new ListFragmentGenerator(model.getPackageName(), appResource, model.getApplicationName())).collect(Collectors.toList());

	}

	private List<AbstractModelClass> getActivities() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new MainActivity(model.getPackageName(), model.getAppResources().get(0), model.getApplicationName()));

		return classes;
	}


	public List<GeneratedFile> getResClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.addAll(getAnimClasses());
		classes.addAll(getLayoutClasses());
		classes.addAll(getValuesClasses());
		classes.add(getManifest());

		return classes;
	}

	private List<GeneratedFile> getAnimClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new FadeIn(resDir));
		classes.add(new FadeOut(resDir));

		return classes;
	}

	private List<GeneratedFile> getLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		//todo

		return classes;
	}

	private List<GeneratedFile> getValuesClasses() {
		List<GeneratedFile> classes = new ArrayList<>();
		classes.add(new Colors(resDir, model.getAppColor()));
		classes.add(new Strings(resDir, model.getAppStrings()));
		classes.add(new Styles(resDir, model.getAppStyles()));

		return classes;
	}


	private GeneratedFile getManifest() {
		return new ManifestGenerator(xmlBaseDir, model.getAppManifest());
	}

	private void copyMipMapFolder() {
		Copy copy = new Copy();
		copy.copySubFolderOnly("mipmap_app", "app/src/main/res");
	}
}
