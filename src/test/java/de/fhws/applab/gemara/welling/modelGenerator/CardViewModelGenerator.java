package de.fhws.applab.gemara.welling.modelGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewException;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;

import java.util.ArrayList;
import java.util.List;

public class CardViewModelGenerator {


	private static DisplayViewAttribute.PicturePosition picturePosition =  DisplayViewAttribute.PicturePosition.LEFT;
	private static String homepageDescription = "welearn";

	public static CardView lecturer() {
		List<ResourceViewAttribute> resourceViewAttributes = new ArrayList<>();

		DisplayViewAttribute titleAttributes = new DisplayViewAttribute("title", ViewAttribute.AttributeType.TEXT);
		titleAttributes.setAttributeLabel("Title");
		SingleResourceViewAttribute title = new SingleResourceViewAttribute(titleAttributes);
		resourceViewAttributes.add(title);

		DisplayViewAttribute nameAttribute = new DisplayViewAttribute("name", ViewAttribute.AttributeType.TEXT);
		nameAttribute.setFontSize(DisplayViewAttribute.FontSize.LARGE);
		List<DisplayViewAttribute> nameAttributes = new ArrayList<>();

		DisplayViewAttribute firstNameAttributes = new DisplayViewAttribute("firstName", ViewAttribute.AttributeType.TEXT);
		firstNameAttributes.setAttributeLabel("FirstName");
		nameAttributes.add(firstNameAttributes);

		DisplayViewAttribute lastNameAttributes = new DisplayViewAttribute("lastName", ViewAttribute.AttributeType.TEXT);
		lastNameAttributes.setAttributeLabel("LastName");
		nameAttributes.add(lastNameAttributes);

		GroupResourceViewAttribute name;
		try {
			nameAttribute.setFontColor("#000");
			name = new GroupResourceViewAttribute(nameAttribute, nameAttributes);
		} catch (DisplayViewException ex) {
			name = null;
		}
		resourceViewAttributes.add(name);

		DisplayViewAttribute mailAttribute = new DisplayViewAttribute("email", ViewAttribute.AttributeType.MAIL);
		mailAttribute.setAttributeLabel("E-Mail");
		mailAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute mail = new SingleResourceViewAttribute(mailAttribute);
		resourceViewAttributes.add(mail);

		DisplayViewAttribute phoneAttribute = new DisplayViewAttribute("phone", ViewAttribute.AttributeType.PHONE_NUMBER);
		phoneAttribute.setAttributeLabel("Phone Number");
		phoneAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute phone = new SingleResourceViewAttribute(phoneAttribute);
		resourceViewAttributes.add(phone);

		DisplayViewAttribute addressAttribute = new DisplayViewAttribute("address", ViewAttribute.AttributeType.LOCATION);
		addressAttribute.setAttributeLabel("Address");
		addressAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute address = new SingleResourceViewAttribute(addressAttribute);
		resourceViewAttributes.add(address);

		DisplayViewAttribute roomAttribute = new DisplayViewAttribute("roomNumber", ViewAttribute.AttributeType.HOME);
		roomAttribute.setAttributeLabel("Room");
		roomAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute room = new SingleResourceViewAttribute(roomAttribute);
		resourceViewAttributes.add(room);

		DisplayViewAttribute welearnAttribute = new DisplayViewAttribute("homepage", ViewAttribute.AttributeType.URL);
		welearnAttribute.setAttributeLabel("welearn");
		welearnAttribute.setClickActionAndroid(true);
		welearnAttribute.setLinkDescription(homepageDescription);
		SingleResourceViewAttribute welearn = new SingleResourceViewAttribute(welearnAttribute);
		resourceViewAttributes.add(welearn);

		DisplayViewAttribute imageAttribute = new DisplayViewAttribute("profileImageUrl", ViewAttribute.AttributeType.PICTURE);
		imageAttribute.setAttributeLabel("ProfileImage");
		imageAttribute.setPicturePosition(picturePosition);
		SingleResourceViewAttribute image = new SingleResourceViewAttribute(imageAttribute);
		resourceViewAttributes.add(image);

		CardView cardView;

		try {
			cardView = new CardView("Lecturer", resourceViewAttributes, name);
		} catch (DisplayViewException ex) {
			cardView = null;
		}

		return cardView;
	}

	public static CardView charges() {
		List<ResourceViewAttribute> resourceViewAttributes = new ArrayList<>();

		DisplayViewAttribute titleAttributes = new DisplayViewAttribute("title", ViewAttribute.AttributeType.TEXT);
		titleAttributes.setAttributeLabel("Title");
		SingleResourceViewAttribute title = new SingleResourceViewAttribute(titleAttributes);
		resourceViewAttributes.add(title);

		DisplayViewAttribute dateAttribute = new DisplayViewAttribute("date", ViewAttribute.AttributeType.TEXT);

		List<DisplayViewAttribute> dateAttributes = new ArrayList<>();

		DisplayViewAttribute startDateAttributes = new DisplayViewAttribute("fromDate", ViewAttribute.AttributeType.DATE);
		startDateAttributes.setAttributeLabel("Startdate");
		dateAttributes.add(startDateAttributes);

		DisplayViewAttribute endDateAttributes = new DisplayViewAttribute("toDate", ViewAttribute.AttributeType.DATE);
		endDateAttributes.setAttributeLabel("Enddate");
		dateAttributes.add(endDateAttributes);

		GroupResourceViewAttribute date;
		try {
			date = new GroupResourceViewAttribute(dateAttribute, dateAttributes);
		} catch (DisplayViewException ex) {
			date = null;
		}
		resourceViewAttributes.add(date);

		CardView cardView;

		try {
			cardView = new CardView("Charge", resourceViewAttributes, title);
		} catch (DisplayViewException ex) {
			cardView = null;
		}

		return cardView;
	}
}