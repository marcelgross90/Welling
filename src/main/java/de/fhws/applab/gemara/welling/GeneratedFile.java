package de.fhws.applab.gemara.welling;

/**
 * Delegates all methods to the dalston GeneratedFile.
 * Except getModuleName, so we can use "android" as package.
 */
public abstract class GeneratedFile extends de.fhws.applab.gemara.dalston.generator.GeneratedFile {

	@Override
	protected String getModuleName() {
		return "android";
	}
}
