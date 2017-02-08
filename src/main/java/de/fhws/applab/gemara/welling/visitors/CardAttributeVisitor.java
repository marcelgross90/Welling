package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.ArrayList;
import java.util.List;

public class CardAttributeVisitor implements ResourceViewAttributeVisitor {

	private String packageName;

	private final List<AbstractLayoutGenerator.View> views = new ArrayList<>();

	public CardAttributeVisitor(String packageName) {
		this.packageName = packageName;
	}

	public List<AbstractLayoutGenerator.View> getViews() {
		return views;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE) {
			getImageView(displayViewAttribute.getAttributeLabel(), packageName);
		}
		_visitForCardSubView(displayViewAttribute.getAttributeName());
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
		_visitForCardSubView(displayViewAttribute.getAttributeName());
	}

	private void  _visitForCardSubView(String viewName) {
		AbstractLayoutGenerator.View view = getAttributeView(viewName, packageName);

		List<AbstractLayoutGenerator.View> views = new ArrayList<>();
		views.add(view);

		addViews(views);
	}

	private void getImageView(String imageLabel, String packageName) {
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

	private AbstractLayoutGenerator.View getAttributeView(String viewName, String packageName) {
		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View(packageName + ".generic.customView.AttributeView");

		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");

		view.setViewAttributes(viewAttributes);

		return view;
	}

	private void addViews(List<AbstractLayoutGenerator.View> views) {
		this.views.clear();
		this.views.addAll(views);
	}
}
