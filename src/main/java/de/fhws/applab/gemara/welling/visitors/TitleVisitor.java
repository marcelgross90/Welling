package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.generator.GetterSetterGenerator;

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

		this.title = getInputWithSmallStart(resourceName) + "." + GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName())
				+ "()";
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		String result = "";

		List<DisplayViewAttribute> attributes = groupResourceViewAttribute.getDisplayViewAttributes();

		for (int i = 0; i < attributes.size(); i++) {
			result += getInputWithSmallStart(resourceName) + "." + GetterSetterGenerator.getGetter(attributes.get(i).getAttributeName())
					+ "()";
			if (i < attributes.size() - 1) {
				result += "+ \" \" + ";
			}
		}
		this.title = result;
	}

	public String getTitle() {
		return title;
	}

	private String getInputWithSmallStart(String input) {
		return Character.toLowerCase(input.charAt(0)) + input.substring(1);
	}
}