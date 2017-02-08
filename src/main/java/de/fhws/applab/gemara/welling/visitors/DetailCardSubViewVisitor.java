package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.ArrayList;
import java.util.List;

public class DetailCardSubViewVisitor implements ResourceViewAttributeVisitor {

	private AbstractLayoutGenerator.View view;
	private String packageName;

	public DetailCardSubViewVisitor(String packageName) {
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
		_visitForDetailCardSubView(displayViewAttribute.getAttributeName(), packageName, displayViewAttribute.getAttributeLabel().toLowerCase());
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
		_visitForDetailCardSubView(displayViewAttribute.getAttributeName(), packageName, displayViewAttribute.getAttributeLabel().toLowerCase());
	}


	private void  _visitForDetailCardSubView(String viewName, String packageName, String stringName) {
		AbstractLayoutGenerator.View view = getAttributeView("tv" + getInputWithCapitalStart(viewName) + "Value", packageName);
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
		linearLayout.addSubView(getDescriptionView("tv" + getInputWithCapitalStart(viewName) + "Caption", stringName));
		linearLayout.addSubView(view);

		this.view = linearLayout;
	}

	private AbstractLayoutGenerator.View getDescriptionView(String viewName, String stringName) {
		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");
		viewAttributes.add("android:layout_weight=\"2\"");
		viewAttributes.add("android:text=\"@string/" + stringName + "\"");


		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View("TextView");
		view.setViewAttributes(viewAttributes);

		return view;
	}

	private AbstractLayoutGenerator.View getAttributeView(String viewName, String packageName) {
		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View(packageName + ".generic.customView.AttributeView");

		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");

		view.setViewAttributes(viewAttributes);

		return view;
	}

	private String getInputWithCapitalStart(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
}
