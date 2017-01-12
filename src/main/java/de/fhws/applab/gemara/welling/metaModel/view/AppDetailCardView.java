package de.fhws.applab.gemara.welling.metaModel.view;

import java.util.ArrayList;
import java.util.List;

public class AppDetailCardView extends AbstractAppCardView {

	private List<AppDetailViewGroup> groups = new ArrayList<>();

	public AppDetailCardView() {
		super();
	}

	public List<AppDetailViewGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<AppDetailViewGroup> groups) {
		this.groups = groups;
		for (AppDetailViewGroup group : groups) {
			setViewAttributes(group.getViewAttributes());
		}
	}
}
