package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getAttributeSetParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBitMapClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getBitMapDrawableClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextClass;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getContextParam;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getDrawableClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getImageViewClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getLoadedFromClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getPicassoClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTargetClassName;
import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.getTypedArrayClassName;

public class ProfileImageView extends de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView {

	private final ClassName builderClassName;
	private final ClassName rClassName;
	private final FieldSpec context = FieldSpec.builder(getContextClass(), "context", Modifier.PRIVATE, Modifier.FINAL).build();

	private final ParameterSpec profileImage;

	public ProfileImageView(String packageName) {
		super(packageName + ".generic.customView", "ProfileImageView", getImageViewClassName());
		ClassName linkClassName = ClassName.get(packageName + ".generic.model", "Link");
		this.rClassName = ClassName.get(packageName, "R");
		this.builderClassName = ClassName.get(packageName + ".generic.model.Link", "Builder");
		this.profileImage = ParameterSpec.builder(linkClassName, "profileImage").build();
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
		fields.add(context);
		return fields;
	}

	@Override
	public MethodSpec getInitMethod() {
		// @formatter:off
		return getInitMethodSignature()
				.addStatement("$T typedArray = $N.getTheme().obtainStyledAttributes(attributeSet, $T.styleable.ProfileImageView, $N, 0)",
						getTypedArrayClassName(), getContextParam(), rClassName, defStyleAttr)
				.addStatement("typedArray.recycle()")
				.build();
		// @formatter:on
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getLoadImage());
		methods.add(getLoadCutImage());
		methods.add(getGetValidUrl());
		return methods;
	}

	@Override
	protected MethodSpec getConstructorOne() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addStatement("super($N)", getContextParam())
				.addStatement("$N($N, null, 0)", getInitMethod(), getContextParam())
				.addStatement("this.$N = $N", context, getContextParam())
				.build();
		// @formatter:on
	}

	@Override
	protected MethodSpec getConstructorTwo() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addStatement("super($N, $N)", getContextParam(), getAttributeSetParam())
				.addStatement("$N($N, $N, 0)", getInitMethod(), getContextParam(), getAttributeSetParam())
				.addStatement("this.$N = $N", context, getContextParam())
				.build();
		// @formatter:on
	}

	@Override
	protected MethodSpec getConstructorThree() {
		// @formatter:off
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(getContextParam())
				.addParameter(getAttributeSetParam())
				.addParameter(defStyleAttr)
				.addStatement("super($N, $N, $N)", getContextParam(), getAttributeSetParam(), defStyleAttr)
				.addStatement("$N($N, $N, $N)", getInitMethod(), getContextParam(), getAttributeSetParam(), defStyleAttr)
				.addStatement("this.$N = $N", context, getContextParam())
				.build();
		// @formatter:on
	}

	private MethodSpec getLoadImage() {
		// @formatter:off
		return MethodSpec.methodBuilder("loadImage")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(profileImage)
				.addParameter(int.class, "width")
				.addParameter(int.class, "height")
				.addStatement("$T $N = $N($N)", String.class, "profileImageUrl", getGetValidUrl(), profileImage)
				.addStatement("$T.with($N).load($N).resizeDimen($N, $N).error($T.drawable.user_picture).into(this)",
						getPicassoClassName(), getContextParam(), "profileImageUrl", "width", "height", rClassName)
				.build();
		// @formatter:on
	}

	private MethodSpec getLoadCutImage() {
		// @formatter:off
		return MethodSpec.methodBuilder("loadCutImage")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addParameter(profileImage)
				.addStatement("$T $N = $N($N)", String.class, "profileImageUrl", getGetValidUrl(), profileImage)
				.addStatement("$T $N = $L", getTargetClassName(), "target", getTargetImpl())
				.addStatement("$T.with($N).load($N).error($T.drawable.user_picture).into($N)",
						getPicassoClassName(), getContextParam(), "profileImageUrl", rClassName, "target")
				.build();
		// @formatter:on
	}

	private TypeSpec getTargetImpl() {
		// @formatter:off
		MethodSpec onPrepareLoad = MethodSpec.methodBuilder("onPrepareLoad")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(getDrawableClassName(), "placeHolderDrawable")
				.build();

		MethodSpec onBitmapLoaded = MethodSpec.methodBuilder("onBitmapLoaded")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(getBitMapClassName(), "bitmap")
				.addParameter(getLoadedFromClassName(), "from")
				.addStatement("$T $N = $T.createBitmap($N, 0, 50, $N.getWidth(), 300)",
						getBitMapClassName(), "editedBitmap", getBitMapClassName(), "bitmap", "bitmap")
				.addStatement("$T $N = new $T(getResources(), $N)",
						getBitMapDrawableClassName(), "drawable", getBitMapDrawableClassName(), "editedBitmap")
				.addStatement("setImageDrawable($N)", "drawable")
				.build();

		MethodSpec onBitmapFailed = MethodSpec.methodBuilder("onBitmapFailed")
				.addModifiers(Modifier.PUBLIC)
				.returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(getDrawableClassName(), "errorDrawable")
				.addStatement("setImageDrawable($N)", "errorDrawable")
				.build();

		return TypeSpec.anonymousClassBuilder("")
				.addSuperinterface(getTargetClassName())
				.addMethod(onPrepareLoad)
				.addMethod(onBitmapLoaded)
				.addMethod(onBitmapFailed)
				.build();
		// @formatter:on
	}

	private MethodSpec getGetValidUrl() {
		// @formatter:off
		return MethodSpec.methodBuilder("getValidUrl")
				.addModifiers(Modifier.PRIVATE)
				.returns(String.class)
				.addParameter(profileImage)
				.beginControlFlow("if ($N != null)", profileImage)
				.addStatement("$T $N = new $T($N)", builderClassName, "linkBuilder", builderClassName, profileImage)
				.addStatement("$N.addQueryParam(\"width\", \"394\")", "linkBuilder")
				.addStatement("$N.addQueryParam(\"height\", \"591\")", "linkBuilder")
				.addStatement("return $N.build()", "linkBuilder")
				.endControlFlow()
				.addStatement("return $S", "empty")
				.build();
		// @formatter:on
	}
}