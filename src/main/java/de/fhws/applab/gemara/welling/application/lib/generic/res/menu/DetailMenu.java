package de.fhws.applab.gemara.welling.application.lib.generic.res.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailMenu extends MenuGenerator {

	public DetailMenu(String directoryName) {
		super("detail_menu", directoryName);
	}

	@Override
	protected List<MenuItem> addMenuItems() {
		return Collections.emptyList();
	}

	@Override
	protected List<MenuGroup> addMenuGroups() {
		List<MenuGroup> menuGroups = new ArrayList<>();
		menuGroups.add(getGroup());
		return menuGroups;
	}

	private MenuItem getEditItem() {
		MenuItem menuItem = new MenuItem("@+id/edit_item", "@string/edit");
		menuItem.setIcon("@drawable/ic_edit");
		return menuItem;
	}

	private MenuItem getDeleteItem() {
		MenuItem menuItem = new MenuItem("@+id/delete_item", "@string/delete");
		menuItem.setIcon("@drawable/ic_delete");
		return menuItem;
	}

	private MenuGroup getGroup() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.addItem(getEditItem());
		menuGroup.addItem(getDeleteItem());

		return menuGroup;
	}
}
