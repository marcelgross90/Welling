package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.application.app.java.DetailActivityGenerator;
import de.fhws.applab.gemara.welling.application.app.java.fragment.NewSpecificResourceFragment;
import de.fhws.applab.gemara.welling.application.app.res.layout.ActivityDetailViewGenerator;
import de.fhws.applab.gemara.welling.application.app.res.layout.NewResourceFragmentViewGenerator;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.application.app.java.MainActivity;
import de.fhws.applab.gemara.welling.application.app.java.fragment.ListFragmentGenerator;
import de.fhws.applab.gemara.welling.application.app.res.anim.FadeIn;
import de.fhws.applab.gemara.welling.application.app.res.anim.FadeOut;
import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Colors;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Strings;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Styles;
import de.fhws.applab.gemara.welling.application.gradle.app.AppProguardRules;
import de.fhws.applab.gemara.welling.application.gradle.app.BuildGradleApp;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.metaModel.AppResource;

import java.util.ArrayList;
import java.util.List;

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

		List<AbstractModelClass> classes = new ArrayList<>();

		for (AppResource appResource : model.getAppResources()) {
			//todo change detailView
			classes.add(new ListFragmentGenerator(model.getPackageName(), de.fhws.applab.gemara.welling.test.modelGenerator.DetailViewGenerator.lecturer(), model.getApplicationName()));
			classes.add(new NewSpecificResourceFragment(model.getPackageName(), appResource, model.getApplicationName()));
		}

		return classes;
	}

	private List<AbstractModelClass> getActivities() {
		List<AbstractModelClass> classes = new ArrayList<>();

		classes.add(new MainActivity(model.getPackageName(), model.getAppResources().get(0), model.getApplicationName()));
		//todo change detailview
		classes.add(new DetailActivityGenerator(model.getPackageName(), de.fhws.applab.gemara.welling.test.modelGenerator.DetailViewGenerator.lecturer(), model.getApplicationName()));
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
		for (AppResource appResource : model.getAppResources()) {
			classes.add(new ActivityDetailViewGenerator(resDir, appResource.getResourceName(), model.getPackageNameLib()));
			classes.add(new NewResourceFragmentViewGenerator(resDir, appResource.getResourceName(), model.getPackageNameLib()));
		}

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
