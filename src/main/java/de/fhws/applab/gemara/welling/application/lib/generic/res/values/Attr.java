package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModelExtension.AppDeclareStyleable;

import java.util.Map;

public class Attr extends ValueGenerator {

	private final AppDeclareStyleable appDeclareStyleable;

	public Attr(String directoryName, AppDeclareStyleable appDeclareStyleable) {
		super("attr", directoryName);
		this.appDeclareStyleable = appDeclareStyleable;
	}

	@Override
	public void generateBody() {
		for (String key : this.appDeclareStyleable.getDeclareStyleables().keySet()) {
			if (appDeclareStyleable.getDeclareStyleables().get(key).getAttr().size() == 0) {
				appendln("<declare-styleable name=\"" + appDeclareStyleable.getDeclareStyleables().get(key).getName() + "\" />");
			} else {
				Map<String, String> attr = appDeclareStyleable.getDeclareStyleables().get(key).getAttr();
				appendln("<declare-styleable name=\"" + appDeclareStyleable.getDeclareStyleables().get(key).getName() + "\" >");
				for (String name : attr.keySet()) {
					appendln("<attr name=\"" + name + "\" format=\"" + attr.get(name) + "\"/>");
				}
				appendln("</declare-styleable>");
			}
		}
	}

}
