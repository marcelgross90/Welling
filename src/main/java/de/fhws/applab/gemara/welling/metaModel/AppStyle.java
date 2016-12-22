package de.fhws.applab.gemara.welling.metaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcelgross on 22.12.16.
 */
public class AppStyle {

	private final List<Style> styles = new ArrayList<>();

	public AppStyle(List<Style> styles) {
		this.styles.addAll(styles);
	}

	public List<Style> getStyles() {
		return styles;
	}

	public static class Style {
		private String name;
		private String parent;
		private final Map<String, String> items = new HashMap<>();

		public Style(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public Map<String, String> getItems() {
			return items;
		}

		public void setItems(Map<String, String> items) {
			this.items.putAll(items);
		}

		public void setItem(String key, String value) {
			this.items.put(key, value);
		}
	}
}
