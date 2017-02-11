package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.visitors.CardAttributeVisitor;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutGenerator extends AbstractLayoutGenerator {

	private final String packageName;
	private final CardView cardView;
	private final AppDescription appDescription;

	public CardLayoutGenerator(AppDescription appDescription, String fileName, CardView cardView) {
		super(fileName, appDescription.getLibResDirectory());
		this.appDescription = appDescription;
		this.cardView = cardView;
		this.packageName = appDescription.getLibPackageName();
	}

	@Override
	protected View generateLayout() {
		return getRelativeLayout();
	}


	private View getRelativeLayout() {
		View relativeLayout = new View("RelativeLayout");
		List<String> viewAttributes = getLayoutAttributes("match_parent", "match_parent");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("android:id=\"@+id/relativeLayout\"");

		relativeLayout.setViewAttributes(viewAttributes);
		List<View> attributeViews = new ArrayList<>();


		for (ResourceViewAttribute resourceViewAttribute : cardView.getResourceViewAttributes()) {
			CardAttributeVisitor visitor = new CardAttributeVisitor(appDescription,packageName);
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
		viewAttributes.add("android:layout_toRightOf=\"@+id/border\"");
		viewAttributes.add("android:layout_toEndOf=\"@+id/border\"");
		viewAttributes.add("android:orientation=\"vertical\"");

		linearLayout.setViewAttributes(viewAttributes);
		linearLayout.setSubViews(subViews);

		return linearLayout;
	}




}
