package de.fhws.applab.gemara.welling.metaModel.view;

public class ViewAttribute {

	private String resourceName;
	private AttributeType type;
	private String displayedName;

	public ViewAttribute(String resourceName, AttributeType type) {
		this.resourceName = resourceName;
		this.type = type;
	}


	public String getResourceName() {
		return resourceName;
	}

	public AttributeType getType() {
		return type;
	}

	public String getDisplayedName() {
		return displayedName;
	}

	public void setDisplayedName(String displayedName) {
		this.displayedName = displayedName;
	}

	public String getGetter() {
		String capitalizedResourceName = Character.toUpperCase(resourceName.charAt(0)) + resourceName.substring(1);
		return "get" + capitalizedResourceName;
	}
}
