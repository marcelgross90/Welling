package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTextViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTypedArrayClassName;

public class AttributeView extends de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView {

	private final ClassName rClassName;

	public AttributeView(String packageName) {
		super(packageName + ".generic.customView", "AttributeView", getTextViewClassName());
		this.rClassName = ClassName.get(packageName, "R");
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
		// @formatter:off
		return getInitMethodSignature()
				.addStatement("$T typedArray = $N.getTheme().obtainStyledAttributes(attributeSet, $T.styleable.AttributeView, $N, 0)",
						getTypedArrayClassName(), getContextParam(), rClassName, defStyleAttr)
				.addStatement("typedArray.recycle()")
				.build();
		// @formatter:on
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		return Collections.emptyList();
	}
}