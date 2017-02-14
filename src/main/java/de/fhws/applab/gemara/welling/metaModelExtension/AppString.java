package de.fhws.applab.gemara.welling.metaModelExtension;

import java.util.HashMap;
import java.util.Map;

public class AppString {

	private final Map<String, String> strings = new HashMap<>();

	public AppString(Map<String, String> strings) {
		this.strings.putAll(strings);
	}

	public AppString() {
	}

	public void setStrings(Map<String, String> strings) {
		this.strings.putAll(strings);
	}

	public void setStrings(String key, String value) {
		this.strings.put(key, value);
	}

	public Map<String, String> getStrings() {
		return strings;
	}
}
