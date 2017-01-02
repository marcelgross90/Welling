package de.fhws.applab.gemara.welling.metaModel.view;

import java.util.ArrayList;
import java.util.List;

public class AppCardView {

	private final List<ViewObject> viewAttributes = new ArrayList<>();

	public AppCardView() {
	}

	public List<ViewObject> getViewAttributes() {
		return viewAttributes;
	}

	public void setViewAttributes(List<ViewObject> viewAttributes) {
		this.viewAttributes.addAll(viewAttributes);
	}

	public void addViewAttribute(ViewObject viewAttribute) {
		this.viewAttributes.add(viewAttribute);
	}
}
