package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.welling.application.lib.specific.java.model.Attribute;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.LinkAttribute;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.SimpleAttribute;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.metaModel.AppAndroidManifest;
import de.fhws.applab.gemara.welling.metaModel.AppColor;
import de.fhws.applab.gemara.welling.metaModel.AppRestAPI;
import de.fhws.applab.gemara.welling.metaModel.AppString;
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

		model.setAppColor(new AppColor("#3F51B5", "#303F9F", "#FF4081", "#fff", "#8080FF"));
		model.setAppRestAPI(appRestAPI);
		model.setLibStrings(getLibStrings());
		model.setAppStrings(getAppStrings());
		return model;
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


		return new AppString(stringMap);
	}

}
