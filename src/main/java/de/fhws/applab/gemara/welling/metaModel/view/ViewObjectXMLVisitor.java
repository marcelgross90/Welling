package de.fhws.applab.gemara.welling.metaModel.view;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;

import java.util.List;

public interface ViewObjectXMLVisitor {

	List<AbstractLayoutGenerator.View> visitForCardSubView(SingleViewObject singleViewObject, String packageName);
	List<AbstractLayoutGenerator.View> visitForCardSubView(GroupedViewObject groupedViewObject, String packageName);
	List<AbstractLayoutGenerator.View> visitForDetailCardSubView(SingleViewObject singleViewObject, String packageName);
	List<AbstractLayoutGenerator.View> visitForDetailCardSubView(GroupedViewObject groupedViewObject, String packageName);
}
