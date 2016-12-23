package de.fhws.applab.gemara.welling.application.lib.generic.res.values;

import de.fhws.applab.gemara.welling.GeneratedFile;

public abstract class ValueGenerator extends GeneratedFile {

	public abstract void generateBody();

	private final String fileName;
	private final String directoryName;

	public ValueGenerator(String fileName, String directoryName) {
		this.fileName = fileName + ".xml";
		this.directoryName = directoryName + "/values";
	}

	@Override
	public void generate() {
		appendln("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		appendln("<resources>");
		generateBody();
		appendln("</resources>");
	}

	@Override
	protected String getFileName() {
		return this.fileName;
	}

	@Override
	protected String getDirectoryName() {
		return this.directoryName;
	}
}
