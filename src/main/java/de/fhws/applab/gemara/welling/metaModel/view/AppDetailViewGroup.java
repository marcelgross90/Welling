package de.fhws.applab.gemara.welling.metaModel.view;

import java.util.ArrayList;
import java.util.List;

public class AppDetailViewGroup {

	private  final List<ViewObject> viewAttributes = new ArrayList<>();
	private final String viewName;

	public AppDetailViewGroup(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewAttributes(List<ViewObject> viewAttributes) {
		this.viewAttributes.addAll(viewAttributes);
	}

	public List<ViewObject> getViewAttributes() {
		return viewAttributes;
	}
}
