package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.List;

public abstract class ResourceViewGenerator<T> {

	protected final T resourceView;
	protected final AppDescription appDescription;
	protected final StateHolder stateHolder;

	protected final String appJavaDirectory;
	protected final String libPackageName;
	protected final String libResDirectory;
	protected final String libJavaDirectory;

	protected abstract List<AbstractModelClass> getLibJavaClasses();
	protected abstract List<GeneratedFile> getLibXMLClasses();
	protected abstract List<AbstractModelClass> getAppJavaClasses();
	protected abstract void addStrings();

	public ResourceViewGenerator(T resourceView, AppDescription appDescription, StateHolder stateHolder) {
		this.resourceView = resourceView;
		this.appDescription = appDescription;
		this.stateHolder = stateHolder;

		this.appJavaDirectory = appDescription.getAppJavaDirectory();
		this.libPackageName = appDescription.getLibPackageName();
		this.libResDirectory = appDescription.getLibResDirectory();
		this.libJavaDirectory = appDescription.getLibJavaDirectory();
	}

	public void generate() {
		FileWriter.writeJavaFiles(getLibJavaClasses(), libJavaDirectory);
		FileWriter.writeGeneratedFiles(getLibXMLClasses());

		FileWriter.writeJavaFiles(getAppJavaClasses(), appJavaDirectory);
		addStrings();
	}
}