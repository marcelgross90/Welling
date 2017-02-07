package de.fhws.applab.gemara.welling.visitors.cardView;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.application.util.GetterSetterGenerator;

import java.util.List;

public class TitleVisitor implements ResourceViewAttributeVisitor {

	private String title;
	private final String resourceName;

	public TitleVisitor(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();

		this.title = resourceName.toLowerCase() + "." + GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName()) + "()";
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		String result = "";

		List<DisplayViewAttribute> attributes = groupResourceViewAttribute.getDisplayViewAttributes();

		for (int i = 0; i < attributes.size(); i++) {
			result += resourceName + "." + GetterSetterGenerator.getGetter(attributes.get(i).getAttributeName()) + "()";
			if (i < attributes.size() -1) {
				result += "+ \" \" + ";
			}
		}
		 this.title = result;
	}

	public String getTitle() {
		return title;
	}
}
