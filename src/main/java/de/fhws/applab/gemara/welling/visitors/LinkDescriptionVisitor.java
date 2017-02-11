package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.generator.AppDescription;

public class LinkDescriptionVisitor implements ResourceViewAttributeVisitor {

	private final AppDescription appDescription;

	public LinkDescriptionVisitor(AppDescription appDescription) {
		this.appDescription = appDescription;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		addString(singleResourceViewAttribute.getDisplayViewAttribute());

	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		addString(groupResourceViewAttribute.getGroupResouceViewAttribute());
		groupResourceViewAttribute.getDisplayViewAttributes().forEach(this::addString);

	}

	private void addString(DisplayViewAttribute displayViewAttribute) {
		String linkDescription = displayViewAttribute.getLinkDescription();
		if (linkDescription != null) {
			if (!linkDescription.trim().isEmpty()) {
				appDescription.setLibStrings(replaceIllegalCharacters(linkDescription.toLowerCase()), linkDescription);
			}
		}
	}

	private String replaceIllegalCharacters(String input) {
		return input.replace("-", "_").replace(" ", "_");
	}

}
