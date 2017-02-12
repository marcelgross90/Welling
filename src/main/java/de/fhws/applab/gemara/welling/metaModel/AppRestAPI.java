package de.fhws.applab.gemara.welling.metaModel;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class AppRestAPI {

	private final Map<String, Pair<String, String>> restApi = new HashMap<>();


	public AppRestAPI(String baseUrl) {
		restApi.put("baseUrl", new Pair<>("entry_url", baseUrl));
		restApi.put("next", new Pair<>("rel_type_next", "next"));
		restApi.put("self", new Pair<>("rel_type_self", "self"));
	}

	public Map<String, Pair<String, String>> getRestApi() {
		return restApi;
	}

	public void setRestApi(String stateKey, String relTypeKey, String relType) {
		this.restApi.put(stateKey, new Pair<>(relTypeKey, relType));
	}
}
