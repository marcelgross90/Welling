package de.fhws.applab.gemara.welling.metaModel;

public class AndroidMetaModel {

	private String packageName;
	private String applicationName;
	private AppColor appColor;
	private AppRestAPI appRestAPI;
	private AppStyle libStyles;
	private AppString libStrings;
	private AppDeclareStyleable appDeclareStyleable;
	private AppAndroidManifest libManifest;

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

	public AppColor getAppColor() {
		return appColor;
	}

	public void setAppColor(AppColor appColor) {
		this.appColor = appColor;
	}

	public AppRestAPI getAppRestAPI() {
		return appRestAPI;
	}

	public void setAppRestAPI(AppRestAPI appRestAPI) {
		this.appRestAPI = appRestAPI;
	}

	public AppStyle getLibStyles() {
		return libStyles;
	}

	public void setLibStyles(AppStyle libStyles) {
		this.libStyles = libStyles;
	}

	public AppString getLibStrings() {
		return libStrings;
	}

	public void setLibStrings(AppString libStrings) {
		this.libStrings = libStrings;
	}

	public AppDeclareStyleable getAppDeclareStyleable() {
		return appDeclareStyleable;
	}

	public void setAppDeclareStyleable(AppDeclareStyleable appDeclareStyleable) {
		this.appDeclareStyleable = appDeclareStyleable;
	}

	public AppAndroidManifest getLibManifest() {
		return libManifest;
	}

	public void setLibManifest(AppAndroidManifest libManifest) {
		this.libManifest = libManifest;
	}
}
