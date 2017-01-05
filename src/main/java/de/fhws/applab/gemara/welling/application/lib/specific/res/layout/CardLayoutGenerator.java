package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.ViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.ViewObjectXMLVisitorImpl;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutGenerator extends AbstractLayoutGenerator {

	private final AppResource appResource;
	private final String packageName;

	public CardLayoutGenerator(String fileName, String directoryName, AppResource appResource, String packageName) {
		super(fileName, directoryName);
		this.appResource = appResource;
		this.packageName = packageName;
	}

	@Override
	protected View generateLayout() {
		return getCardView();
	}

	private View getCardView() {
		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("xmlns:app=\"http://schemas.android.com/apk/res-auto\"");
		viewAttributes.add("style=\"@style/cardView\"");
		viewAttributes.add("app:cardPreventCornerOverlap=\"false\"");
		viewAttributes.add("android:id=\"@+id/card_" + appResource.getResourceName().toLowerCase() + "\"");
		View cardView = new View("android.support.v7.widget.CardView");
		cardView.setViewAttributes(viewAttributes);
		cardView.addSubView(getRelativeLayout());

		return cardView;
	}

	private View getRelativeLayout() {
		ViewObjectXMLVisitorImpl visitor = new ViewObjectXMLVisitorImpl();
		View relativeLayout = new View("RelativeLayout");
		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("android:id=\"@+id/relativeLayout\"");

		relativeLayout.setViewAttributes(viewAttributes);
		List<View> attributeViews = new ArrayList<>();

		for (ViewObject viewObject : appResource.getAppCardView().getViewAttributes()) {
			List<View> views = viewObject.addCardViewSubView(packageName, visitor);
			if (views.size() == 2) {
				views.forEach(relativeLayout::addSubView);
			} else {
				attributeViews.addAll(views);
			}
		}

		relativeLayout.addSubView(getLinearLayout(attributeViews));
		return relativeLayout;
	}

	private View getLinearLayout(List<View> subViews) {
		View linearLayout = new View("LinearLayout");

		List<String> viewAttributes = getLayoutAttributes("wrap_content", "wrap_content");
		viewAttributes.add("android:layout_alignParentTop=\"true\"");
		viewAttributes.add("android:layout_toEndOf=\"@+id/border\"");
		viewAttributes.add("android:orientation=\"vertical\"");

		linearLayout.setViewAttributes(viewAttributes);
		linearLayout.setSubViews(subViews);

		return linearLayout;
	}




}
