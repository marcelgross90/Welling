package de.fhws.applab.gemara.welling.metaModel.view.viewObject;

public class ViewAttribute {

	private final String attributeName;
	private final AttributeType type;
	private String label;

	public ViewAttribute(String attributeName, AttributeType type) {
		this.attributeName = attributeName;
		this.type = type;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public AttributeType getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getGetter() {
		String capitalizedResourceName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
		return "get" + capitalizedResourceName;
	}

	public String getSetter() {
		String capitalizedResourceName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
		return "set" + capitalizedResourceName;
	}
}
