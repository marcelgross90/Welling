package de.fhws.applab.gemara.welling.metaModel.view;

import java.util.ArrayList;
import java.util.List;

public class AppDetailCardView extends AbstractAppCardView {

	private final ViewObject title;
	private List<AppDetailViewGroup> groups = new ArrayList<>();

	public AppDetailCardView(ViewObject title) {
		super();
		this.title = title;
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

	public ViewObject getTitle() {
		return title;
	}

}
