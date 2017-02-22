package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import java.util.List;

public class CustomCardViewLayoutGenerator extends AbstractLayoutGenerator {

	private final String packageName;
	private final String viewType;
	private final String layoutId;

	public CustomCardViewLayoutGenerator(String directoryName, String packageName, String viewName, String viewType, String layoutId) {
		super(viewName, directoryName);
		this.packageName = packageName;
		this.viewType = viewType;
		this.layoutId = layoutId;
	}

	@Override
	protected View generateLayout() {
		View view = new View(packageName + ".specific.customView." + viewType);
		List<String> viewAttributes = getLayoutAttributes("match_parent", "wrap_content");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("android:id=\"@+id/" + layoutId + "\"");
		viewAttributes.add("android:layout_margin=\"@dimen/spacing_small\"");
		view.setViewAttributes(viewAttributes);
		return view;
	}
}