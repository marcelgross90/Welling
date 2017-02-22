package de.fhws.applab.gemara.welling.application.app.res.layout;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.List;

public abstract class InputResourceFragmentViewGenerator extends AbstractLayoutGenerator {

	private final String resourceName;
	private final String packageName;

	@SuppressWarnings("WeakerAccess")
	public InputResourceFragmentViewGenerator(String directoryName, String fileName, String resourceName, String packageName) {
		super(fileName, directoryName);
		this.resourceName = resourceName;
		this.packageName = packageName;
	}

	@Override
	protected View generateLayout() {
		View detailView = new View(packageName + ".specific.customView." + resourceName + "InputView");
		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("android:id=\"@+id/" + resourceName.toLowerCase() + "_input\"");

		detailView.setViewAttributes(viewAttributes);

		return detailView;
	}
}