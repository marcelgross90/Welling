package de.fhws.applab.gemara.welling.metaModel;

import java.util.HashMap;
import java.util.Map;

public class AppString {

	private final Map<String, String> strings = new HashMap<>();

	public AppString(Map<String, String> strings) {
		this.strings.putAll(strings);
	}

	public Map<String, String> getStrings() {
		return strings;
	}
}
