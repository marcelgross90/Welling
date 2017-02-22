package de.fhws.applab.gemara.welling.application.lib.generic.res.menu;

import de.fhws.applab.gemara.welling.generator.AppDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListMenu extends MenuGenerator {

	public ListMenu(AppDescription appDescription) {
		super("list_menu", appDescription);
	}

	@Override
	protected List<MenuItem> addMenuItems() {
		addString("add", "Add");
		MenuItem item = new MenuItem("@+id/add", "@string/add");
		item.setIcon("@drawable/ic_add");
		item.setShowAsAction("always");
		List<MenuItem> menuItems = new ArrayList<>();
		menuItems.add(item);
		return menuItems;
	}

	@Override
	protected List<MenuGroup> addMenuGroups() {
		return Collections.emptyList();
	}
}