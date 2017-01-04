package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.welling.AndroidApplicationGenerator;
import de.fhws.applab.gemara.welling.metaModel.InputException;

public class Main {

	public static void main(String[] args) throws InputException {
		AndroidApplicationGenerator generator = new AndroidApplicationGenerator(MetaModelGenerator.generateMetaModel());
		generator.generate();
	}

}
