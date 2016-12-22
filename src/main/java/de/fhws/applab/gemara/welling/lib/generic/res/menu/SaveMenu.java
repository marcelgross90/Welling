package de.fhws.applab.gemara.welling.lib.generic.res.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveMenu extends MenuGenerator {

	public SaveMenu(String directoryName) {
		super("save_menu", directoryName);
	}

	@Override
	protected List<MenuItem> addMenuItems() {
		MenuItem item = new MenuItem("@+id/saveItem", "@string/save");
		item.setIcon("@drawable/ic_done");
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
