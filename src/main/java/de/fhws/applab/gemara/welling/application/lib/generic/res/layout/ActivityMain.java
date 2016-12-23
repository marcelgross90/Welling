package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends LayoutGenerator {

	public ActivityMain(String directoryName) {
		super("activity_main", directoryName);
	}

	@Override
	protected View generateLayout() {
		View linearLayout = new View("LinearLayout");
		List<String> attributes = getLayoutAttributes("match_parent", "match_parent");
		attributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		attributes.add("android:orientation=\"vertical\"");
		linearLayout.setViewAttributes(attributes);
		linearLayout.addSubView(generateToolbar());
		linearLayout.addSubView(generateFrameLayout());

		return linearLayout;

	}

	private View generateToolbar() {
		View include = new View("include");
		include.addViewAttribute("layout=\"@layout/toolbar\"");

		return include;
	}

	private View generateFrameLayout() {
		View frameLayout = new View("FrameLayout");
		List<String> attributes = getLayoutAttributes("match_parent", "match_parent");
		attributes.add("android:id=\"@+id/content_container\"");
		frameLayout.setViewAttributes(attributes);

		return frameLayout;
	}

}
