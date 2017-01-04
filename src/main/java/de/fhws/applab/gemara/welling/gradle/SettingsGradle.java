package de.fhws.applab.gemara.welling.gradle;

import de.fhws.applab.gemara.welling.GeneratedFile;

public class SettingsGradle extends GeneratedFile {

	@SuppressWarnings("FieldCanBeLocal")
	private final String directoryName = "generated/";
	private final String libName;

	public SettingsGradle(String libName) {
		this.libName = libName;
	}

	@Override
	public void generate() {
		appendln("include ':app', ':" + this.libName + "'");
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
