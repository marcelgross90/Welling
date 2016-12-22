package de.fhws.applab.gemara.welling.metaModel;

import java.util.HashMap;
import java.util.Map;

public class AppRestAPI {

	private final Map<String, String> restApi = new HashMap<>();

	public AppRestAPI(String baseUrl) {
		restApi.put("entry_url", baseUrl);
		restApi.put("rel_type_next", "next");
		restApi.put("rel_type_self", "self");
	}

	public Map<String, String> getRestApi() {
		return restApi;
	}

	public void setRestApi(Map<String, String> api) {
		restApi.putAll(api);
	}

	public void addState(String relTypeKey, String relType) {
		restApi.put(relTypeKey, relType);
	}
}
