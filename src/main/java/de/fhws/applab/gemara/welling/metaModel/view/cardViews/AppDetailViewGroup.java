package de.fhws.applab.gemara.welling.metaModel.view.cardViews;

import de.fhws.applab.gemara.welling.metaModel.view.viewObject.ViewObject;

import java.util.ArrayList;
import java.util.List;

public class AppDetailViewGroup {

	private  final List<ViewObject> viewAttributes = new ArrayList<>();
	private final String headline;

	public AppDetailViewGroup(String headline) {
		this.headline = headline;
	}

	public String getHeadline() {
		return headline;
	}

	public void setViewAttributes(List<ViewObject> viewAttributes) {
		this.viewAttributes.addAll(viewAttributes);
	}

	public List<ViewObject> getViewAttributes() {
		return viewAttributes;
	}
}
