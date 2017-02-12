package de.fhws.applab.gemara.welling.application.app.res.layout;

public class NewResourceFragmentViewGenerator extends InputResourceFragmentViewGenerator {

	public NewResourceFragmentViewGenerator(String directoryName, String resourceName, String packageName) {
		super(directoryName, "fragment_new_" + resourceName.toLowerCase(), resourceName, packageName);
	}
}