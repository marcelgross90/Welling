package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.attributes.IAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.attributes.embedded.EmbeddedCollectionResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.embedded.EmbeddedResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.linked.LinkedResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleDatatype;
import de.fhws.applab.gemara.enfield.metamodel.attributes.sub.ResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.sub.ResourceCollectionAttribute;

public class IAttributeVisitorImpl implements IAttributeVisitor {

	private boolean containsImage = false;

	public boolean isContainsImage() {
		return containsImage;
	}

	@Override
	public void visit(SimpleAttribute simpleAttribute) {
		if (simpleAttribute.getDatatype() == SimpleDatatype.IMAGE) {
			containsImage = true;
		}
	}

	@Override
	public void visit(EmbeddedResourceAttribute embeddedResourceAttribute) {
		containsImage = false;
	}

	@Override
	public void visit(EmbeddedCollectionResourceAttribute embeddedCollectionResourceAttribute) {
		containsImage = false;
	}

	@Override
	public void visit(LinkedResourceAttribute linkedResourceAttribute) {
		containsImage = false;
	}

	@Override
	public void visit(ResourceAttribute resourceAttribute) {
		containsImage = false;
	}

	@Override
	public void visit(ResourceCollectionAttribute resourceCollectionAttribute) {
		containsImage = false;
	}
}