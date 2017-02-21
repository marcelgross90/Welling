package de.fhws.applab.gemara.welling.application.lib.generic.java.customView;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.metaModelExtension.AppDeclareStyleable;

import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static de.fhws.applab.gemara.welling.application.androidSpecifics.AndroidSpecificClasses.*;

public class DateTimeView extends de.fhws.applab.gemara.welling.application.lib.generic.java.customView.CustomView {

	private final AppDescription appDescription;

	private final ClassName rClassName;

	public DateTimeView(AppDescription appDescription) {
		super(appDescription.getLibPackageName() + ".generic.customView", "DateTimeView", getTextViewClassName());
		this.appDescription = appDescription;
		this.rClassName = ClassName.get(appDescription.getLibPackageName(), "R");
	}

	@Override
	public Modifier[] addClassModifiers() {
		return new Modifier[]{Modifier.PUBLIC};
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
		appDescription.setDeclareStyleables("dateTimeView", new AppDeclareStyleable.DeclareStyleable("DateTimeView"));
		ClassName calendar = ClassName.get(Calendar.class);
		return getInitMethodSignature()
				.addStatement("$T typedArray = $N.getTheme().obtainStyledAttributes(attributeSet, $T.styleable.DateTimeView, $N, 0)",
				getTypedArrayClassName(), getContextParam(), rClassName, defStyleAttr)
				.beginControlFlow("try")
				.addStatement("$T $N = $T.getInstance()", calendar, "calendar", calendar)
				.addStatement("$T $N = $N.get($T.DAY_OF_MONTH) + \".\" + $N.get($T.MONTH) + \".\" + $N.get($T.YEAR) + \" \" + $N.get($T.HOUR_OF_DAY) + \":\" + $N.get($T.MINUTE)",
						String.class, "date", "calendar", calendar, "calendar", calendar, "calendar", calendar, "calendar", calendar, "calendar", calendar)
				.addStatement("setText($N)", "date")
				.endControlFlow()
				.beginControlFlow("finally")
				.addStatement("$N.recycle()", "typedArray")
				.endControlFlow()
				.build();
	}

	@Override
	public List<MethodSpec> addAdditionalMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(getSetDate());
		return methods;
	}

	private MethodSpec getSetDate() {
		ClassName simpleDateFormat = ClassName.get(SimpleDateFormat.class);
		ParameterSpec date = ParameterSpec.builder(Date.class, "date").build();
		return MethodSpec.methodBuilder("setDate")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addParameter(date)
				.addStatement("$T simpleDateFormat = new $T(\"dd.MM.yyyy HH:mm\", $T.GERMANY)",
						simpleDateFormat, simpleDateFormat, ClassName.get(Locale.class))
				.addStatement("setText(simpleDateFormat.format($N))", date)
				.build();
	}
}
