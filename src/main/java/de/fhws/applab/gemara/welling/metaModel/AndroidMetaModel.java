package de.fhws.applab.gemara.welling.metaModel;

import java.util.ArrayList;
import java.util.List;

public class AndroidMetaModel {

	private String packageName;
	private String packageNameLib;
	private String applicationName;
	private AppColor appColor;
	private AppRestAPI appRestAPI;
	private AppStyle libStyles;
	private AppStyle appStyles;
	private AppString libStrings;
	private AppString appStrings;
	private AppDeclareStyleable appDeclareStyleable;
	private AppAndroidManifest libManifest;
	private AppAndroidManifest appManifest;
	private final List<AppResource> appResources = new ArrayList<>();

	public AndroidMetaModel(String packageName, String applicationName) {
		this.packageName = packageName;
		this.applicationName = applicationName;
		this.packageNameLib = packageName + "." + applicationName.toLowerCase() + "_lib";
	}

	public String getPackageNameLib() {
		return packageNameLib;
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

	public AppStyle getAppStyles() {
		return appStyles;
	}

	public void setAppStyles(AppStyle appStyles) {
		this.appStyles = appStyles;
	}

	public AppString getLibStrings() {
		return libStrings;
	}

	public void setLibStrings(AppString libStrings) {
		this.libStrings = libStrings;
	}

	public AppString getAppStrings() {
		return appStrings;
	}

	public void setAppStrings(AppString appStrings) {
		this.appStrings = appStrings;
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

	public AppAndroidManifest getAppManifest() {
		return appManifest;
	}

	public void setAppManifest(AppAndroidManifest appManifest) {
		this.appManifest = appManifest;
	}

	public List<AppResource> getAppResources() {
		return appResources;
	}

	public void setAppResources(List<AppResource> appResources) {
		this.appResources.addAll(appResources);
	}

	public void addAppResource(AppResource appResource) {
		this.appResources.add(appResource);
	}
}
