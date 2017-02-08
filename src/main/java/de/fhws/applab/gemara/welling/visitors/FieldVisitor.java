package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute.AttributeType;

import javax.lang.model.element.Modifier;

public class FieldVisitor implements ResourceViewAttributeVisitor {

	private FieldSpec fieldSpec;

	private final ClassName attributeViewClassName;
	private final ClassName profileImageViewClassName;

	public FieldVisitor(ClassName attributeViewClassName, ClassName profileImageViewClassName) {
		this.attributeViewClassName = attributeViewClassName;
		this.profileImageViewClassName = profileImageViewClassName;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {

		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		String viewName = displayViewAttribute.getAttributeName();

		if (displayViewAttribute.getAttributeType() == AttributeType.PICTURE) {
			this.fieldSpec =  FieldSpec.builder(profileImageViewClassName, viewName, Modifier.PRIVATE).build();
		} else {
			addField(viewName);
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = groupResourceViewAttribute.getGroupResouceViewAttribute();
		String viewName = displayViewAttribute.getAttributeName();
		addField(viewName);
	}

	private void addField(String viewName) {
		this.fieldSpec = FieldSpec.builder(attributeViewClassName, viewName, Modifier.PRIVATE).build();
	}

	public FieldSpec getFieldSpec() {
		return this.fieldSpec;
	}
}
