package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModel.AppRestAPI;

import java.util.Map;

public class RestApi extends ValueGenerator {

	private final AppRestAPI appRestAPI;

	public RestApi(String directoryName, AppRestAPI appRestAPI) {
		super("rest_api", directoryName);
		this.appRestAPI = appRestAPI;
	}

	@Override
	public void generateBody() {
		Map<String, String> api = appRestAPI.getRestApi();
		for (String key : api.keySet()) {
			appendln("<string name=\"" + key + "\">" + api.get(key) + "</string>");
		}
	}
}
