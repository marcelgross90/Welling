package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.welling.application.app.java.MainActivity;
import de.fhws.applab.gemara.welling.application.app.res.anim.FadeIn;
import de.fhws.applab.gemara.welling.application.app.res.anim.FadeOut;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Styles;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;
import de.fhws.applab.gemara.welling.generator.conastantsGenerator.StyleGenerator;

import java.util.ArrayList;
import java.util.List;

public class PrepareAppGenerator {

	private final AppDescription appDescription;
	private final String startResourceName;

	public PrepareAppGenerator(AppDescription appDescription, String startResourceName) {
		this.appDescription = appDescription;
		this.startResourceName = startResourceName;

		copyMipMapFolder();
		addInitialStrings();
	}

	public void generate() {
		FileWriter.writeJavaFiles(getMainActivity(), appDescription.getAppJavaDirectory());
		FileWriter.writeGeneratedFiles(getAnimClasses());
		FileWriter.writeGeneratedFiles(getStyle());
	}

	private AbstractModelClass getMainActivity() {
		return new MainActivity(appDescription.getAppPackageName(), startResourceName, appDescription.getAppName());
	}

	private List<GeneratedFile> getAnimClasses() {
		List<GeneratedFile> classes = new ArrayList<>();

		classes.add(new FadeIn(appDescription.getAppResDirectory()));
		classes.add(new FadeOut(appDescription.getAppResDirectory()));

		return classes;
	}

	private GeneratedFile getStyle() {
		return new Styles(appDescription.getAppResDirectory(), StyleGenerator.getAppStyle());
	}

	private void copyMipMapFolder() {
		Copy copy = new Copy();
		copy.copySubFolderOnly("mipmap_app", "app/src/main/res");
	}

	private void addInitialStrings() {
		appDescription.setAppStrings("app_name", appDescription.getAppName());
	}
}
