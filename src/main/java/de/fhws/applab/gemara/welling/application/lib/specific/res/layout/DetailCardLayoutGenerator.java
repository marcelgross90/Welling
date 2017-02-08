package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.Category;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.visitors.DetailCardSubViewVisitor;

import java.util.ArrayList;
import java.util.List;

public class DetailCardLayoutGenerator extends AbstractLayoutGenerator {

	private final DetailView detailView;
	private final String packageName;

	public DetailCardLayoutGenerator(String fileName, String directoryName, String packageName, DetailView detailView) {
		super(fileName, directoryName);

		this.detailView = detailView;
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
		viewAttributes.add("android:id=\"@+id/detailCardView\"");
		View cardView = new View("android.support.v7.widget.CardView");
		cardView.setViewAttributes(viewAttributes);
		cardView.addSubView(getLinearLayout());

		return cardView;
	}

	private View getLinearLayout() {
		View linearLayout = getBaseLinearLayout();

		DetailCardSubViewVisitor viewVisitor = new DetailCardSubViewVisitor(packageName);
		for (Category category : detailView.getCategories()) {
			linearLayout.addSubView(getGroupHeaderView(category.getName()));
			for (ResourceViewAttribute resourceViewAttribute : category.getResourceViewAttributes()) {
				resourceViewAttribute.accept(viewVisitor);
				linearLayout.addSubView(viewVisitor.getView());
			}
		}
		return linearLayout;
	}


	private View getBaseLinearLayout() {
		List<String> viewAttributes = getLayoutAttributes("match_parent", "wrap_content");
		viewAttributes.add("android:orientation=\"vertical\"");
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");

		View baseLinearLayout = new View("LinearLayout");
		baseLinearLayout.setViewAttributes(viewAttributes);


		return baseLinearLayout;
	}

	private View getGroupHeaderView(String header) {
		List<String> viewAttributes = getLayoutAttributes("match_parent", "wrap_content");
		viewAttributes.add("android:layout_marginTop=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginLeft=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:orientation=\"vertical\"");

		View linearLayout = new View("LinearLayout");
		linearLayout.setViewAttributes(viewAttributes);
		linearLayout.addSubView(getGroupHeadlineView(header));
		linearLayout.addSubView(getSeparatorView());

		return linearLayout;
	}

	private View getGroupHeadlineView(String header) {
		List<String> viewAttributes = getLayoutAttributes("wrap_content", "wrap_content");
		viewAttributes.add("android:layout_marginTop=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginLeft=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_gravity=\"end\"");
		viewAttributes.add("android:textSize=\"16sp\"");
		viewAttributes.add("android:text=\"@string/card_caption_" + header.toLowerCase() + "\"");

		View textView = new View("TextView");
		textView.setViewAttributes(viewAttributes);

		return textView;
	}

	private View getSeparatorView() {
		List<String> viewAttributes = getLayoutAttributes("match_parent", "1dp");
		viewAttributes.add("android:background=\"@android:color/darker_gray\"");
		viewAttributes.add("android:layout_marginTop=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginLeft=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_medium\"");

		View separator = new View("View");
		separator.setViewAttributes(viewAttributes);

		return separator;
	}
}
