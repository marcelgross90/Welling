package de.fhws.applab.gemara.welling.metaModelExtension;

import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;

import java.util.HashMap;
import java.util.Map;

public class Resource {

	private final SingleResource resource;
	private final Map<String, SingleResource> subResources = new HashMap<>();

	public Resource(SingleResource resource) {
		this.resource = resource;
	}

	public SingleResource getResource() {
		return resource;
	}

	public Map<String, SingleResource> getSubResources() {
		return subResources;
	}

	public void setSubResources(SingleResource subResource) {
		this.subResources.put(subResource.getResourceName(), subResource);
	}
}
