package de.fhws.applab.gemara.welling.metaModel.view.cardViews.visitor;

import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.GroupedViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.SingleViewObject;

public interface TitleVisitor {

	String visitForTitle(SingleViewObject singleViewObject, AppResource resource);
	String visitForTitle(GroupedViewObject groupedViewObject, AppResource resource);
}
