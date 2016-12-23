package de.fhws.applab.gemara.welling.application.lib.generic.res.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListMenu extends MenuGenerator {

	public ListMenu(String directoryName) {
		super("list_menu", directoryName);
	}

	@Override
	protected List<MenuItem> addMenuItems() {
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
