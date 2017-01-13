package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.List;

public class ViewResourceDetailActivityGenerator extends AbstractLayoutGenerator {

	private final String resourceName;
	private final String packageName;

	public ViewResourceDetailActivityGenerator(String directoryName, String resourceName, String packagenName) {
		super("view_" + resourceName.toLowerCase() + "_detail", directoryName);
		this.resourceName = resourceName;
		this.packageName = packagenName;
	}

	@Override
	protected View generateLayout() {
		View view = new View("android.support.design.widget.CoordinatorLayout");

		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("xmlns:app=\"http://schemas.android.com/apk/res-auto\"");

		view.setViewAttributes(viewAttributes);

		view.addSubView(getAppBarLayout());
		view.addSubView(getRecyclerView());

		return view;
	}

	private View getRecyclerView() {
		View view = new View("android.support.v7.widget.RecyclerView");

		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("android:id=\"@+id/rv" + resourceName + "Details\"");
		viewAttributes.add("android:scrollbars=\"none\"");
		viewAttributes.add("app:layout_behavior=\"@string/appbar_scrolling_view_behavior\"");
		view.setViewAttributes(viewAttributes);


		return view;
	}

	private View getAppBarLayout() {
		View view = new View("android.support.design.widget.AppBarLayout");

		List<String> viewAttributes = getLayoutAttributes("match_parent", "300dp");
		viewAttributes.add("android:id=\"@+id/" + resourceName.toLowerCase() + "_detail_appbar\"");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("xmlns:app=\"http://schemas.android.com/apk/res-auto\"");
		viewAttributes.add("android:theme=\"@style/AppTheme.AppBarOverlay\"");
		view.setViewAttributes(viewAttributes);

		view.addSubView(getCollapsingToolbarLayout());

		return view;
	}

	private View getCollapsingToolbarLayout() {
		View view = new View("android.support.design.widget.CollapsingToolbarLayout");

		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("android:id=\"@+id/" + resourceName.toLowerCase() + "_detail_collapsing_toolbar\"");
		viewAttributes.add("app:contentScrim=\"?attr/colorPrimary\"");
		viewAttributes.add("app:layout_scrollFlags=\"scroll|exitUntilCollapsed\"");

		view.setViewAttributes(viewAttributes);

		view.addSubView(getProfileImageView());
		view.addSubView(getToolbar());

		return view;
	}

	private View getProfileImageView() {
		View view = new View(packageName + ".generic.customView.ProfileImageView");

		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("android:id=\"@+id/iv" + resourceName + "Picture\"");
		viewAttributes.add("android:contentDescription=\"@string/profile_image\"");
		viewAttributes.add("android:fitsSystemWindows=\"true\"");
		viewAttributes.add("android:scaleType=\"centerCrop\"");
		viewAttributes.add("app:layout_collapseMode=\"parallax\"");

		view.setViewAttributes(viewAttributes);
		return view;
	}

	private View getToolbar() {
		View view = new View("android.support.v7.widget.Toolbar");

		List<String> viewAttributes = getLayoutAttributes("match_parent", "?attr/actionBarSize");
		viewAttributes.add("android:id=\"@+id/toolbar\"");
		viewAttributes.add("android:background=\"#00000000\"");
		viewAttributes.add("app:layout_collapseMode=\"pin\"");
		viewAttributes.add("app:popupTheme=\"@style/AppTheme.PopupOverlay\"");

		view.setViewAttributes(viewAttributes);

		return view;
	}
}
