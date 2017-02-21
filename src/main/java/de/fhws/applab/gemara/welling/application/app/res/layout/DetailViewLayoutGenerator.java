package de.fhws.applab.gemara.welling.application.app.res.layout;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;

import java.util.List;

public class DetailViewLayoutGenerator extends AbstractLayoutGenerator {

	private final String resourceName;
	private final String packageName;

	public DetailViewLayoutGenerator(AppDescription appDescription, String resourceName, String viewName) {
		super(viewName, appDescription.getAppResDirectory());

		this.resourceName = resourceName;
		this.packageName = appDescription.getLibPackageName();
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
