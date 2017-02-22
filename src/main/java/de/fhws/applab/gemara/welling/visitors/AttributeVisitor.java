package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.attributes.IAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.attributes.embedded.EmbeddedCollectionResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.embedded.EmbeddedResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.linked.LinkedResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleDatatype;
import de.fhws.applab.gemara.enfield.metamodel.attributes.sub.ResourceAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.sub.ResourceCollectionAttribute;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.Attribute;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.LinkAttribute;

import javax.lang.model.element.Modifier;

public class AttributeVisitor implements IAttributeVisitor {

	private Attribute attribute;

	private final String packageName;

	public AttributeVisitor(String packageName) {
		this.packageName = packageName;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	@Override
	public void visit(SimpleAttribute simpleAttribute) {
		if (simpleAttribute.getDatatype() == SimpleDatatype.LINK || simpleAttribute.getDatatype() == SimpleDatatype.IMAGE) {
			this.attribute = new LinkAttribute(simpleAttribute.getAttributeName(), packageName, Modifier.PRIVATE);
		} else if (simpleAttribute.getDatatype() == SimpleDatatype.DATE) {
			this.attribute = new de.fhws.applab.gemara.welling.application.lib.specific.java.model.SimpleAttribute(
					simpleAttribute.getAttributeName(), Attribute.DataType.DATE, Modifier.PRIVATE);
		} else if (simpleAttribute.getDatatype() == SimpleDatatype.INT) {
			this.attribute = new de.fhws.applab.gemara.welling.application.lib.specific.java.model.SimpleAttribute(
					simpleAttribute.getAttributeName(), Attribute.DataType.INT, Modifier.PRIVATE);
		} else {
			this.attribute = new de.fhws.applab.gemara.welling.application.lib.specific.java.model.SimpleAttribute(
					simpleAttribute.getAttributeName(), Attribute.DataType.STRING, Modifier.PRIVATE);
		}
	}

	@Override
	public void visit(EmbeddedResourceAttribute embeddedResourceAttribute) {

	}

	@Override
	public void visit(EmbeddedCollectionResourceAttribute embeddedCollectionResourceAttribute) {

	}

	@Override
	public void visit(LinkedResourceAttribute linkedResourceAttribute) {
		this.attribute = new LinkAttribute(linkedResourceAttribute.getAttributeName(), packageName, Modifier.PRIVATE);
	}

	@Override
	public void visit(ResourceAttribute resourceAttribute) {

	}

	@Override
	public void visit(ResourceCollectionAttribute resourceCollectionAttribute) {
		this.attribute = new LinkAttribute(resourceCollectionAttribute.getAttributeName(), packageName, Modifier.PRIVATE);
	}
}