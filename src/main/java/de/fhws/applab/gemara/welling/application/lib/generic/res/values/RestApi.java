package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModelExtension.AppRestAPI;
import javafx.util.Pair;

public class RestApi extends ValueGenerator {

	private final AppRestAPI appRestAPI;

	public RestApi(String directoryName, AppRestAPI appRestAPI) {
		super("rest_api", directoryName);
		this.appRestAPI = appRestAPI;
	}

	@Override
	public void generateBody() {
		for (String states : appRestAPI.getRestApi().keySet()) {
			Pair<String, String> rel = appRestAPI.getRestApi().get(states);
			appendln("<string name=\"" + rel.getKey() + "\">" + rel.getValue() + "</string>");
		}
	}
}
