package de.fhws.applab.gemara.welling.application.lib.generic.java.fragment;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.AbstractModelClass;

import javax.lang.model.element.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;
import static de.fhws.applab.gemara.welling.androidMethods.LifecycleMethods.*;

public class DateTimePickerFragment extends AbstractModelClass {

	private final ClassName calendarClassName = ClassName.get(Calendar.class);
	private final ClassName onDateTimeSetListenerClassName;
	private final ClassName datePickerDialogClassName;


	private final FieldSpec calendar = FieldSpec.builder(calendarClassName, "calendar", Modifier.PRIVATE, Modifier.FINAL).initializer("$T.getInstance()", calendarClassName).build();
	private final FieldSpec listener;

	private final FieldSpec year = FieldSpec.builder(int.class, "year", Modifier.PRIVATE).build();
	private final FieldSpec month = FieldSpec.builder(int.class, "month", Modifier.PRIVATE).build();
	private final FieldSpec day = FieldSpec.builder(int.class, "day", Modifier.PRIVATE).build();


	public DateTimePickerFragment(String packageName) {
		super(packageName + ".generic.fragment", "DateTimePickerFragment");
		this.onDateTimeSetListenerClassName = ClassName.get(this.packageName + "." + this.className, "OnDateTimeSetListener");
		this.datePickerDialogClassName = getDatePickerDialogClassName();

		this.listener = FieldSpec.builder(onDateTimeSetListenerClassName, "listener", Modifier.PRIVATE).build();
	}

	@Override
	public JavaFile javaFile() {
		MethodSpec onCreate = getOnCreateFragment()
				.addStatement("$N = ($T) getTargetFragment()", listener, onDateTimeSetListenerClassName).build();

		MethodSpec onCreateDialog = MethodSpec.methodBuilder("onCreateDialog")
				.addAnnotation(Override.class)
				.addAnnotation(getNonNullClassName())
				.addParameter(getSavedInstanceStateParam())
				.addModifiers(Modifier.PUBLIC).returns(getDialogClassName())
				.addStatement("return new $T(getActivity(), this, $N.get($T.YEAR), $N.get($T.MONTH), $N.get($T.DAY_OF_MONTH))",
						datePickerDialogClassName, calendar, calendarClassName,calendar, calendarClassName, calendar, calendarClassName )
				.build();

		MethodSpec onDateSet = MethodSpec.methodBuilder("onDateSet")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(getDatePickerClassName(), "datePicker")
				.addParameter(ParameterSpec.builder(int.class, "year", Modifier.FINAL).build())
				.addParameter(ParameterSpec.builder(int.class, "month", Modifier.FINAL).build())
				.addParameter(ParameterSpec.builder(int.class, "day", Modifier.FINAL).build())
				.addStatement("this.$N = year", year)
				.addStatement("this.$N = month", month)
				.addStatement("this.$N = day", day)
				.addStatement("$T timePickerDialog = new $T(getContext(), this, $N.get($T.HOUR_OF_DAY), $N.get($T.MINUTE), $T.is24HourFormat(getActivity()))",
						getTimePickerDialogClassName(), getTimePickerDialogClassName(), calendar, calendarClassName, calendar, calendarClassName, getDateFormatClassName())
				.addStatement("timePickerDialog.show()")
				.build();

		MethodSpec onTimeSet = MethodSpec.methodBuilder("onTimeSet")
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.addAnnotation(Override.class)
				.addParameter(getTimePickerClassName(), "timePicker")
				.addParameter(int.class, "hour")
				.addParameter(int.class, "minute")
				.addStatement("$T simpleDateFormat = new $T(\"yyyy-MM-dd'T'HH:mm\", $T.GERMANY)",
						ClassName.get(SimpleDateFormat.class), ClassName.get(SimpleDateFormat.class), ClassName.get(Locale.class))
				.addStatement("$T dateTimeString = new $T()", ClassName.get(StringBuilder.class), ClassName.get(StringBuilder.class))
				.addStatement("dateTimeString.append(year)")
				.addStatement("dateTimeString.append(\"-\")")
				.addStatement("dateTimeString.append(month + 1)")
				.addStatement("dateTimeString.append(\"-\")")
				.addStatement("dateTimeString.append(day)")
				.addStatement("dateTimeString.append(\"T\")")
				.addStatement("dateTimeString.append(hour)")
				.addStatement("dateTimeString.append(\":\")")
				.addStatement("dateTimeString.append(minute)")
				.addStatement("$T date", ClassName.get(Date.class))
				.beginControlFlow("try")
				.addStatement("date = simpleDateFormat.parse(dateTimeString.toString())")
				.endControlFlow()
				.beginControlFlow("catch($T e)", ClassName.get(ParseException.class))
				.addStatement("date = new $T()", ClassName.get(Date.class))
				.endControlFlow()
				.addStatement("$N.onDateTimeSet(date)", listener)
				.build();

		TypeSpec type = TypeSpec.classBuilder(this.className).superclass(getDialogFragmentClassName()).addSuperinterface(getOnDateSetListenerClassName()).addSuperinterface(getOnTimeSetListenerClassName())
				.addModifiers(Modifier.PUBLIC)
				.addField(calendar)
				.addField(listener)
				.addField(year)
				.addField(month)
				.addField(day)
				.addType(generateInterface())
				.addMethod(onCreate)
				.addMethod(onCreateDialog)
				.addMethod(onDateSet)
				.addMethod(onTimeSet)
				.build();


		return JavaFile.builder(this.packageName, type).build();
	}

	private TypeSpec generateInterface() {
		MethodSpec onDateTimeSet = MethodSpec.methodBuilder("onDateTimeSet")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(void.class)
				.addParameter(ClassName.get(Date.class), "date")
				.build();

		return TypeSpec.interfaceBuilder(onDateTimeSetListenerClassName).addModifiers(Modifier.PUBLIC)
				.addMethod(onDateTimeSet).build();
	}
}
