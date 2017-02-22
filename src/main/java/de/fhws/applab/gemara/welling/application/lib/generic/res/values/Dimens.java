package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

public class Dimens extends ValueGenerator {

	public Dimens(String directoryName) {
		super("dimens", directoryName);
	}

	@Override
	public void generateBody() {
		appendln("<dimen name=\"spacing_small\">4dp</dimen>");
		appendln("<dimen name=\"spacing_medium\">8dp</dimen>");
		appendln("<dimen name=\"picture_height\">170dp</dimen>");
		appendln("<dimen name=\"picture_width\">110dp</dimen>");
	}
}