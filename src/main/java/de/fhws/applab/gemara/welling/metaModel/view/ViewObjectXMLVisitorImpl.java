package de.fhws.applab.gemara.welling.metaModel.view;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.ArrayList;
import java.util.List;

public class ViewObjectXMLVisitorImpl implements ViewObjectXMLVisitor {

	@Override
	public List<AbstractLayoutGenerator.View> visitForCardSubView(SingleViewObject singleViewObject, String packageName) {
		if (singleViewObject.getViewAttribute().getType() == AttributeType.PICTURE) {
			return getImageView(singleViewObject, packageName);
		}
		return _visitForCardSubView(singleViewObject.getViewName(), packageName);
	}

	private List<AbstractLayoutGenerator.View> getImageView(SingleViewObject singleViewObject, String packageName) {
		AbstractLayoutGenerator.View imageView = new AbstractLayoutGenerator.View(packageName + ".generic.customView.ProfileImageView");

		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"wrap_content\"");
		viewAttributes.add("android:layout_height=\"@dimen/picture_height\"");
		viewAttributes.add("android:id=\"@+id/profileImg\"");
		viewAttributes.add("android:contentDescription=\"@string/" + singleViewObject.getViewAttribute().getDisplayedName().toLowerCase() + "\"");
		viewAttributes.add("android:layout_alignParentLeft=\"true\"");
		viewAttributes.add("android:layout_alignParentStart=\"true\"");
		viewAttributes.add("android:layout_alignParentTop=\"true\"");
		viewAttributes.add("android:minWidth=\"@dimen/picture_width\"");

		imageView.setViewAttributes(viewAttributes);

		List<AbstractLayoutGenerator.View> imageViews = new ArrayList<>();
		imageViews.add(imageView);
		imageViews.add(getBorderView("profileImg"));
		return imageViews;

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

	@Override
	public List<AbstractLayoutGenerator.View> visitForCardSubView(GroupedViewObject groupedViewObject, String packageName) {
		return _visitForCardSubView(groupedViewObject.getViewName(), packageName);
	}

	private List<AbstractLayoutGenerator.View> _visitForCardSubView(String viewName, String packageName) {
		AbstractLayoutGenerator.View view = new AbstractLayoutGenerator.View(packageName + "_lib.generic.customView.AttributeView");

		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"wrap_content\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:id=\"@+id/" + viewName + "\"");

		view.setViewAttributes(viewAttributes);

		List<AbstractLayoutGenerator.View> views = new ArrayList<>();
		views.add(view);
		return views;
	}
}
