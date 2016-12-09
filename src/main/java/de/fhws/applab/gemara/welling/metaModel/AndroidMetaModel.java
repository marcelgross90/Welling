package de.fhws.applab.gemara.welling.metaModel;

public class AndroidMetaModel {

	private String packageName;
	private String applicationName;

	public AndroidMetaModel(String packageName, String applicationName) {
		this.packageName = packageName;
		this.applicationName = applicationName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
