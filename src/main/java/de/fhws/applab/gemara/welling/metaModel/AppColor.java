package de.fhws.applab.gemara.welling.metaModel;

public class AppColor {

	private String colorPrimary;
	private String colorPrimaryDark;
	private String colorAccent;
	private String toolbarTextColor;
	private String cardLinkColor;

	public AppColor(String colorPrimary, String colorPrimaryDark, String colorAccent, String toolbarTextColor, String cardLinkColor) throws InputException {
		setColorPrimary(colorPrimary);
		setColorPrimaryDark(colorPrimaryDark);
		setColorAccent(colorAccent);
		setToolbarTextColor(toolbarTextColor);
		setCardLinkColor(cardLinkColor);
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

	public String getCardLinkColor() {
		return cardLinkColor;
	}

	public void setCardLinkColor(String cardLinkColor) throws InputException {
		validateInput(cardLinkColor);
		this.cardLinkColor = cardLinkColor;
	}

	private void validateInput(String input) throws InputException {
		if (!input.startsWith("#")) {
			throw new InputException("Colors must be represented as hex");
		}
	}
}
