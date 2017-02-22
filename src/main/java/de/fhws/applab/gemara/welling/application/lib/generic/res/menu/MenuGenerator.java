package de.fhws.applab.gemara.welling.application.lib.generic.res.menu;

import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuGenerator extends GeneratedFile {

	protected abstract List<MenuItem> addMenuItems();
	protected abstract List<MenuGroup> addMenuGroups();

	protected final String fileName;
	protected final String directoryName;
	protected final AppDescription appDescription;

	public MenuGenerator(String fileName, AppDescription appDescription) {
		this.appDescription = appDescription;
		this.fileName = fileName + ".xml";
		this.directoryName = appDescription.getLibResDirectory() + "/menu";
	}

	@Override
	public void generate() {
		appendln("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		appendln("<menu xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
				+ "\t  xmlns:app=\"http://schemas.android.com/apk/res-auto\">");
		addGroups(addMenuGroups());
		addItems(addMenuItems());
		appendln("</menu>");
	}

	@Override
	protected String getFileName() {
		return this.fileName;
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}

	protected void addString(String key, String value) {
		appDescription.setLibStrings(key, value);

	}

	private void addGroups(List<MenuGroup> menuGroups) {
		for (MenuGroup menuGroup : menuGroups) {
			appendln("<group>");
			addItems(menuGroup.getItems());
			appendln("</group>");
		}

	}

	private void addItems(List<MenuItem> menuItems) {
		for (MenuItem menuItem : menuItems) {
			appendln("<item android:id=\"" + menuItem.getId() + "\"");
			appendln("android:title=\"" + menuItem.getTitle() + "\"");
			addAttributeIfExists("android:icon=", menuItem.getIcon());
			addAttributeIfExists("app:showAsAction=", menuItem.getShowAsAction());
			appendln("/>");
		}
	}

	private void addAttributeIfExists(String prefix, String attribute) {
		if (attribute != null) {
			appendln(prefix + "\"" + attribute + "\"");
		}
	}

	public class MenuItem {
		private final String id;
		private final String title;
		private String icon;
		private String showAsAction;

		public MenuItem(String id, String title) {
			this.id = id;
			this.title = title;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getShowAsAction() {
			return showAsAction;
		}

		public void setShowAsAction(String showAsAction) {
			this.showAsAction = showAsAction;
		}
	}

	public class MenuGroup {
		private final List<MenuItem> items;

		public MenuGroup() {
			this.items = new ArrayList<>();
		}

		public List<MenuItem> getItems() {
			return items;
		}

		public void addItem(MenuItem items) {
			this.items.add(items);
		}
	}
}