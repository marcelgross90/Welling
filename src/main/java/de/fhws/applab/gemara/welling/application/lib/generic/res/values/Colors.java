package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModelExtension.AppColor;

public class Colors extends ValueGenerator {

	private final AppColor appColor;

	public Colors(String directoryName, AppColor appColor) {
		super("colors", directoryName);
		this.appColor = appColor;
	}

	@Override
	public void generateBody() {
		appendln("<color name=\"colorPrimary\">" + appColor.getColorPrimary() + "</color>");
		appendln("<color name=\"colorPrimaryDark\">" + appColor.getColorPrimaryDark() + "</color>");
		appendln("<color name=\"colorAccent\">" + appColor.getColorAccent() + "</color>");
		appendln("<color name=\"toolbar_text\">" + appColor.getToolbarTextColor() + "</color>");
	}

}