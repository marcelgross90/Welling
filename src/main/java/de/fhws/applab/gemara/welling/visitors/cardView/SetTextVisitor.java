package de.fhws.applab.gemara.welling.visitors.cardView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.GroupResourceViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.SingleResourceViewAttribute;
import de.fhws.applab.gemara.welling.application.util.GetterSetterGenerator;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute.AttributeType;

import java.util.List;

public class SetTextVisitor implements ResourceViewAttributeVisitor {

	private MethodSpec.Builder builder;
	private final ClassName rClassName;
	private final String specificResourceName;

	public SetTextVisitor(MethodSpec.Builder builder, ClassName rClassName, String specificResourceName) {
		this.builder = builder;
		this.rClassName = rClassName;
		this.specificResourceName = specificResourceName;
	}

	@Override
	public void visit(SingleResourceViewAttribute singleResourceViewAttribute) {
		DisplayViewAttribute displayViewAttribute = singleResourceViewAttribute.getDisplayViewAttribute();

		if (displayViewAttribute.getAttributeType() == AttributeType.PICTURE) {
			builder.addStatement("$N.loadImage($N.$L(), $T.dimen.picture_width, $T.dimen.picture_height)", displayViewAttribute.getAttributeName(),
					specificResourceName, GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName()), rClassName, rClassName);
		} else if (displayViewAttribute.getAttributeType() == AttributeType.URL) {
			builder.addStatement("$N.setText(getResources().getText($T.string.$N))", displayViewAttribute.getAttributeName(), rClassName,
					displayViewAttribute.getLinkDescription());
		} else {
			builder.addStatement("$N.setText($N.$L())", displayViewAttribute.getAttributeName(), specificResourceName, GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName()));
		}
	}

	@Override
	public void visit(GroupResourceViewAttribute groupResourceViewAttribute) {
		List<DisplayViewAttribute> displayViewAttributes = groupResourceViewAttribute.getDisplayViewAttributes();
		String literal = "";
		for (int i = 0; i < displayViewAttributes.size(); i++) {
			if (i == 0) {
				literal += specificResourceName + "." + GetterSetterGenerator.getGetter(displayViewAttributes.get(i).getAttributeName()) + "()";
			} else {
				literal += "+ \" \" + " + specificResourceName + "." + GetterSetterGenerator.getGetter(
						displayViewAttributes.get(i).getAttributeName()) + "()";
			}
		}

		builder.addStatement("$N.setText($L)", groupResourceViewAttribute.getGroupResouceViewAttribute().getAttributeName(), literal);
	}

}
