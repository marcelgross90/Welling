package de.fhws.applab.gemara.welling.metaModel;

import de.fhws.applab.gemara.welling.application.lib.specific.model.Attribute;

import java.util.ArrayList;
import java.util.List;

public class AppResource {

	private String resourceName;
	private boolean containsImage;
	private final List<Attribute> attributes = new ArrayList<>();

	public AppResource(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public boolean isContainsImage() {
		return containsImage;
	}

	public void setContainsImage(boolean containsImage) {
		this.containsImage = containsImage;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes.addAll(attributes);
	}
}
