package de.fhws.applab.gemara.welling.metaModelExtension;

import java.util.ArrayList;
import java.util.List;

public class AppAndroidManifest {

	private final String packageName;
	private final List<String> permissions = new ArrayList<>();
	private Application application;

	public AppAndroidManifest(String packageName) {
		this.application = new Application();
		this.packageName = packageName;
		this.permissions.add("android.permission.INTERNET");
	}

	public void setPermissions(List<String> permissions) {
		this.permissions.addAll(permissions);
	}

	public void setPermissions(String permission) {
		this.permissions.add(permission);
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getPackageName() {
		return packageName;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public Application getApplication() {
		return application;
	}

	public static class Application {
		private final List<String> applicationAttributes = new ArrayList<>();
		private final List<Activity> activities = new ArrayList<>();

		public Application() {
			this.applicationAttributes.add("android:allowBackup=\"true\"");
			this.applicationAttributes.add("android:label=\"@string/app_name\"");
			this.applicationAttributes.add("android:supportsRtl=\"true\"");
		}

		public void addApplicationAttributes(List<String> applicationAttributes) {
			this.applicationAttributes.addAll(applicationAttributes);
		}

		public void addActivities(List<Activity> activities) {
			this.activities.addAll(activities);
		}

		public void addActivities(Activity activitie) {
			this.activities.add(activitie);
		}

		public List<String> getApplicationAttributes() {
			return applicationAttributes;
		}

		public List<Activity> getActivities() {
			return activities;
		}
	}

	public static class Activity {
		private final String name;
		private final List<IntentFilter> intentFilters = new ArrayList<>();

		public Activity(String name) {
			this.name = name;
		}

		public void addIntentFilters(List<IntentFilter> intentFilters) {
			this.intentFilters.addAll(intentFilters);
		}

		public String getName() {
			return name;
		}

		public List<IntentFilter> getIntentFilters() {
			return intentFilters;
		}
	}

	public static class IntentFilter {
		private String action;
		private final List<String> categories = new ArrayList<>();
		private Data data;

		public IntentFilter() {
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public List<String> getCategories() {
			return categories;
		}

		public void setCategories(List<String> categories) {
			this.categories.addAll(categories);
		}

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}
	}

	public static class Data {
		private final String host;
		private final String scheme;

		public Data(String host, String scheme) {
			this.host = host;
			this.scheme = scheme;
		}

		public String getHost() {
			return host;
		}

		public String getScheme() {
			return scheme;
		}
	}

}
