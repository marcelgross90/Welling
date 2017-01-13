package de.fhws.applab.gemara.welling.application.app.res.layout;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.List;

public class ActivityDetailViewGenerator extends AbstractLayoutGenerator {

	private final String resourceName;
	private final String packageName;

	public ActivityDetailViewGenerator(String directoryName, String resourceName, String packageName) {
		super("activity_" + resourceName.toLowerCase() + "_detail", directoryName);
		this.resourceName = resourceName;
		this.packageName = packageName;
	}

	@Override
	protected View generateLayout() {
		View detailView = new View(packageName + ".specific.customView." + resourceName + "DetailView");
		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("android:id=\"@+id/detail_view\"");
		viewAttributes.add("android:fitsSystemWindows=\"true\"");

		detailView.setViewAttributes(viewAttributes);

		return detailView;
	}
}
