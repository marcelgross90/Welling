package de.fhws.applab.gemara.welling.visitors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import de.fhws.applab.gemara.enfield.metamodel.wembley.ViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.DisplayViewAttribute;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.ResourceViewAttributeVisitor;
import de.fhws.applab.gemara.welling.generator.GetterSetterGenerator;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getIntentClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getUriClassName;

@SuppressWarnings("WeakerAccess")
public abstract class ClickActionVisitor implements ResourceViewAttributeVisitor {

	protected final MethodSpec.Builder method;
	protected final String index;
	protected final ClassName rClassName;
	private final String resourceName;
	private final boolean contextNeeded;

	public ClickActionVisitor(MethodSpec.Builder method, String index, ClassName rClassName, String resourceName, boolean contextNeeded) {
		this.method = method;
		this.index = index;
		this.rClassName = rClassName;
		this.resourceName = resourceName;
		this.contextNeeded = contextNeeded;
	}

	protected void getRightAction(DisplayViewAttribute displayViewAttribute) {
		if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.MAIL) {
			method.addStatement("$T $N = new $T($T.ACTION_SENDTO, $T.parse($S + $N))", getIntentClassName(), "intent", getIntentClassName(),
					getIntentClassName(), getUriClassName(), "mailto:",
					resourceName + "." + GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName() + "()"));
			if (contextNeeded) {
				method.addStatement("$N.startActivity($T.createChooser($N, $S))", "context", getIntentClassName(), "intent", "Send Mail");
			} else {
				method.addStatement("startActivity($T.createChooser($N, $S))", getIntentClassName(), "intent", "Send Mail");
			}

		} else if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.PHONE_NUMBER) {
			method.addStatement("$T $N = new $T($T.ACTION_DIAL)", getIntentClassName(), "dialIntent", getIntentClassName(),
					getIntentClassName());
			method.addStatement("$N.setData($T.parse($S + $N))", "dialIntent", getUriClassName(), "tel:",
					resourceName + "." + GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName()) + "()");
			if (contextNeeded) {
				method.addStatement("$N.startActivity($N)", "context", "dialIntent");
			} else {
				method.addStatement("startActivity($N)", "dialIntent");
			}

		} else if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.LOCATION) {
			method.addStatement("$T $N = $T.parse($S + $N)", getUriClassName(), "location", getUriClassName(), "geo:0,0?q=",
					resourceName + "." + GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName()) + "()");
			method.addStatement("$T $N = new $T($T.ACTION_VIEW, $N)", getIntentClassName(), "mapIntent", getIntentClassName(),
					getIntentClassName(), "location");
			method.addStatement("$N.setPackage($S)", "mapIntent", "com.google.android.apps.maps");

			if (contextNeeded) {
				method.addStatement("$N.startActivity($N)", "context", "mapIntent");
			} else {
				method.addStatement("startActivity($N)", "mapIntent");
			}
		} else if (displayViewAttribute.getAttributeType() == ViewAttribute.AttributeType.URL) {
			method.addStatement("$T $N = new $T($T.ACTION_VIEW, $T.parse($N))", getIntentClassName(), "browserIntent", getIntentClassName(),
					getIntentClassName(), getUriClassName(),
					resourceName + "." + GetterSetterGenerator.getGetter(displayViewAttribute.getAttributeName()) + "().getHref()");
			if (contextNeeded) {
				method.addStatement("$N.startActivity($N)", "context", "browserIntent");
			} else {
				method.addStatement("startActivity($N)", "browserIntent");
			}
		}
	}
}
