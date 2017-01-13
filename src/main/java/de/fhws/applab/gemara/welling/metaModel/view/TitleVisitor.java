package de.fhws.applab.gemara.welling.metaModel.view;

import de.fhws.applab.gemara.welling.metaModel.AppResource;

public interface TitleVisitor {

	String visitForTitle(SingleViewObject singleViewObject, AppResource resource);
	String visitForTitle(GroupedViewObject groupedViewObject, AppResource resource);
}
