package de.fhws.applab.gemara.welling.metaModelExtension;

import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;

import java.util.HashMap;
import java.util.Map;

public class AppResource {

	private final Map<String, Resource> resources = new HashMap<>();

	public Map<String, Resource> getResources() {
		return resources;
	}

	public Resource getResource(String resourceName) {
		return resources.get(resourceName);
	}

	public void setResources(SingleResource singleResource) {
		this.resources.put(singleResource.getResourceName(), new Resource(singleResource));
	}

	public void addSubResource(SingleResource resource, SingleResource subResource) {
		this.resources.get(resource.getResourceName()).setSubResources(subResource);
	}
}