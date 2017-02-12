package de.fhws.applab.gemara.welling.metaModel;

import java.util.HashMap;
import java.util.Map;

public class AppDeclareStyleable {

	private final Map<String, DeclareStyleable> declareStyleables = new HashMap<>();

	public AppDeclareStyleable() {
	}

	public void setDeclareStyleables(Map<String, DeclareStyleable> declareStyleables) {
		this.declareStyleables.putAll(declareStyleables);
	}

	public void setDeclareStyleables(String key, DeclareStyleable declareStyleable) {
		this.declareStyleables.put(key, declareStyleable);
	}

	public Map<String, DeclareStyleable> getDeclareStyleables() {
		return declareStyleables;
	}

	public static class DeclareStyleable {
		private final String name;
		private final Map<String, String> attr = new HashMap<>();

		public DeclareStyleable(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Map<String, String> getAttr() {
			return attr;
		}

		public void setAttr(Map<String, String> attr) {
			this.attr.putAll(attr);
		}

		public void setAttr(String name, String format) {
			this.attr.put(name, format);
		}
	}
}
