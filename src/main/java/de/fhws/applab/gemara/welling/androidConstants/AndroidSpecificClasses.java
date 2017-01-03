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

	public static ClassName getViewOnClickListenerClassName() {
		return ClassName.get("android.view.View", "OnClickListener");
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

	public static ClassName getOnBackStackChangedListenerClassName() {
		return ClassName.get("android.support.v4.app.FragmentManager", "OnBackStackChangedListener");
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

	public static ClassName getAttributeSetClassName() {
		return ClassName.get("android.util", "AttributeSet");
	}

	public static ClassName getTextInputLayoutClassName() {
		return ClassName.get("android.support.design.widget", "TextInputLayout");
	}

	public static ClassName getEditTextClassName() {
		return ClassName.get("android.widget", "EditText");
	}

	public static ClassName getTypedArrayClassName() {
		return ClassName.get("android.content.res", "TypedArray");
	}

	public static ClassName getInputTypeClassName() {
		return ClassName.get("android.text", "InputType");
	}

	public static ClassName getTextViewClassName() {
		return ClassName.get("android.widget", "TextView");
	}

	public static ClassName getImageViewClassName() {
		return ClassName.get("android.widget", "ImageView");
	}

	public static ClassName getPicassoClassName() {
		return ClassName.get("com.squareup.picasso", "Picasso");
	}

	public static ClassName getLoadedFromClassName() {
		return ClassName.get("com.squareup.picasso.Picasso", "LoadedFrom");
	}

	public static ClassName getTargetClassName() {
		return ClassName.get("com.squareup.picasso", "Target");
	}

	public static ClassName getDrawableClassName() {
		return ClassName.get("android.graphics.drawable", "Drawable");
	}

	public static ClassName getBitMapClassName() {
		return ClassName.get("android.graphics", "Bitmap");
	}

	public static ClassName getBitMapDrawableClassName() {
		return ClassName.get("android.graphics.drawable", "BitmapDrawable");
	}

	public static ClassName getRelativeLayoutClassName() {
		return ClassName.get("android.widget", "RelativeLayout");
	}

	public static ClassName getScrollViewClassName() {
		return ClassName.get("android.widget", "ScrollView");
	}

	public static ClassName getAppCompatActivityClassName() {
		return ClassName.get("android.support.v7.app", "AppCompatActivity");
	}

	public static ClassName getIntentClassName() {
		return ClassName.get("android.content", "Intent");
	}

	public static ClassName getToastClassName() {
		return ClassName.get("android.widget", "Toast");
	}

	public static ClassName getToolbarClassname() {
		return ClassName.get("android.support.v7.widget", "Toolbar");
	}

	public static ClassName getActionbarClassname() {
		return ClassName.get("android.support.v7.widget", "Actionbar");
	}

	public static ClassName getGensonClassName() {
		return ClassName.get("com.owlike.genson", "Genson");
	}

	public static ClassName getGenericTypeClassName() {
		return ClassName.get("com.owlike.genson", "GenericType");
	}

	public static ClassName getMenuClassName() {
		return ClassName.get("android.view", "Menu");
	}

	public static ClassName getMenuInflaterClassName() {
		return ClassName.get("android.view", "MenuInflater");
	}

	public static ClassName getMenuItemClassName() {
		return ClassName.get("android.view", "MenuItem");
	}

	public static ClassName getCardViewClassName() {
		return ClassName.get("android.support.v7.widget", "CardView");
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

	public static ParameterSpec getAttributeSetParam() {
		return ParameterSpec.builder(getAttributeSetClassName(), "attributeSet").build();
	}

}
