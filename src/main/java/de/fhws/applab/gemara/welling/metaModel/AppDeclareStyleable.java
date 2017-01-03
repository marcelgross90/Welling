package de.fhws.applab.gemara.welling.metaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDeclareStyleable {

	private final List<DeclareStyleable> declareStyleables = new ArrayList<>();

	public AppDeclareStyleable(List<DeclareStyleable> declareStyleables) {
		this.declareStyleables.addAll(declareStyleables);
	}

	public List<DeclareStyleable> getDeclareStyleables() {
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
