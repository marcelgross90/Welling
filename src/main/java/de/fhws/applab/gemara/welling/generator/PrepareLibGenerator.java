package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.java.activity.AbstractMainActivity;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.AttributeView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.customView.ProfileImageView;
import de.fhws.applab.gemara.welling.application.lib.generic.java.model.Link;
import de.fhws.applab.gemara.welling.application.lib.generic.java.model.Resource;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.HeaderParser;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkCallback;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkClient;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkRequest;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.NetworkResponse;
import de.fhws.applab.gemara.welling.application.lib.generic.java.network.OKHttpSingleton;
import de.fhws.applab.gemara.welling.application.lib.generic.java.util.FragmentHandler;
import de.fhws.applab.gemara.welling.application.lib.generic.java.util.GensonBuilder;
import de.fhws.applab.gemara.welling.application.lib.generic.java.util.ScrollListener;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.ActivityMain;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.Toolbar;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Dimens;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Styles;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.generator.conastantsGenerator.StyleGenerator;
import de.fhws.applab.gemara.welling.metaModel.AppAndroidManifest;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;

import java.util.ArrayList;
import java.util.List;

public class PrepareLibGenerator {

	private final AppDescription appDescription;


	public PrepareLibGenerator(AppDescription appDescription) {
		this.appDescription = appDescription;

		copyDrawableFolders();
	}

	public void generate() {
		FileWriter.writeJavaFiles(getJavaFiles(), appDescription.getLibJavaDirectory());
		FileWriter.writeGeneratedFiles(getGeneratedFiles());

	}

	private List<AbstractModelClass> getJavaFiles() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.addAll(getGenericModels());
		classes.addAll(getGenericNetwork());
		classes.addAll(getGenericUtil());
		classes.addAll(getGenericCustomViews());
		classes.add(new AbstractMainActivity(appDescription.getLibPackageName()));

		return classes;
	}

	private List<AbstractModelClass> getGenericModels() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new Link(appDescription.getLibPackageName()));
		classes.add(new Resource(appDescription.getLibPackageName()));

		return classes;
	}

	private List<AbstractModelClass> getGenericNetwork() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new HeaderParser(appDescription.getLibPackageName()));
		classes.add(new NetworkCallback(appDescription.getLibPackageName()));
		classes.add(new NetworkClient(appDescription.getLibPackageName()));
		classes.add(new NetworkRequest(appDescription.getLibPackageName()));
		classes.add(new NetworkResponse(appDescription.getLibPackageName()));
		classes.add(new OKHttpSingleton(appDescription.getLibPackageName()));

		return classes;
	}

	private List<AbstractModelClass> getGenericUtil() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new FragmentHandler(appDescription.getLibPackageName()));
		classes.add(new GensonBuilder(appDescription.getLibPackageName()));
		classes.add(new ScrollListener(appDescription.getLibPackageName()));

		return classes;
	}

	private List<AbstractModelClass> getGenericCustomViews() {
		List<AbstractModelClass> classes = new ArrayList<>();
		classes.add(new AttributeView(appDescription.getLibPackageName()));
		classes.add(new ProfileImageView(appDescription.getLibPackageName()));

		addDeclareStyleables(classes);
		return classes;
	}

	private List<GeneratedFile> getGeneratedFiles() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new Dimens(appDescription.getLibResDirectory()));
		classes.add(getManifest());
		classes.add(getStyle());
		classes.addAll(getLayoutClasses());

		return classes;
	}

	private GeneratedFile getManifest() {
		return new ManifestGenerator(appDescription.getLibManifestDirectory(), new AppAndroidManifest(appDescription.getLibPackageName()));
	}

	private GeneratedFile getStyle() {
		return new Styles(appDescription.getLibResDirectory(), StyleGenerator.getLibStyle());
	}

	private List<GeneratedFile> getLayoutClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new Toolbar(appDescription.getLibResDirectory()));
		classes.add(new ActivityMain(appDescription.getLibResDirectory()));

		return classes;
	}

	private void addDeclareStyleables(List<AbstractModelClass> customViews) {
		for (AbstractModelClass customView : customViews) {
			appDescription.setDeclareStyleables(new AppDeclareStyleable.DeclareStyleable(customView.getClassName()));
		}

	}

	private void copyDrawableFolders() {
		Copy copy = new Copy();
		copy.copySubFolderOnly("drawable_lib", appDescription.getLibName() + "/src/main/res");
	}
}
