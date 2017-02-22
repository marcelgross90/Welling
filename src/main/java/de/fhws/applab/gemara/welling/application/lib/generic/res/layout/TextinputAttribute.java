package de.fhws.applab.gemara.welling.application.lib.generic.res.layout;

import java.util.List;

public class TextinputAttribute extends AbstractLayoutGenerator {

	public TextinputAttribute(String directoryName) {
		super("textinput_attribute", directoryName);
	}

	@Override
	protected View generateLayout() {
		View view = new View("android.support.design.widget.TextInputLayout");
		List<String> attributes = getLayoutAttributes("match_parent", "wrap_content");
		attributes.add("xmlns:android=\"http://schemas.android.com/apk/res/android\"");
		attributes.add("xmlns:tools=\"http://schemas.android.com/tools\"");

		view.setViewAttributes(attributes);
		view.addSubView(generateEditText());

		return view;
	}

	private View generateEditText() {
		View view = new View("EditText");
		List<String> attributes = getLayoutAttributes("match_parent", "wrap_content");
		attributes.add("tools:ignore=\"TextFields\"");
		attributes.add("android:id=\"@+id/attribute_et\"");
		attributes.add("android:ems=\"10\"");

		view.setViewAttributes(attributes);

		return view;
	}
}