package de.fhws.applab.gemara.welling.application.lib.specific.res.layout;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputViewAttribute;
import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;

import java.util.ArrayList;
import java.util.List;

public class InputLayoutGenerator extends AbstractLayoutGenerator {

	private final InputView inputView;
	private final String packageName;

	public InputLayoutGenerator(String fileName, String directoryName, String packageName, InputView inputView) {
		super(fileName, directoryName);
		this.inputView = inputView;
		this.packageName = packageName;
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
			List<String> viewAttributes = new ArrayList<>();
			viewAttributes.add("android:id=\"@+id/" + inputViewAttribute.getAttributeName() + "\"");
			viewAttributes.add("android:layout_width=\"match_parent\"");
			viewAttributes.add("android:layout_height=\"wrap_content\"");
			viewAttributes.add("custom:hintText=\"@string/" + inputViewAttribute.getHintText().toLowerCase() + "\"");
			if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.TEXT) {
				viewAttributes.add("custom:inputType=\"text\"");
			} else if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.MAIL) {
				viewAttributes.add("custom:inputType=\"mail\"");
			} else if (inputViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PHONE_NUMBER) {
				viewAttributes.add("custom:inputType=\"phone\"");
			}

			View inputView = new View(packageName + ".AttributeInput");
			inputView.setViewAttributes(viewAttributes);

			attributeInputViews.add(inputView);
		}

		return attributeInputViews;
	}
}
