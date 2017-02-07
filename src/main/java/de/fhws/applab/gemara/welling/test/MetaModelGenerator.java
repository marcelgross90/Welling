package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.welling.application.lib.specific.java.model.Attribute;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.LinkAttribute;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.SimpleAttribute;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.metaModel.AppAndroidManifest;
import de.fhws.applab.gemara.welling.metaModel.AppColor;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.AppRestAPI;
import de.fhws.applab.gemara.welling.metaModel.AppString;
import de.fhws.applab.gemara.welling.metaModel.AppStyle;
import de.fhws.applab.gemara.welling.metaModel.InputException;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaModelGenerator {

	private static String packageName;
	private static String packageNameLib;

	public static AndroidMetaModel generateMetaModel() throws InputException {
		packageName = "de.fhws.applab.gemara";
		packageNameLib = "de.fhws.applab.gemara.lecturergen_lib";

		AndroidMetaModel model = new AndroidMetaModel(packageName, "LecturerGen");
		AppRestAPI appRestAPI = new AppRestAPI("https://apistaging.fiw.fhws.de/mig/api/");
		appRestAPI.setRestApi(generateRestAPI());

		model.setAppColor(new AppColor("#3F51B5", "#303F9F", "#FF4081", "#fff", "#8080FF"));
		model.setAppRestAPI(appRestAPI);
		model.setLibStyles(generateLibStyle());
		model.setAppStyles(generateAppStyle());
		model.setLibStrings(getLibStrings());
		model.setAppStrings(getAppStrings());
		model.setAppDeclareStyleable(generateAppDeclareStyleable());
		model.setLibManifest(generateLibManifest());
		model.setAppManifest(generateAppManifest());
		model.addAppResource(getLecturerResource());
		return model;
	}

	private static AppAndroidManifest generateLibManifest() {
		return new AppAndroidManifest(packageNameLib);
	}

	private static AppAndroidManifest generateAppManifest() {
		AppAndroidManifest manifest = new AppAndroidManifest(packageName);
		AppAndroidManifest.Application application = manifest.getApplication();
		List<String> applicationAttributes = new ArrayList<>();
		applicationAttributes.add("android:fullBackupContent=\"true\"");
		applicationAttributes.add("android:icon=\"@mipmap/ic_launcher\"");
		applicationAttributes.add("android:theme=\"@style/AppTheme\"");
		application.addApplicationAttributes(applicationAttributes);

		List<AppAndroidManifest.Activity> activities = new ArrayList<>();
		AppAndroidManifest.Activity mainActivity = new AppAndroidManifest.Activity(".MainActivity");
		AppAndroidManifest.Activity lecturerDetailActivity = new AppAndroidManifest.Activity(".LecturerDetailActivity");

		activities.add(lecturerDetailActivity);

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

		//todo finish manifest
		manifest.setApplication(application);

		return manifest;
	}

	private static Map<String, String> generateRestAPI() {
		Map<String, String> api = new HashMap<>();
		api.put("rel_type_first_state", "getAllLecturers");

		api.put("rel_type_create_new_lecturer", "createNewLecturer");
		api.put("rel_type_update_lecturer", "updateLecturer");
		api.put("rel_type_delete_lecturer", "deleteLecturer");
		api.put("rel_type_delete_charge", "deleteCharge");

		api.put("rel_type_update_charge", "updateCharge");
		api.put("rel_type_get_all_charges", "getAllCharges");
		api.put("rel_type_get_one_charge_of_lecturer", "getOneChargeOfLecturer");
		api.put("rel_type_create_charge_of_lecturer", "createChargeOfLecturer");

		return api;
	}

	private static AppString getAppStrings() {
		Map<String, String> stringMap = new HashMap<>();
		stringMap.put("app_name", "LecturersGen");

		stringMap.put("lecturer_updated", "Lecturer updated!");
		stringMap.put("charge_updated", "Charge updated!");
		stringMap.put("lecturer_saved", "Lecturer saved!");
		stringMap.put("charge_saved", "Charge saved!");

		stringMap.put("load_lecturer_error", "Could not load lecturers");
		stringMap.put("load_charges_error", "Could not load charges");
		stringMap.put("lecturer_delete_error", "Could not delete lecturer");
		stringMap.put("charge_delete_error", "Could not delete charge");

		stringMap.put("delete", "Delete");

		return new AppString(stringMap);
	}

	private static AppString getLibStrings() {
		Map<String, String> stringMap = new HashMap<>();
		stringMap.put("app_name", "lecturer_lib");

		stringMap.put("homepage", "welearn");
		stringMap.put("title", "Title");
		stringMap.put("firstName", "Firstname");
		stringMap.put("lastName", "Lastname");
		stringMap.put("email", "E-Mail");
		stringMap.put("phone", "Phone Number");
		stringMap.put("address", "Address");
		stringMap.put("roomNumber", "Room");

		stringMap.put("card_caption_contact", "Contact");
		stringMap.put("card_caption_email", "E-Mail");
		stringMap.put("card_caption_phone", "Phone");
		stringMap.put("card_caption_website", "Homepage");
		stringMap.put("card_caption_office", "Office");
		stringMap.put("card_caption_room", "Room");
		stringMap.put("card_caption_address", "Address");
		stringMap.put("card_caption_charge", "Charge");

		stringMap.put("profile_image", "Profile picture");
		stringMap.put("show_charges", "Show charges");

		stringMap.put("title_missing", "Title is missing");
		stringMap.put("firstName_missing", "Firstname is missing");
		stringMap.put("lastName_missing", "Lastname is missing");
		stringMap.put("email_missing", "Email is missing");
		stringMap.put("phone_missing", "Phonenumber is missing");
		stringMap.put("address_missing", "Address is missing");
		stringMap.put("roomNumber_missing", "Roomnumber is missing");
		stringMap.put("homepage_missing", "Welearn-Address is missing");

		stringMap.put("lecturer_updated", "Lecturer updated");
		stringMap.put("lecturer_saved", "Lecturer saved");

		stringMap.put("full_name", "Fullname");
		stringMap.put("start_date", "Start Date");
		stringMap.put("end_date", "End Date");

		stringMap.put("load_lecturer_error", "Could not load lecturers");
		stringMap.put("load_charges_error", "Could not load charges");
		stringMap.put("lecturer_delete_error", "Could not delete lecturer");
		stringMap.put("charge_delete_error", "Could not delete charge");
		//static strings
		stringMap.put("delete_dialog_title", "Delete %s");
		stringMap.put("ok", "OK");
		stringMap.put("cancel", "Cancel");
		stringMap.put("add", "Add");
		stringMap.put("save", "Save");
		stringMap.put("edit", "Edit");
		stringMap.put("delete", "Delete");

		return new AppString(stringMap);
	}

	private static AppStyle generateLibStyle() {
		AppStyle.Style appTheme = new AppStyle.Style("AppTheme");
		appTheme.setParent("Theme.AppCompat.Light.NoActionBar");
		appTheme.setItem("colorPrimary", "@color/colorPrimary");
		appTheme.setItem("colorPrimaryDark", "@color/colorPrimaryDark");
		appTheme.setItem("colorAccent", "@color/colorAccent");

		AppStyle.Style toolbarTheme = new AppStyle.Style("ToolbarTheme");
		toolbarTheme.setParent("ThemeOverlay.AppCompat.Dark.ActionBar");
		toolbarTheme.setItem("android:textColorPrimary", "@color/toolbar_text");

		AppStyle.Style cardView = new AppStyle.Style("cardView");
		cardView.setItem("android:layout_width", "match_parent");
		cardView.setItem("android:layout_height", "wrap_content");
		cardView.setItem("cardCornerRadius", "1dp");
		cardView.setItem("android:layout_marginBottom", "10dp");
		cardView.setItem("cardElevation", "5dp");

		AppStyle.Style detailCardView = new AppStyle.Style("detailCardView");
		cardView.setItem("android:layout_width", "match_parent");
		cardView.setItem("android:layout_height", "wrap_content");
		cardView.setItem("cardCornerRadius", "1dp");
		cardView.setItem("cardElevation", "5dp");

		AppStyle.Style appBarOverlay = new AppStyle.Style("AppTheme.AppBarOverlay");
		appBarOverlay.setParent("ThemeOverlay.AppCompat.Dark.ActionBar");

		AppStyle.Style popupOverlay = new AppStyle.Style("AppTheme.PopupOverlay");
		popupOverlay.setParent("ThemeOverlay.AppCompat.Light");

		List<AppStyle.Style> styles = new ArrayList<>();
		styles.add(appTheme);
		styles.add(toolbarTheme);
		styles.add(cardView);
		styles.add(detailCardView);
		styles.add(appBarOverlay);
		styles.add(popupOverlay);

		return new AppStyle(styles);
	}

	private static AppStyle generateAppStyle() {
		AppStyle.Style appTheme = new AppStyle.Style("AppTheme");
		appTheme.setParent("Theme.AppCompat.Light.NoActionBar");
		appTheme.setItem("colorPrimary", "@color/colorPrimary");
		appTheme.setItem("colorPrimaryDark", "@color/colorPrimaryDark");
		appTheme.setItem("colorAccent", "@color/colorAccent");

		AppStyle.Style appBarOverlay = new AppStyle.Style("AppTheme.AppBarOverlay");
		appBarOverlay.setParent("ThemeOverlay.AppCompat.Dark.ActionBar");

		AppStyle.Style popupOverlay = new AppStyle.Style("AppTheme.PopupOverlay");
		popupOverlay.setParent("ThemeOverlay.AppCompat.Light");

		List<AppStyle.Style> styles = new ArrayList<>();
		styles.add(appTheme);

		styles.add(appBarOverlay);
		styles.add(popupOverlay);

		return new AppStyle(styles);
	}

	private static AppDeclareStyleable generateAppDeclareStyleable() {
		List<AppDeclareStyleable.DeclareStyleable> declareStyleables = new ArrayList<>();
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("AttributeView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("DateTimeView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("ProfileImageView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("LecturerCardView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("ChargeCardView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("ChargeDetailView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("LecturerDetailView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("LecturerDetailCardView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("LecturerInputView"));
		declareStyleables.add(new AppDeclareStyleable.DeclareStyleable("ChargeInputView"));
		AppDeclareStyleable.DeclareStyleable attributeInput = new AppDeclareStyleable.DeclareStyleable("AttributeInput");
		attributeInput.setAttr("hintText", "integer");
		attributeInput.setAttr("inputType", "string");
		declareStyleables.add(attributeInput);

		return new AppDeclareStyleable(declareStyleables);
	}

	private static AppResource getLecturerResource() {
		AppResource appResource = new AppResource("Lecturer");
		appResource.setAttributes(getLecturerAttributes());

		return appResource;
	}

	private static List<Attribute> getLecturerAttributes() {
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(new SimpleAttribute("id", SimpleAttribute.DataType.INT, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("title", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("firstName", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("lastName", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("email", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("phone", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("roomNumber", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new SimpleAttribute("address", SimpleAttribute.DataType.STRING, Modifier.PRIVATE));
		attributes.add(new LinkAttribute("homepage", packageNameLib, Modifier.PRIVATE));
		attributes.add(new LinkAttribute("profileImageUrl", packageNameLib, Modifier.PRIVATE));
		attributes.add(new LinkAttribute("self", packageNameLib, Modifier.PRIVATE));
		attributes.add(new LinkAttribute("chargeUrl", packageNameLib, Modifier.PRIVATE));
		return attributes;
	}

}
