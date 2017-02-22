package de.fhws.applab.gemara.welling.generator.abstractGenerator;

import com.squareup.javapoet.JavaFile;

public abstract class AbstractModelClass {

	protected String packageName;
	protected String className;

	protected AbstractModelClass() {}

	protected AbstractModelClass(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
	}

	public final void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	public final void setClassName(final String className) {
		this.className = className;
	}

	public final String getPackageName() {
		return this.packageName;
	}

	public final String getClassName() {
		return this.className;
	}

	public abstract JavaFile javaFile();
}