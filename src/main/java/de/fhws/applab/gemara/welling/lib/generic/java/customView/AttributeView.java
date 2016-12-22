package de.fhws.applab.gemara.welling.lib.generic.java.customView;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getTextViewClassName;
import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.getTypedArrayClassName;

public class AttributeView extends CustomView {

	public AttributeView(String packageName, String className) {
		super(packageName, className, getTextViewClassName());
	}

	@Override
	public Modifier[] addClassModifiers() {
		return new Modifier[] { Modifier.PUBLIC };
	}

	@Override
	public List<MethodSpec> addConstructors() {
		return getStandardConstructors();
	}

	@Override
	public List<FieldSpec> addFields() {
		return Collections.emptyList();
	}

	@Override
	public MethodSpec getInitMethod() {
		return getInitMethodSignature()
				.addStatement("$T typedArray = $N.getTheme().obtainStyledAttributes(attributeSet, $T.styleable.AttributeInput, $N, 0)",
						getTypedArrayClassName(), getContextParam(), rClass, defStyleAttr).addStatement("typedArray.recycle()").build();
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		return Collections.emptyList();
	}
}
