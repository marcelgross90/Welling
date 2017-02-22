package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;

import java.util.ArrayList;
import java.util.List;

public class CardAttributeVisitor implements ResourceViewAttributeVisitor {

	private final String packageName;

	private final AppDescription appDescription;
	private final List<AbstractLayoutGenerator.View> views = new ArrayList<>();

	public CardAttributeVisitor(AppDescription appDescription, String packageName) {
		this.packageName = packageName;
		this.appDescription = appDescription;
	}

	public List<AbstractLayoutGenerator.View> getViews() {
		return views;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE) {
			getImageView(displayViewAttribute.getAttributeLabel(), packageName);
		} else {
			_visitForCardSubView(displayViewAttribute);
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
		_visitForCardSubView(displayViewAttribute);
	}

	private void _visitForCardSubView(DisplayViewAttribute displayViewAttribute) {
		AbstractLayoutGenerator.View view = getAttributeView(displayViewAttribute, packageName);

		List<AbstractLayoutGenerator.View> views = new ArrayList<>();

		views.add(view);

		addViews(views);
	}

	private void getImageView(String imageLabel, String packageName) {
		addString(imageLabel.toLowerCase(), imageLabel);

		AbstractLayoutGenerator.View imageView = new AbstractLayoutGenerator.View(packageName + ".generic.customView.ProfileImageView");

		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"wrap_content\"");
		viewAttributes.add("android:layout_height=\"@dimen/picture_height\"");
		viewAttributes.add("android:id=\"@+id/profileImg\"");
		viewAttributes.add("android:contentDescription=\"@string/" + imageLabel.toLowerCase() + "\"");
		viewAttributes.add("android:layout_alignParentLeft=\"true\"");
		viewAttributes.add("android:layout_alignParentStart=\"true\"");
		viewAttributes.add("android:layout_alignParentTop=\"true\"");
		viewAttributes.add("android:minWidth=\"@dimen/picture_width\"");

		imageView.setViewAttributes(viewAttributes);

		List<AbstractLayoutGenerator.View> imageViews = new ArrayList<>();
		imageViews.add(imageView);
		imageViews.add(getBorderView("profileImg"));

		addViews(imageViews);
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(key, value);
	}

	@SuppressWarnings("SameParameterValue")
	private AbstractLayoutGenerator.View getBorderView(String imageName) {
		AbstractLayoutGenerator.View border = new AbstractLayoutGenerator.View("View");
		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"@dimen/spacing_small\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/border\"");
		viewAttributes.add("android:layout_alignBottom=\"@+id/" + imageName + "\"");
		viewAttributes.add("android:layout_alignTop=\"@+id/" + imageName + "\"");
		viewAttributes.add("android:layout_marginEnd=\"@dimen/spacing_small\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_small\"");
		viewAttributes.add("android:layout_toEndOf=\"@+id/" + imageName + "\"");
		viewAttributes.add("android:layout_toRightOf=\"@+id/" + imageName + "\"");
		viewAttributes.add("android:background=\"@color/colorPrimary\"");

		border.setViewAttributes(viewAttributes);

		return border;
	}

	private AbstractLayoutGenerator.View getAttributeView(DisplayViewAttribute displayViewAttribute, String packageName) {
		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View(packageName + ".generic.customView.AttributeView");

		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + displayViewAttribute.getAttributeName() + "\"");

		if (displayViewAttribute.getFontColor() != null) {
			viewAttributes.add("android:textColor=\"" + displayViewAttribute.getFontColor() + "\"");
		}

		if (displayViewAttribute.getFontSize() == DisplayViewAttribute.FontSize.LARGE) {
			viewAttributes.add("android:textSize=\"18sp\"");
		}

		if (displayViewAttribute.getFontSize() == DisplayViewAttribute.FontSize.SMALL) {
			viewAttributes.add("android:textSize=\"9sp\"");
		}

		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.HOME) {
			viewAttributes.add("android:drawableLeft=\"@drawable/ic_home\"");
			viewAttributes.add("android:drawableStart=\"@drawable/ic_home\"");
			viewAttributes.add("android:drawablePadding=\"@dimen/spacing_small\"");
		}

		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.MAIL) {
			viewAttributes.add("android:drawableLeft=\"@drawable/ic_email\"");
			viewAttributes.add("android:drawableStart=\"@drawable/ic_email\"");
			viewAttributes.add("android:drawablePadding=\"@dimen/spacing_small\"");
		}

		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PHONE_NUMBER) {
			viewAttributes.add("android:drawableLeft=\"@drawable/ic_phone\"");
			viewAttributes.add("android:drawableStart=\"@drawable/ic_phone\"");
			viewAttributes.add("android:drawablePadding=\"@dimen/spacing_small\"");
		}

		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.LOCATION) {
			viewAttributes.add("android:drawableLeft=\"@drawable/ic_location\"");
			viewAttributes.add("android:drawableStart=\"@drawable/ic_location\"");
			viewAttributes.add("android:drawablePadding=\"@dimen/spacing_small\"");
		}

		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.URL) {
			viewAttributes.add("android:drawableLeft=\"@drawable/ic_open_in_browser\"");
			viewAttributes.add("android:drawableStart=\"@drawable/ic_open_in_browser\"");
			viewAttributes.add("android:drawablePadding=\"@dimen/spacing_small\"");
		}

		view.setViewAttributes(viewAttributes);

		return view;
	}

	private void addViews(List<AbstractLayoutGenerator.View> views) {
		this.views.clear();
		this.views.addAll(views);
	}
}