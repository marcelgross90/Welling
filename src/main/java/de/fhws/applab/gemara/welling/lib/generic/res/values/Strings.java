package de.fhws.applab.gemara.welling.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModel.AppString;

import java.util.Map;

public class Strings extends ValueGenerator {

	private AppString appString;

	public Strings(String directoryName, AppString appString) {
		super("strings", directoryName);
		this.appString = appString;
	}

	@Override
	public void generateBody() {
		Map<String, String> strings = appString.getStrings();

		for (String key : strings.keySet()) {
			appendln("<string name=\"" + key + "\">" + strings.get(key) + "</string>");
		}
	}
}