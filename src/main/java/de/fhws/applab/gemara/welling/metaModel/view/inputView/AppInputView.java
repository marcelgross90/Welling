package de.fhws.applab.gemara.welling.metaModel.view.inputView;

import de.fhws.applab.gemara.welling.metaModel.view.viewObject.SingleViewObject;

import java.util.ArrayList;
import java.util.List;

public class AppInputView {

	public AppInputView() {
	}

	private final List<SingleViewObject> viewAttributes = new ArrayList<>();

	public List<SingleViewObject> getViewAttributes() {
		return viewAttributes;
	}

	public void setViewAttributes(List<SingleViewObject> viewAttributes) {
		this.viewAttributes.addAll(viewAttributes);
	}

	public void addViewAttribute(SingleViewObject viewAttribute) {
		this.viewAttributes.add(viewAttribute);
	}
}
