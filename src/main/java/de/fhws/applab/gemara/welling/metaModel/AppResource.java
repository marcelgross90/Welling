package de.fhws.applab.gemara.welling.metaModel;

import de.fhws.applab.gemara.welling.application.lib.specific.java.model.Attribute;

import java.util.ArrayList;
import java.util.List;

public class AppResource {

	private final String resourceName;
	private final List<Attribute> attributes = new ArrayList<>();

	public AppResource(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}


	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes.addAll(attributes);
	}

}
