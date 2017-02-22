package de.fhws.applab.gemara.welling.modelGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewException;

import java.util.ArrayList;
import java.util.List;

public class InputViewModelGenerator {

	public static InputView lecturer() {

		List<InputViewAttribute> inputViewAttributes = new ArrayList<>();

		InputViewAttribute title = new InputViewAttribute("title", ViewAttribute.AttributeType.TEXT, "Title", "Title is missing!");
		title.setAttributeLabel("Title");
		inputViewAttributes.add(title);

		InputViewAttribute firstName = new InputViewAttribute("firstName", ViewAttribute.AttributeType.TEXT, "FirstName",
				"Firstname is missing!");
		firstName.setAttributeLabel("Firstname");
		inputViewAttributes.add(firstName);

		InputViewAttribute lastName = new InputViewAttribute("lastName", ViewAttribute.AttributeType.TEXT, "Lastname",
				"LastName is missing!");
		lastName.setAttributeLabel("Lastname");
		inputViewAttributes.add(lastName);

		InputViewAttribute mail = new InputViewAttribute("email", ViewAttribute.AttributeType.MAIL, "E-Mail", "E-Mail is missing!");
		mail.setAttributeLabel("E-Mail");
		inputViewAttributes.add(mail);

		InputViewAttribute phone = new InputViewAttribute("phone", ViewAttribute.AttributeType.PHONE_NUMBER, "Phone Number",
				"Phone number is missing!");
		phone.setAttributeLabel("Phone Number");
		inputViewAttributes.add(phone);

		InputViewAttribute address = new InputViewAttribute("address", ViewAttribute.AttributeType.TEXT, "Address", "Address is missing!");
		address.setAttributeLabel("Address");
		inputViewAttributes.add(address);

		InputViewAttribute room = new InputViewAttribute("roomNumber", ViewAttribute.AttributeType.TEXT, "Room", "Room is missing!");
		room.setAttributeLabel("Room");
		inputViewAttributes.add(room);

		InputViewAttribute weLearn = new InputViewAttribute("homepage", ViewAttribute.AttributeType.URL, "welearn",
				"welearn URL is missing!");
		weLearn.setAttributeLabel("welearn");
		inputViewAttributes.add(weLearn);

		InputView inputView;
		try {
			inputView = new InputView("Lecturer", inputViewAttributes);
		} catch (InputViewException ex) {
			inputView = null;
		}

		return inputView;
	}

	public static InputView charge() {

		List<InputViewAttribute> inputViewAttributes = new ArrayList<>();

		InputViewAttribute title = new InputViewAttribute("title", ViewAttribute.AttributeType.TEXT, "Title", "Title is missing!");
		title.setAttributeLabel("Title");
		inputViewAttributes.add(title);

		InputViewAttribute fromDate = new InputViewAttribute("fromDate", ViewAttribute.AttributeType.DATE, "Startdate",
				"Startdate is missing!");
		fromDate.setAttributeLabel("Startdate");
		inputViewAttributes.add(fromDate);

		InputViewAttribute endDate = new InputViewAttribute("toDate", ViewAttribute.AttributeType.DATE, "EndDate", "Enddate is missing!");
		endDate.setAttributeLabel("Enddate");
		inputViewAttributes.add(endDate);

		InputView inputView;
		try {
			inputView = new InputView("Charge", inputViewAttributes);
		} catch (InputViewException ex) {
			inputView = null;
		}

		return inputView;
	}
}