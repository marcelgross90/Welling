package de.fhws.applab.gemara.welling.metaModel.view.viewObject.visitors;

import de.fhws.applab.gemara.welling.application.lib.generic.res.layout.AbstractLayoutGenerator;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.GroupedViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.SingleViewObject;

import java.util.List;

public interface ViewObjectXMLVisitor {

	List<AbstractLayoutGenerator.View> visitForCardSubView(SingleViewObject singleViewObject, String packageName);
	List<AbstractLayoutGenerator.View> visitForCardSubView(GroupedViewObject groupedViewObject, String packageName);
	AbstractLayoutGenerator.View visitForDetailCardSubView(SingleViewObject singleViewObject, String packageName);
	AbstractLayoutGenerator.View visitForDetailCardSubView(GroupedViewObject groupedViewObject, String packageName);
}
