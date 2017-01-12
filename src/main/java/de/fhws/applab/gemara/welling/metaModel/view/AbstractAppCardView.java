package de.fhws.applab.gemara.welling.metaModel.view;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAppCardView {

	private final List<ViewObject> viewAttributes = new ArrayList<>();

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
