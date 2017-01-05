package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextClass;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getEditTextClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getInputTypeClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLayoutInflaterClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTextInputLayoutClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTypedArrayClassName;

public class AttributeInput extends de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView {

	private final FieldSpec attribute = FieldSpec.builder(getEditTextClassName(), "attribute", Modifier.PRIVATE).build();

	public AttributeInput(String packageName) {
		super(packageName, "AttributeInput", getTextInputLayoutClassName());
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
		List<FieldSpec> fields = new ArrayList<>();
		fields.add(attribute);
		return fields;
	}

	@Override
	public MethodSpec getInitMethod() {
		return getInitMethodSignature()
				.addStatement("$T inflater = ($T) $N.getSystemService($T.LAYOUT_INFLATER_SERVICE)", getLayoutInflaterClassName(),
						getLayoutInflaterClassName(), getContextParam(), getContextClass())
				.addStatement("this.addView(inflater.inflate($T.layout.textinput_attribute, this, false))", rClass)
				.addStatement("$T typedArray = $N.getTheme().obtainStyledAttributes(attributeSet, $T.styleable.AttributeInput, $N, 0)",
						getTypedArrayClassName(), getContextParam(), rClass, defStyleAttr)
				.beginControlFlow("try")
				.addStatement("this.$N = ($T) findViewById($T.id.attribute_et)", attribute, getEditTextClassName(), rClass)
				.addStatement("this.$N.setHint(typedArray.getResourceId($T.styleable.AttributeInput_hintText, 0))", attribute, rClass)
				.addStatement("this.$N.setInputType($N(typedArray.getString($T.styleable.AttributeInput_inputType)))", attribute,
						getParseTextToInputType(), rClass).endControlFlow().beginControlFlow("finally").addStatement("typedArray.recycle()").endControlFlow()
				.build();
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getParseTextToInputType());
		methods.add(getSetText());
		methods.add(getGetText());
		return methods;
	}

	private MethodSpec getParseTextToInputType() {
		ClassName inputType = getInputTypeClassName();
		return MethodSpec.methodBuilder("parseTextToInputType").addModifiers(Modifier.PRIVATE).returns(int.class)
				.addParameter(String.class, "inputTypeString").addCode(
						"switch ($N) { \n" + "case \"text\":\n" + "return $T.TYPE_CLASS_TEXT;\n" + "case \"phone\":\n"
								+ "return $T.TYPE_CLASS_PHONE;\n" + "case \"mail\":\n"
								+ "return $T.TYPE_CLASS_TEXT | $T.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;\n" + "default:\n"
								+ "return $T.TYPE_CLASS_TEXT;\n" + "} \n", "inputTypeString", inputType, inputType, inputType, inputType,
						inputType).build();
	}

	private MethodSpec getSetText() {
		ParameterSpec text = ParameterSpec.builder(String.class, "text").build();
		return MethodSpec.methodBuilder("setText").addModifiers(Modifier.PRIVATE).returns(void.class).addParameter(text)
				.addStatement("this.$N.setText($N)", attribute, text).build();
	}

	private MethodSpec getGetText() {
		return MethodSpec.methodBuilder("getText").addModifiers(Modifier.PRIVATE).returns(String.class)
				.addStatement("return this.$N.getText().toString().trim()", attribute).build();
	}

}
