package de.fhws.applab.gemara.welling.application.lib.generic.res.menu;

import de.fhws.applab.gemara.welling.generator.AppDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveMenu extends MenuGenerator {

	public SaveMenu(AppDescription appDescription) {
		super("save_menu", appDescription);
	}

	@Override
	protected List<MenuItem> addMenuItems() {
		addString("save", "Save");
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
