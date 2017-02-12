package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.welling.metaModel.AppAndroidManifest;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;
import de.fhws.applab.gemara.welling.metaModel.AppRestAPI;
import de.fhws.applab.gemara.welling.metaModel.AppString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppDescription {

	private String appName;
	private String appPackageName;
	private String appResDirectory;
	private String appJavaDirectory;
	private String appManifestDirectory;

	private String libName;
	private String libPackageName;
	private String libResDirectory;
	private String libJavaDirectory;
	private String libManifestDirectory;

	private AppDeclareStyleable appDeclareStyleable;
	private AppRestAPI appRestAPI;
	private AppAndroidManifest appManifest;
	private AppString appString;
	private AppString libString;

	public AppDescription(Model metaModel, String startDir, String baseUrl) {
		this.appName = metaModel.getProjectName();
		this.appPackageName = metaModel.getPackagePrefix();
		this.appManifestDirectory = "/generated/app/src/main";
		this.appResDirectory = appManifestDirectory + "/res";
		this.appJavaDirectory = startDir + "/app/src/main/java/";

		this.libName = metaModel.getProjectName().toLowerCase() + "_lib";
		this.libPackageName = appPackageName + "." + libName;
		this.libManifestDirectory = "/generated/" + libName + "/src/main";
		this.libResDirectory = libManifestDirectory + "/res";
		this.libJavaDirectory = startDir + libName + "/src/main/java/";

		this.appDeclareStyleable = new AppDeclareStyleable();
		this.appRestAPI = new AppRestAPI(baseUrl);
		this.appManifest = new AppAndroidManifest(this.appPackageName);
		this.appString = new AppString();
		this.libString = new AppString();

		prepareManifest();
	}

	public String getAppName() {
		return appName;
	}

	public String getAppPackageName() {
		return appPackageName;
	}

	public String getAppResDirectory() {
		return appResDirectory;
	}

	public String getAppJavaDirectory() {
		return appJavaDirectory;
	}

	public String getAppManifestDirectory() {
		return appManifestDirectory;
	}

	public String getLibName() {
		return libName;
	}

	public String getLibPackageName() {
		return libPackageName;
	}

	public String getLibResDirectory() {
		return libResDirectory;
	}

	public String getLibJavaDirectory() {
		return libJavaDirectory;
	}

	public String getLibManifestDirectory() {
		return libManifestDirectory;
	}

	public AppDeclareStyleable getAppDeclareStyleable() {
		return appDeclareStyleable;
	}

	public void setDeclareStyleables(Map<String, AppDeclareStyleable.DeclareStyleable> declareStyleables) {
		this.appDeclareStyleable.setDeclareStyleables(declareStyleables);
	}

	public void setDeclareStyleables(String key, AppDeclareStyleable.DeclareStyleable declareStyleable) {
		this.appDeclareStyleable.setDeclareStyleables(key, declareStyleable);
	}

	public AppRestAPI getAppRestAPI() {
		return appRestAPI;
	}


	public void setRestApi(String stateKey, String relTypeKey, String relType) {
		this.appRestAPI.setRestApi(stateKey, relTypeKey, relType);
	}

	public AppAndroidManifest getAppManifest() {
		return appManifest;
	}

	public void setManifestPermissions(List<String> permissions) {
		this.appManifest.setPermissions(permissions);
	}

	public void setManifestPermissions(String permission) {
		this.appManifest.setPermissions(permission);
	}

	public void setManifestApplication(AppAndroidManifest.Application application) {
		this.appManifest.setApplication(application);
	}

	public AppString getAppString() {
		return appString;
	}

	public void setAppStrings(Map<String, String> strings) {
		this.appString.setStrings(strings);
	}

	public void setAppStrings(String key, String value) {
		this.appString.setStrings(key, value);
	}

	public AppString getLibString() {
		return libString;
	}

	public void setLibStrings(Map<String, String> strings) {
		this.libString.setStrings(strings);
	}

	public void setLibStrings(String key, String value) {
		this.libString.setStrings(key, value);
	}

	private void prepareManifest() {
		AppAndroidManifest.Application application = this.appManifest.getApplication();
		List<String> applicationAttributes = new ArrayList<>();
		applicationAttributes.add("android:fullBackupContent=\"true\"");
		applicationAttributes.add("android:icon=\"@mipmap/ic_launcher\"");
		applicationAttributes.add("android:theme=\"@style/AppTheme\"");
		application.addApplicationAttributes(applicationAttributes);

		List<AppAndroidManifest.Activity> activities = new ArrayList<>();
		AppAndroidManifest.Activity mainActivity = new AppAndroidManifest.Activity(".MainActivity");


		List<AppAndroidManifest.IntentFilter> intentFilters = new ArrayList<>();

		AppAndroidManifest.IntentFilter mainFilter = new AppAndroidManifest.IntentFilter();
		mainFilter.setAction("android.intent.action.MAIN");
		List<String> mainFilterCategories = new ArrayList<>();
		mainFilterCategories.add("android.intent.category.LAUNCHER");
		mainFilter.setCategories(mainFilterCategories);
		intentFilters.add(mainFilter);

		AppAndroidManifest.IntentFilter viewFilter = new AppAndroidManifest.IntentFilter();
		viewFilter.setAction("android.intent.action.VIEW");
		viewFilter.setData(new AppAndroidManifest.Data("www.fhws.de", "http"));
		List<String> viewFilterCategories = new ArrayList<>();
		viewFilterCategories.add("android.intent.category.DEFAULT");
		viewFilterCategories.add("android.intent.category.BROWSABLE");
		viewFilter.setCategories(viewFilterCategories);
		intentFilters.add(viewFilter);

		mainActivity.addIntentFilters(intentFilters);
		activities.add(mainActivity);

		application.addActivities(activities);

		this.appManifest.setApplication(application);

	}
}
