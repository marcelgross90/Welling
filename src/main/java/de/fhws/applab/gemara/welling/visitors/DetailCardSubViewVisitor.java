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

public class DetailCardSubViewVisitor implements ResourceViewAttributeVisitor {

	private final AppDescription appDescription;

	private AbstractLayoutGenerator.View view;
	private String packageName;

	public DetailCardSubViewVisitor(AppDescription appDescription, String packageName) {
		this.appDescription = appDescription;
		this.packageName = packageName;
	}

	public AbstractLayoutGenerator.View getView() {
		return view;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE) {
			this.view = null;
		}
		_visitForDetailCardSubView(displayViewAttribute, packageName);
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
		_visitForDetailCardSubView(displayViewAttribute, packageName);
	}

	private void _visitForDetailCardSubView(DisplayViewAttribute displayViewAttribute, String packageName) {
		AbstractLayoutGenerator.View view;

		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE) {
			view = getButton(displayViewAttribute);
		} else {
			view = getAttributeView(displayViewAttribute, packageName);
		}

		view.addViewAttribute("android:layout_weight=\"1\"");

		AbstractLayoutGenerator.View linearLayout = new AbstractLayoutGenerator.View("LinearLayout");
		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:layout_marginTop=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginLeft=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:orientation=\"horizontal\"");
		linearLayout.setViewAttributes(viewAttributes);
		linearLayout.addSubView(getDescriptionView(displayViewAttribute));
		linearLayout.addSubView(view);

		this.view = linearLayout;
	}

	private AbstractLayoutGenerator.View getDescriptionView(DisplayViewAttribute displayViewAttribute) {
		String viewName = "tv" + getInputWithCapitalStart(displayViewAttribute.getAttributeName()) + "Caption";
		String stringName = replaceIllegalCharacters(displayViewAttribute.getAttributeLabel().toLowerCase());
		addString(stringName, displayViewAttribute.getAttributeLabel());

		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");
		viewAttributes.add("android:layout_weight=\"2\"");
		viewAttributes.add("android:text=\"@string/" + stringName + "\"");

		if (displayViewAttribute.getFontColor() != null) {
			viewAttributes.add("android:textColor=\"" + displayViewAttribute.getFontColor() + "\"");
		}

		if (displayViewAttribute.getFontSize() == DisplayViewAttribute.FontSize.LARGE) {
			viewAttributes.add("android:textSize=\"18sp\"");
		}

		if (displayViewAttribute.getFontSize() == DisplayViewAttribute.FontSize.SMALL) {
			viewAttributes.add("android:textSize=\"9sp\"");
		}

		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View("TextView");
		view.setViewAttributes(viewAttributes);

		return view;
	}

	private AbstractLayoutGenerator.View getAttributeView(DisplayViewAttribute displayViewAttribute, String packageName) {
		String viewName = "tv" + getInputWithCapitalStart(displayViewAttribute.getAttributeName()) + "Value";
		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View(packageName + ".generic.customView.AttributeView");

		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");

		if (displayViewAttribute.getFontColor() != null) {
			viewAttributes.add("android:textColor=\"" + displayViewAttribute.getFontColor() + "\"");
		}

		if (displayViewAttribute.getFontSize() == DisplayViewAttribute.FontSize.LARGE) {
			viewAttributes.add("android:textSize=\"18sp\"");
		}

		if (displayViewAttribute.getFontSize() == DisplayViewAttribute.FontSize.SMALL) {
			viewAttributes.add("android:textSize=\"9sp\"");
		}

		view.setViewAttributes(viewAttributes);

		return view;
	}

	private AbstractLayoutGenerator.View getButton(DisplayViewAttribute displayViewAttribute) {
		addString(replaceIllegalCharacters(displayViewAttribute.getAttributeName()) + "_btn", displayViewAttribute.getAttributeLabel());
		String viewName = "tv" + getInputWithCapitalStart(displayViewAttribute.getAttributeName()) + "Value";

		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View("Button");

		List<String> viewAttributes = new ArrayList<>();

		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:text=\"@string/" + replaceIllegalCharacters(displayViewAttribute.getAttributeName()) + "_btn" + "\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");

		view.setViewAttributes(viewAttributes);

		return view;
	}

	private String getInputWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(key, value);
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}
}