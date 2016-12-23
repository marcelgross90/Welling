package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModel.AppStyle;

import java.util.Map;

import static de.fhws.applab.gemara.welling.metaModel.AppStyle.Style;

public class Styles extends ValueGenerator {

	private final AppStyle appStyle;

	public Styles(String directoryName, AppStyle appStyle) {
		super("styles", directoryName);
		this.appStyle = appStyle;
	}

	@Override
	public void generateBody() {

		for (Style style : this.appStyle.getStyles()) {
			if (style.getItems().size() == 0) {
				appendln("<style name=\"" + style.getName() + "\"" + (style.getParent() != null ? " parent=\"" + style.getParent() + "\"/>" : "/>"));
			} else {
				Map<String, String> items = style.getItems();
				appendln("<style name=\"" + style.getName() + "\"" + (style.getParent() != null ? " parent=\"" + style.getParent() + "\">" : ">"));
				for (String key : items.keySet()) {
					appendln("<item name=\"" + key + "\">" + items.get(key) + "</item>");
				}
				appendln("</style>");
			}
		}
	}
}
