package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.visitors.cardView.CardAttributeVisitor;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutGenerator extends AbstractLayoutGenerator {

	private final String packageName;
	private final CardView cardView;

	public CardLayoutGenerator(String fileName, String directoryName, CardView cardView, String packageName) {
		super(fileName, directoryName);
		this.cardView = cardView;
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
		//viewAttributes.add("android:id=\"@+id/card_" + appResource.getAttributeName().toLowerCase() + "\"");
		View cardView = new View("android.support.v7.widget.CardView");
		cardView.setViewAttributes(viewAttributes);
		cardView.addSubView(getRelativeLayout());

		return cardView;
	}

	private View getRelativeLayout() {
		View relativeLayout = new View("RelativeLayout");
		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("android:id=\"@+id/relativeLayout\"");

		relativeLayout.setViewAttributes(viewAttributes);
		List<View> attributeViews = new ArrayList<>();


		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			CardAttributeVisitor visitor = new CardAttributeVisitor(packageName);
			resourceViewAttribute.accept(visitor);
			List<View> views =  visitor.getViews();
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
