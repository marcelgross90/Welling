package de.fhws.applab.gemara.welling.application;

import de.fhws.applab.gemara.welling.GeneratedFile;

public class SettingsGradle extends GeneratedFile {

	//todo maybe replace directory with hardcoded directory
	private final String directoryName;
	private final String libName;

	public SettingsGradle(String directoryName, String libName) {
		this.directoryName = directoryName;
		this.libName = libName;
	}

	@Override
	public void generate() {
		appendln("include: ':app', ':" + this.libName + "'");
	}

	@Override
	protected String getFileName() {
		return "settings.gradle";
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}
}
