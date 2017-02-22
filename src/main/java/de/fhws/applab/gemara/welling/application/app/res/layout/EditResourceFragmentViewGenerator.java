package de.fhws.applab.gemara.welling.application.app.res.layout;

public class EditResourceFragmentViewGenerator extends InputResourceFragmentViewGenerator {

	public EditResourceFragmentViewGenerator(String directoryName, String resourceName, String packageName) {
		super(directoryName, "fragment_edit_" + resourceName.toLowerCase(), resourceName, packageName);
	}
}