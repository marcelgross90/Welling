package de.fhws.applab.gemara.welling.application.lib.generic;

import de.fhws.applab.gemara.welling.GeneratedFile;
import de.fhws.applab.gemara.welling.metaModel.AppAndroidManifest;

import java.util.List;

public class ManifestGenerator extends GeneratedFile {

	private final String directoryName;
	private final AppAndroidManifest appAndroidManifest;

	public ManifestGenerator(String directoryName, AppAndroidManifest appAndroidManifest) {
		this.directoryName = directoryName;
		this.appAndroidManifest = appAndroidManifest;
	}

	@Override
	public void generate() {
		appendln("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		appendln("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n package=\"" + appAndroidManifest.getPackageName() + "\">");
		appendPermissions();
		appendApplication();
		appendln("</manifest>");
	}

	private void appendPermissions() {
		List<String> permissions = appAndroidManifest.getPermissions();

		for (String permission : permissions) {
			appendln("<uses-permission android:name=\"" + permission + "\"/>");
		}
	}

	private void appendApplication() {
		AppAndroidManifest.Application application = appAndroidManifest.getApplication();
		appendln("<application");
		appendApplicationAttributes(application);
		appendln(">");
		appendActivities(application);

	}

	private void appendApplicationAttributes(AppAndroidManifest.Application application) {
		application.getApplicationAttributes().forEach(this::appendln);
	}

	private void appendActivities(AppAndroidManifest.Application application) {
		List<AppAndroidManifest.Activity> activities = application.getActivities();

		for (AppAndroidManifest.Activity activity : activities) {
			appendln("<activity android:name=\"" + activity.getName() + "\">");
			appendIntentFilter(activity);
			appendln("</activity>");
		}
	}

	private void appendIntentFilter(AppAndroidManifest.Activity activity) {
		List<AppAndroidManifest.IntentFilter> intentFilters = activity.getIntentFilters();

		for (AppAndroidManifest.IntentFilter intentFilter : intentFilters) {
			appendln("<intent-filter>");
			appendln("<action android:name=\"" + intentFilter.getAction() + "\"/>");
			appendCategories(intentFilter);
			appendData(intentFilter.getData());
			appendln("</intent-filter>");
		}
	}

	private void appendCategories(AppAndroidManifest.IntentFilter intentFilter) {
		List<String> categories = intentFilter.getCategories();
		for (String category : categories) {
			appendln("<category android:name=\"" + category + "\"/>");
		}
	}

	private void appendData(AppAndroidManifest.Data data) {
		if (data != null) {
			appendln("<data android:host=\"" + data.getHost()
					+ "\" android:scheme=\"" + data.getScheme() + "\"/>");
		}
	}

	@Override
	protected String getFileName() {
		return "AndroidManifest.xml";
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}
}
