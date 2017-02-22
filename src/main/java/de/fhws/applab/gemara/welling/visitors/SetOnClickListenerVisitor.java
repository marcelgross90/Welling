package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;

public class SetOnClickListenerVisitor implements ResourceViewAttributeVisitor {

	private final MethodSpec.Builder method;
	private final String listenerName;

	public SetOnClickListenerVisitor(MethodSpec.Builder method, String listenerName) {
		this.method = method;
		this.listenerName = listenerName;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();
		if (displayViewAttribute.isClickActionAndroid()) {
			method.addStatement("$N.setOnClickListener($N)", displayViewAttribute.getAttributeName(), listenerName);
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {

	}
}