package de.fhws.applab.gemara.welling.androidConstants;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;

public class AndroidSpecificClasses {

	//ClassNames
	public static ClassName getContextClass() {
		return ClassName.get("android.content", "Context");
	}

	public static ClassName getLogClass() {
		return ClassName.get("android.util", "Log");
	}

	public static ClassName getViewClassName() {
		return ClassName.get("android.view", "View");
	}

	public static ClassName getLayoutInflaterClassName() {
		return ClassName.get("android.view", "LayoutInflater");
	}

	public static ClassName getViewGroupClassName() {
		return ClassName.get("android.view", "ViewGroup");
	}

	public static ClassName getBundleClassName() {
		return ClassName.get("android.os", "Bundle");
	}

	public static ClassName getFragmentManagerClassName() {
		return ClassName.get("android.support.v4.app", "FragmentManager");
	}

	public static ClassName getFragmentClassName() {
		return ClassName.get("android.support.v4.app", "Fragment");
	}

	public static ClassName getProgressBarClassName() {
		return ClassName.get("android.widget", "ProgressBar");
	}

	public static ClassName getLinearLayoutManagerClassName() {
		return ClassName.get("android.support.v7.widget", "LinearLayoutManager");
	}

	public static ClassName getOnScrollListenerClassName() {
		return  ClassName.get("android.support.v7.widget.RecyclerView", "OnScrollListener");
	}

	public static ClassName getRecyclerViewClassName() {
		return ClassName.get("android.support.v7.widget", "RecyclerView");
	}

	public static ClassName getViewHolderClassName() {
		return ClassName.get("android.support.v7.widget.RecyclerView", "ViewHolder");
	}

	public static ClassName getAdapterClassName() {
		return ClassName.get("android.support.v7.widget.RecyclerView", "Adapter");
	}

	public static ClassName getDatePickerDialogClassName() {
		return ClassName.get("android.app", "DatePickerDialog");
	}

	public static ClassName getOnDateSetListenerClassName() {
		return ClassName.get("android.app.DatePickerDialog", "OnDateSetListener");
	}

	public static ClassName getDialogClassName() {
		return ClassName.get("android.app", "Dialog");
	}

	public static ClassName getTimePickerDialogClassName() {
		return ClassName.get("android.app", "TimePickerDialog");
	}

	public static ClassName getOnTimeSetListenerClassName() {
		return ClassName.get("android.app.TimePickerDialog", "OnTimeSetListener");
	}

	public static ClassName getNonNullClassName() {
		return ClassName.get("android.support.annotation", "NonNull");
	}

	public static ClassName getNullableClassName() {
		return ClassName.get("android.support.annotation", "Nullable");
	}

	public static ClassName getDialogFragmentClassName() {
		return ClassName.get("android.support.v4.app", "DialogFragment");
	}

	public static ClassName getDatePickerClassName() {
		return ClassName.get("android.widget", "DatePicker");
	}

	public static ClassName getTimePickerClassName() {
		return ClassName.get("android.widget", "TimePicker");
	}

	public static ClassName getDateFormatClassName() {
		return ClassName.get("android.text.format", "DateFormat");
	}

	public static ClassName getAlertDialogBuilderClassName() {
		return ClassName.get("android.app.AlertDialog", "Builder");
	}

	public static ClassName getDialogInterfaceClassName() {
		return ClassName.get("android.content", "DialogInterface");
	}

	public static ClassName getDialogInterfaceOnClickListenerClassName() {
		return ClassName.get("android.content.DialogInterface", "OnClickListener");
	}

	//Fields
	public static FieldSpec getFragmentManagerField(Modifier... modifiers) {
		return FieldSpec.builder(getFragmentManagerClassName(), "fragmentManager").addModifiers(modifiers).build();
	}

	public static FieldSpec getProgressBarField(Modifier... modifiers) {
		return FieldSpec.builder(getProgressBarClassName(), "progressBar").addModifiers(modifiers).build();
	}


	//Parameter
	public static ParameterSpec getSavedInstanceStateParam() {
		return ParameterSpec.builder(getBundleClassName(), "savedInstanceState").build();
	}


	public static ParameterSpec getContextParam() {
		return ParameterSpec.builder(getContextClass(), "context").build();
	}


}
