package de.fhws.applab.gemara.welling.metaModelExtension;

import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resource {

	private SingleResource resource;
	private Map<String, SingleResource> subResources = new HashMap<>();

	public Resource(SingleResource resource) {
		this.resource = resource;
	}

	public SingleResource getResource() {
		return resource;
	}

	public Map<String, SingleResource> getSubResources() {
		return subResources;
	}

	public SingleResource getSubResource(String resourceName) {
		return subResources.get(resourceName);
	}

	public void setSubResources(List<SingleResource> subResources) {
		subResources.forEach(this::setSubResources);
	}

	public void setSubResources(SingleResource subResource) {
		this.subResources.put(subResource.getResourceName(), subResource);
	}
}
