package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;

import java.util.Map;

import static de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable.DeclareStyleable;

public class Attr extends ValueGenerator {

	private final AppDeclareStyleable appDeclareStyleable;

	public Attr(String directoryName, AppDeclareStyleable appDeclareStyleable) {
		super("attr", directoryName);
		this.appDeclareStyleable = appDeclareStyleable;
	}

	@Override
	public void generateBody() {
		for (DeclareStyleable declareStyleable : this.appDeclareStyleable.getDeclareStyleables()) {
			if (declareStyleable.getAttr().size() == 0) {
				appendln("<declare-styleable name=\"" + declareStyleable.getName() + "\" />");
			} else {
				Map<String, String> attr = declareStyleable.getAttr();
				appendln("<declare-styleable name=\"" + declareStyleable.getName() + "\" >");
				for (String name : attr.keySet()) {
					appendln("<attr name=\"" + name + "\" format=\"" + attr.get(name) + "\"/>");
				}
				appendln("</declare-styleable>");
			}
		}
	}

}
