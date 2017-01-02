package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import java.util.List;

public class FragmentResourceList extends AbstractLayoutGenerator {

	public FragmentResourceList(String directoryName) {
		super("fragment_resource_list", directoryName);
	}

	@Override
	protected View generateLayout() {
		View view = new View("FrameLayout");
		List<String> attributes = getLayoutAttributes("match_parent", "match_parent");
		attributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");

		view.setViewAttributes(attributes);
		view.addSubView(generateRecyclerView());
		view.addSubView(generateProgressBar());

		return view;
	}

	private View generateRecyclerView() {
		View view = new View("android.support.v7.widget.RecyclerView");
		List<String> attributes = getLayoutAttributes("match_parent", "match_parent");
		attributes.add("android:id=\"@+id/resource_recycler_view\"");
		attributes.add("android:scrollbars=\"vertical\"");

		view.setViewAttributes(attributes);

		return view;
	}

	private View generateProgressBar() {
		View view = new View("ProgressBar");
		List<String> attributes = getLayoutAttributes("wrap_content", "wrap_content");
		attributes.add("android:layout_gravity=\"bottom|center\"");
		attributes.add("android:visibility=\"gone\"");
		attributes.add("android:layout_marginBottom=\"30dp\"");
		attributes.add("android:id=\"@+id/progressBar\"");

		view.setViewAttributes(attributes);

		return view;
	}
}
