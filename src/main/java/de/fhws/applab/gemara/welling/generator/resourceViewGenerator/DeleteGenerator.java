package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.welling.application.lib.generic.java.fragment.DeleteDialogFragment;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;

public class DeleteGenerator {

	private final AppDescription appDescription;
	private final String libJavaDirectory;

	public DeleteGenerator(AppDescription appDescription) {
		this.appDescription = appDescription;
		this.libJavaDirectory = appDescription.getLibJavaDirectory();
	}

	public void generate() {
		FileWriter.writeJavaFiles(new DeleteDialogFragment(appDescription), libJavaDirectory);
	}
}
