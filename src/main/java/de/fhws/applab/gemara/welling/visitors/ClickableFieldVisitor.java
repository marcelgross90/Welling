package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

import javax.lang.model.element.Modifier;

public class ClickableFieldVisitor implements ResourceViewAttributeVisitor {

	private final TypeSpec.Builder type;
	private final ClassName attributeViewClassName;

	public ClickableFieldVisitor(TypeSpec.Builder type, ClassName attributeViewClassName) {
		this.type = type;
		this.attributeViewClassName = attributeViewClassName;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();

		if (displayViewAttribute.isClickActionAndroid()) {
			FieldSpec field = FieldSpec
					.builder(attributeViewClassName, displayViewAttribute.getAttributeName(), Modifier.PRIVATE, Modifier.FINAL).build();
			type.addField(field);
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {

	}
}