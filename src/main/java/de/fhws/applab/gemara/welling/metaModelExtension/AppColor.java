package de.fhws.applab.gemara.welling.metaModelExtension;

import de.fhws.applab.gemara.enfield.metamodel.wembley.frondentSpecifics.FrontendColor;

public class AppColor {

	private String colorPrimary;
	private String colorPrimaryDark;
	private String colorAccent;
	private String toolbarTextColor;

	public AppColor(FrontendColor frontendColor) throws InputException {
		setColorPrimary(frontendColor.getColorPrimary());
		setColorPrimaryDark(frontendColor.getColorPrimaryDark());
		setColorAccent(frontendColor.getColorAccent());
		setToolbarTextColor(frontendColor.getToolbarTextColor());
	}

	public String getColorPrimary() {
		return colorPrimary;
	}

	public void setColorPrimary(String colorPrimary) throws InputException {
		validateInput(colorPrimary);
		this.colorPrimary = colorPrimary;
	}

	public String getColorPrimaryDark() {
		return colorPrimaryDark;
	}

	public void setColorPrimaryDark(String colorPrimaryDark) throws InputException {
		validateInput(colorPrimaryDark);
		this.colorPrimaryDark = colorPrimaryDark;
	}

	public String getColorAccent() {
		return colorAccent;
	}

	public void setColorAccent(String colorAccent) throws InputException {
		validateInput(colorAccent);
		this.colorAccent = colorAccent;
	}

	public String getToolbarTextColor() {
		return toolbarTextColor;
	}

	public void setToolbarTextColor(String toolbarTextColor) throws InputException {
		validateInput(toolbarTextColor);
		this.toolbarTextColor = toolbarTextColor;
	}

	private void validateInput(String input) throws InputException {
		if (!input.startsWith("#")) {
			throw new InputException("Colors must be represented as hex");
		}
	}
}
