package de.fhws.applab.gemara.welling.generator.constantsGenerator;

import de.fhws.applab.gemara.welling.metaModelExtension.AppStyle;

import java.util.ArrayList;
import java.util.List;

public class StyleGenerator {

	public static AppStyle getLibStyle() {
		AppStyle.Style appTheme = new AppStyle.Style("AppTheme");
		appTheme.setParent("Theme.AppCompat.Light.NoActionBar");
		appTheme.setItem("colorPrimary", "@color/colorPrimary");
		appTheme.setItem("colorPrimaryDark", "@color/colorPrimaryDark");
		appTheme.setItem("colorAccent", "@color/colorAccent");

		AppStyle.Style toolbarTheme = new AppStyle.Style("ToolbarTheme");
		toolbarTheme.setParent("ThemeOverlay.AppCompat.Dark.ActionBar");
		toolbarTheme.setItem("android:textColorPrimary", "@color/toolbar_text");

		AppStyle.Style cardView = new AppStyle.Style("cardView");
		cardView.setItem("android:layout_width", "match_parent");
		cardView.setItem("android:layout_height", "wrap_content");
		cardView.setItem("cardCornerRadius", "1dp");
		cardView.setItem("android:layout_marginBottom", "10dp");
		cardView.setItem("cardElevation", "5dp");

		AppStyle.Style detailCardView = new AppStyle.Style("detailCardView");
		cardView.setItem("android:layout_width", "match_parent");
		cardView.setItem("android:layout_height", "wrap_content");
		cardView.setItem("cardCornerRadius", "1dp");
		cardView.setItem("cardElevation", "5dp");

		AppStyle.Style appBarOverlay = new AppStyle.Style("AppTheme.AppBarOverlay");
		appBarOverlay.setParent("ThemeOverlay.AppCompat.Dark.ActionBar");

		AppStyle.Style popupOverlay = new AppStyle.Style("AppTheme.PopupOverlay");
		popupOverlay.setParent("ThemeOverlay.AppCompat.Light");

		List<AppStyle.Style> styles = new ArrayList<>();
		styles.add(appTheme);
		styles.add(toolbarTheme);
		styles.add(cardView);
		styles.add(detailCardView);
		styles.add(appBarOverlay);
		styles.add(popupOverlay);

		return new AppStyle(styles);
	}

	public static AppStyle getAppStyle() {
		AppStyle.Style appTheme = new AppStyle.Style("AppTheme");
		appTheme.setParent("Theme.AppCompat.Light.NoActionBar");
		appTheme.setItem("colorPrimary", "@color/colorPrimary");
		appTheme.setItem("colorPrimaryDark", "@color/colorPrimaryDark");
		appTheme.setItem("colorAccent", "@color/colorAccent");

		AppStyle.Style appBarOverlay = new AppStyle.Style("AppTheme.AppBarOverlay");
		appBarOverlay.setParent("ThemeOverlay.AppCompat.Dark.ActionBar");

		AppStyle.Style popupOverlay = new AppStyle.Style("AppTheme.PopupOverlay");
		popupOverlay.setParent("ThemeOverlay.AppCompat.Light");

		List<AppStyle.Style> styles = new ArrayList<>();
		styles.add(appTheme);

		styles.add(appBarOverlay);
		styles.add(popupOverlay);

		return new AppStyle(styles);
	}
}