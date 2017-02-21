package de.fhws.applab.gemara.welling.modelGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewException;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.Category;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;

import java.util.ArrayList;
import java.util.List;

public class DetailViewModelGenerator {

	public static DetailView lecturer() {

		DetailView detailView;

		try {
			List<Category> categories = new ArrayList<>();
			DisplayViewAttribute nameAttribute = new DisplayViewAttribute("name", ViewAttribute.AttributeType.TEXT);
			GroupResourceViewAttribute name = new GroupResourceViewAttribute(nameAttribute, getViewTitleAttributes());

			categories.add(new Category("Office", getOfficeResourceViewAttributes()));
			categories.add(new Category("Contact", getContactResourceViewAttributes()));
			categories.add(new Category("Charges", getChangeResourceViewAttributes()));

			detailView = new DetailView("Lecturer", name, categories);
			detailView.setImage(getImage());
		} catch (DisplayViewException ex) {
			detailView = null;
		}

		return detailView;
	}

	public static DetailView charge() {
		DetailView detailView;

		try {
			List<Category> categories = new ArrayList<>();
			DisplayViewAttribute titleAttribute = new DisplayViewAttribute("title", ViewAttribute.AttributeType.TEXT);
			SingleResourceViewAttribute title = new SingleResourceViewAttribute(titleAttribute);

			categories.add(new Category("Tenure", getTenureResourceViewAttributes()));

			detailView = new DetailView("Charge", title, categories);
		} catch (DisplayViewException ex) {
			detailView = null;
		}

		return detailView;

	}


	private static List<ResourceViewAttribute> getTenureResourceViewAttributes() {
		List<ResourceViewAttribute> tenureAttributes = new ArrayList<>();

		DisplayViewAttribute fromDateAttribute = new DisplayViewAttribute("fromDate", ViewAttribute.AttributeType.DATE);
		fromDateAttribute.setAttributeLabel("Startdate");
		SingleResourceViewAttribute fromDate = new SingleResourceViewAttribute(fromDateAttribute);
		tenureAttributes.add(fromDate);

		DisplayViewAttribute toDateAttribute = new DisplayViewAttribute("toDate", ViewAttribute.AttributeType.DATE);
		toDateAttribute.setAttributeLabel("Enddate");
		SingleResourceViewAttribute toDate = new SingleResourceViewAttribute(toDateAttribute);
		tenureAttributes.add(toDate);
		return tenureAttributes;
	}

	private static SingleResourceViewAttribute getImage() {
		DisplayViewAttribute imageAttribute = new DisplayViewAttribute("profileImageUrl", ViewAttribute.AttributeType.PICTURE);
		imageAttribute.setAttributeLabel("ProfileImage");
		imageAttribute.setPicturePosition(DisplayViewAttribute.PicturePosition.LEFT);
		return new SingleResourceViewAttribute(imageAttribute);
	}

	private static List<DisplayViewAttribute> getViewTitleAttributes() {
		List<DisplayViewAttribute> nameAttributes = new ArrayList<>();
		DisplayViewAttribute firstNameAttributes = new DisplayViewAttribute("firstName", ViewAttribute.AttributeType.TEXT);
		firstNameAttributes.setAttributeLabel("FirstName");
		nameAttributes.add(firstNameAttributes);
		DisplayViewAttribute lastNameAttributes = new DisplayViewAttribute("lastName", ViewAttribute.AttributeType.TEXT);
		lastNameAttributes.setAttributeLabel("LastName");
		nameAttributes.add(lastNameAttributes);
		return nameAttributes;
	}

	private static List<ResourceViewAttribute> getOfficeResourceViewAttributes() {
		List<ResourceViewAttribute> officeAttributes = new ArrayList<>();

		DisplayViewAttribute addressAttribute = new DisplayViewAttribute("address", ViewAttribute.AttributeType.LOCATION);
		addressAttribute.setAttributeLabel("Address");
		addressAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute address = new SingleResourceViewAttribute(addressAttribute);
		officeAttributes.add(address);

		DisplayViewAttribute roomAttribute = new DisplayViewAttribute("roomNumber", ViewAttribute.AttributeType.TEXT);
		roomAttribute.setAttributeLabel("Room");
		roomAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute room = new SingleResourceViewAttribute(roomAttribute);
		officeAttributes.add(room);
		return officeAttributes;
	}

	private static List<ResourceViewAttribute> getContactResourceViewAttributes() {
		List<ResourceViewAttribute> contactAttributes = new ArrayList<>();

		DisplayViewAttribute mailAttribute = new DisplayViewAttribute("email", ViewAttribute.AttributeType.MAIL);
		mailAttribute.setAttributeLabel("E-Mail");
		mailAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute mail = new SingleResourceViewAttribute(mailAttribute);
		contactAttributes.add(mail);

		DisplayViewAttribute phoneAttribute = new DisplayViewAttribute("phone", ViewAttribute.AttributeType.PHONE_NUMBER);
		phoneAttribute.setAttributeLabel("Phone Number");
		phoneAttribute.setClickActionAndroid(true);
		SingleResourceViewAttribute phone = new SingleResourceViewAttribute(phoneAttribute);
		contactAttributes.add(phone);

		DisplayViewAttribute welearnAttribute = new DisplayViewAttribute("homepage", ViewAttribute.AttributeType.URL);
		welearnAttribute.setAttributeLabel("welearn");
		welearnAttribute.setClickActionAndroid(true);
		welearnAttribute.setLinkDescription("welearn");
		SingleResourceViewAttribute welearn = new SingleResourceViewAttribute(welearnAttribute);
		contactAttributes.add(welearn);
		return contactAttributes;
	}

	private static List<ResourceViewAttribute> getChangeResourceViewAttributes() {
		List<ResourceViewAttribute> chargeAttributes = new ArrayList<>();

		DisplayViewAttribute charge = new DisplayViewAttribute("chargeUrl", ViewAttribute.AttributeType.SUBRESOURCE);
		charge.setAttributeLabel("Charge");
		charge.setClickActionAndroid(true);
		SingleResourceViewAttribute chargeResourceViewAttribute = new SingleResourceViewAttribute(charge);
		chargeAttributes.add(chargeResourceViewAttribute);


		return chargeAttributes;

	}
}
