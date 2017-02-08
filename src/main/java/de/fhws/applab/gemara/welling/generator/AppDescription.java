package de.fhws.applab.gemara.welling.generator;

import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.welling.metaModel.AppDeclareStyleable;

import java.util.List;

public class AppDescription {

	private String appName;
	private String appPackageName;
	private String appResDirectory;
	private String appJavaDirectory;
	private String appManifestDirectory;

	private String libName;
	private String libPackageName;
	private String libResDirectory;
	private String libJavaDirectory;
	private String libManifestDirectory;

	private AppDeclareStyleable appDeclareStyleable;

	public AppDescription(Model metaModel, String startDir) {
		this.appName = metaModel.getProjectName();
		this.appPackageName = metaModel.getPackagePrefix();
		this.appManifestDirectory = "/generated/app/src/main";
		this.appResDirectory = appManifestDirectory + "/res";
		this.appJavaDirectory = startDir + "/app/src/main/java/";

		this.libName = metaModel.getProjectName().toLowerCase() + "_lib";
		this.libPackageName = appPackageName + "." + libName;
		this.libManifestDirectory = "/generated/" + libName + "/src/main";
		this.libResDirectory = libManifestDirectory + "/res";
		this.libJavaDirectory = startDir + libName + "/src/main/java/";

		this.appDeclareStyleable = new AppDeclareStyleable();
	}

	public String getAppName() {
		return appName;
	}

	public String getAppPackageName() {
		return appPackageName;
	}

	public String getAppResDirectory() {
		return appResDirectory;
	}

	public String getAppJavaDirectory() {
		return appJavaDirectory;
	}

	public String getAppManifestDirectory() {
		return appManifestDirectory;
	}

	public String getLibName() {
		return libName;
	}

	public String getLibPackageName() {
		return libPackageName;
	}

	public String getLibResDirectory() {
		return libResDirectory;
	}

	public String getLibJavaDirectory() {
		return libJavaDirectory;
	}

	public String getLibManifestDirectory() {
		return libManifestDirectory;
	}

	public AppDeclareStyleable getAppDeclareStyleable() {
		return appDeclareStyleable;
	}

	public void setDeclareStyleables(List<AppDeclareStyleable.DeclareStyleable> declareStyleables) {
		this.appDeclareStyleable.setDeclareStyleables(declareStyleables);
	}

	public void setDeclareStyleables(AppDeclareStyleable.DeclareStyleable declareStyleable) {
		this.appDeclareStyleable.setDeclareStyleables(declareStyleable);
	}
}
