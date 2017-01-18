package de.fhws.applab.gemara.welling.metaModel.view.cardViews.visitor;

import de.fhws.applab.gemara.welling.metaModel.AppResource;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.GroupedViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.SingleViewObject;
import de.fhws.applab.gemara.welling.metaModel.view.viewObject.ViewAttribute;

import java.util.List;

public class TitleVisitorImpl implements TitleVisitor {

	@Override
	public String visitForTitle(SingleViewObject singleViewObject, AppResource resource) {
		return resource.getResourceName().toLowerCase() + "." + singleViewObject.getViewAttribute().getGetter() + "()";
	}

	@Override
	public String visitForTitle(GroupedViewObject groupedViewObject, AppResource resource) {
		String result = "";
		List<ViewAttribute> attributes = groupedViewObject.getViewAttribute();
		for (int i = 0; i < attributes.size(); i++) {
			result += resource.getResourceName().toLowerCase() + "." + attributes.get(i).getGetter() + "()";
			if (i < attributes.size() -1) {
				result += "+ \" \" + ";
			}
		}

		return result;
	}
}
