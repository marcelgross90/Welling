package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;

import java.util.ArrayList;
import java.util.List;

public class InputLayoutGenerator extends AbstractLayoutGenerator {

	private final AppDescription appDescription;
	private final InputView inputView;
	private final String packageName;

	public InputLayoutGenerator(AppDescription appDescription, InputView inputView) {
		super("view_" + inputView.getResourceName().toLowerCase() + "_input", appDescription.getLibResDirectory());
		this.inputView = inputView;
		this.appDescription = appDescription;
		this.packageName = appDescription.getLibPackageName() + ".generic.customView";
	}

	@Override
	protected View generateLayout() {
		return getScrollView();
	}

	private View getScrollView() {
		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		viewAttributes.add("xmlns:custom=\"http://schemas.android.com/apk/res-auto\"");
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"match_parent\"");

		View scrollView = new View("ScrollView");
		scrollView.setViewAttributes(viewAttributes);
		scrollView.addSubView(getLinearLayout());

		return scrollView;
	}

	private View getLinearLayout() {
		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:orientation=\"vertical\"");

		View linearLayout = new View("LinearLayout");
		linearLayout.setViewAttributes(viewAttributes);
		linearLayout.setSubViews(getAttributeInputViews());

		return linearLayout;
	}

	private List<View> getAttributeInputViews() {
		List<View> attributeInputViews = new ArrayList<>();

		for (InputViewAttribute inputViewAttribute : inputView.getInputViewAttributes()) {
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.SUBRESOURCE
					|| inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PICTURE) {
				continue;
			}
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.DATE) {
				View innerLinearLayout = getInnerLinearLayout(inputViewAttribute);

				List<String> viewAttributes = new ArrayList<>();
				viewAttributes.add("android:id=\"@+id/" + inputViewAttribute.getAttributeName() + "\"");
				viewAttributes.add("android:layout_width=\"match_parent\"");
				viewAttributes.add("android:layout_height=\"wrap_content\"");
				viewAttributes.add("android:layout_weight=\"0.5\"");

				View dateTimeView = new View(packageName + ".DateTimeView");
				dateTimeView.setViewAttributes(viewAttributes);

				innerLinearLayout.addSubView(dateTimeView);

				attributeInputViews.add(innerLinearLayout);

			} else {
				addString(inputViewAttribute.getAttributeName() + "_hint", inputViewAttribute.getHintText());

				List<String> viewAttributes = new ArrayList<>();
				viewAttributes.add("android:id=\"@+id/" + inputViewAttribute.getAttributeName() + "\"");
				viewAttributes.add("android:layout_width=\"match_parent\"");
				viewAttributes.add("android:layout_height=\"wrap_content\"");
				viewAttributes.add("custom:hintText=\"@string/" + replaceIllegalCharacters(inputViewAttribute.getAttributeName()) + "_hint"
						+ "\"");
				viewAttributes.add(getInputType(inputViewAttribute.getAttributeType()));

				View inputView = new View(packageName + ".AttributeInput");
				inputView.setViewAttributes(viewAttributes);

				attributeInputViews.add(inputView);
			}
		}

		return attributeInputViews;
	}

	private View getInnerLinearLayout(InputViewAttribute inputViewAttribute) {
		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:orientation=\"horizontal\"");

		View view = new View("LinearLayout");
		view.setViewAttributes(viewAttributes);
		view.addSubView(getInnerTextView(inputViewAttribute));

		return view;
	}

	private View getInnerTextView(InputViewAttribute inputViewAttribute) {
		addString(inputViewAttribute.getHintText().toLowerCase(), inputViewAttribute.getHintText());

		List<String> viewAttributes = new ArrayList<>();
		viewAttributes.add("android:layout_width=\"match_parent\"");
		viewAttributes.add("android:layout_height=\"wrap_content\"");
		viewAttributes.add("android:layout_weight=\"0.5\"");
		viewAttributes.add("android:text=\"@string/" + inputViewAttribute.getHintText().toLowerCase() + "\"");

		View view = new View("TextView");
		view.setViewAttributes(viewAttributes);

		return view;
	}

	private String getInputType(ViewAttribute.AttributeType attributeType) {
		if (attributeType == ViewAttribute.AttributeType.MAIL) {
			return "custom:inputType=\"mail\"";
		} else if (attributeType == ViewAttribute.AttributeType.PHONE_NUMBER) {
			return "custom:inputType=\"phone\"";
		}
		return "custom:inputType=\"text\"";
	}

	private void addString(String key, String value) {
		appDescription.setLibStrings(replaceIllegalCharacters(key), value);
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}
}