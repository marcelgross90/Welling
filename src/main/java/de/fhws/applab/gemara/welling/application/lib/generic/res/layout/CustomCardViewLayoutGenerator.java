package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import de.fhws.applab.gemara.welling.metaModel.AppResource;

import java.util.List;

public class CustomCardViewLayoutGenerator extends AbstractLayoutGenerator {

	private final AppResource appResource;
	private final String packageName;

	public CustomCardViewLayoutGenerator(String directoryName, String packageName, AppResource appResource) {
		super("card_" + appResource.getResourceName().toLowerCase(), directoryName);
		this.appResource = appResource;
		this.packageName = packageName;
	}

	@Override
	protected View generateLayout() {
		View view = new View(packageName + ".specific.customView." + appResource.getResourceName() + "CardView");
		List<String> viewAttributes = getLayoutAttributes("match_parent", "wrap_content");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("android:id=\"@+id/card_" + appResource.getResourceName().toLowerCase() + "\"");
		viewAttributes.add("android:layout_margin=\"@dimen/spacing_small\"");
		view.setViewAttributes(viewAttributes);
		return view;
	}
}
