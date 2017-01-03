package de.fhws.applab.gemara.welling;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.fhws.applab.gemara.welling.androidMethods.LifecycleMethods;

import javax.lang.model.element.Modifier;

import static de.fhws.applab.gemara.welling.androidConstants.AndroidSpecificClasses.*;

public class AppCompatActivityClass extends AbstractModelClass {

	public AppCompatActivityClass(String packageName, String className) {
		super(packageName, className);
	}

	public JavaFile javaFile() {

		MethodSpec onBackPressed = MethodSpec.methodBuilder("onBackPressed")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(void.class)
				.beginControlFlow("if ($N.getBackStackEntryCount() > 1)", getFragmentManagerField(Modifier.PRIVATE))
				.addStatement("super.onBackPressed()")
				.endControlFlow().beginControlFlow("else")
				.addStatement("finish()").endControlFlow()
				.build();

		MethodSpec onSupportNavigateUp = MethodSpec.methodBuilder("onSupportNavigateUp")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(Boolean.class)
				.addStatement("$N.popBackStack()", getFragmentManagerField(Modifier.PRIVATE))
				.addStatement("return false")
				.build();

		MethodSpec.Builder onCreateBuilder = LifecycleMethods.getOnCreate();
		MethodSpec onCreate = onCreateBuilder
				.addStatement("setContentView(R.layout.activity_main)")
				.addStatement("$N = getSupportFragmentManager()", getFragmentManagerField(Modifier.PRIVATE))
				.addStatement("initToolbar()")
				.beginControlFlow("$N == null", getSavedInstanceStateParam())
				.addStatement("initialNetworkRequest()")
				.endControlFlow()
				.build();


		final TypeSpec type = TypeSpec.classBuilder(this.className)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ClassName.get("android.support.v7.app", "AppCompatActivity"))
				.addField(getFragmentManagerField(Modifier.PRIVATE))
				.addMethod(onBackPressed)
				.addMethod(onSupportNavigateUp)
				.addMethod(onCreate)
				.build();

		return JavaFile.builder(this.packageName, type).build();
	}
}
