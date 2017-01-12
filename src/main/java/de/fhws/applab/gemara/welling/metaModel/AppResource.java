package de.fhws.applab.gemara.welling.metaModel;

import de.fhws.applab.gemara.welling.application.lib.specific.java.model.Attribute;
import de.fhws.applab.gemara.welling.metaModel.view.AppCardView;
import de.fhws.applab.gemara.welling.metaModel.view.AppDetailCardView;

import java.util.ArrayList;
import java.util.List;

public class AppResource {

	private final String resourceName;
	private boolean containsImage;
	private final List<Attribute> attributes = new ArrayList<>();
	private AppCardView appCardView;
	private AppDetailCardView appDetailCardView;

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

	public AppCardView getAppCardView() {
		return appCardView;
	}

	public void setAppCardView(AppCardView appCardView) {
		this.appCardView = appCardView;
	}

	public AppDetailCardView getAppDetailCardView() {
		return appDetailCardView;
	}

	public void setAppDetailCardView(AppDetailCardView appDetailCardView) {
		this.appDetailCardView = appDetailCardView;
	}
}
