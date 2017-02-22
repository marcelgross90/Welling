package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.Category;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.visitors.DetailCardSubViewVisitor;

import java.util.List;

public class DetailCardLayoutGenerator extends AbstractLayoutGenerator {

	private final AppDescription appDescription;
	private final DetailView detailView;
	private final String packageName;

	public DetailCardLayoutGenerator(AppDescription appDescription, String fileName, DetailView detailView) {
		super(fileName, appDescription.getLibResDirectory());

		this.appDescription = appDescription;
		this.detailView = detailView;
		this.packageName = appDescription.getLibPackageName();
	}

	@Override
	protected View generateLayout() {
		return getLinearLayout();
	}

	private View getLinearLayout() {
		View linearLayout = getBaseLinearLayout();

		DetailCardSubViewVisitor viewVisitor = new DetailCardSubViewVisitor(appDescription, packageName);
		for (Category category : detailView.getCategories()) {
			linearLayout.addSubView(getGroupHeaderView(category));
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

	private View getGroupHeaderView(Category category) {
		List<String> viewAttributes = getLayoutAttributes("match_parent", "wrap_content");
		viewAttributes.add("android:layout_marginTop=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginLeft=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:orientation=\"vertical\"");

		View linearLayout = new View("LinearLayout");
		linearLayout.setViewAttributes(viewAttributes);
		linearLayout.addSubView(getGroupHeadlineView(category));
		linearLayout.addSubView(getSeparatorView());

		return linearLayout;
	}

	private View getGroupHeadlineView(Category category) {
		addString("card_caption_" + category.getName().toLowerCase(), category.getName());
		List<String> viewAttributes = getLayoutAttributes("wrap_content", "wrap_content");
		viewAttributes.add("android:layout_marginTop=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginLeft=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_marginRight=\"@dimen/spacing_medium\"");
		viewAttributes.add("android:layout_gravity=\"end\"");
		viewAttributes.add("android:textSize=\"16sp\"");
		viewAttributes.add("android:text=\"@string/card_caption_" + replaceIllegalCharacters(category.getName().toLowerCase()) + "\"");

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

	private void addString(String key, String value) {
		appDescription.setLibStrings(replaceIllegalCharacters(key), value);
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}
}