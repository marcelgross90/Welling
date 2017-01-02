package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import java.util.List;

public class Toolbar extends AbstractLayoutGenerator {

	public Toolbar(String directoryName) {
		super("toolbar", directoryName);
	}

	@Override
	protected View generateLayout() {
		View view = new View("android.support.v7.widget.Toolbar");
		List<String> attributes = getLayoutAttributes("match_parent", "?attr/actionBarSize");
		attributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		attributes.add("xmlns:app=\"http://schemas.android.com/apk/res-auto\"");
		attributes.add("xmlns:tools=\"http://schemas.android.com/tools\"");
		attributes.add("tools:ignore=\"Overdraw\"");
		attributes.add("android:id=\"@+id/toolbar\"");
		attributes.add("android:background=\"?attr/colorPrimary\"");
		attributes.add("app:theme=\"@style/ToolbarTheme\"");
		attributes.add("app:popupTheme=\"@style/ThemeOverlay.AppCompat.Light\"");

		view.setViewAttributes(attributes);

		return view;
	}
}
