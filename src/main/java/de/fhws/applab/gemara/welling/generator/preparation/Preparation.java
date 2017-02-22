package de.fhws.applab.gemara.welling.generator.preparation;

import de.fhws.applab.gemara.welling.generator.AppDescription;

@SuppressWarnings("WeakerAccess")
public abstract class Preparation {

	protected final AppDescription appDescription;

	public Preparation(AppDescription appDescription) {
		this.appDescription = appDescription;
	}

	public abstract void generate();
}