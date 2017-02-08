package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.welling.ApplicationGenerator;
import de.fhws.applab.gemara.welling.metaModel.InputException;

public class Main {

	public static void main(String[] args) throws InputException {
	/*	AndroidApplicationGenerator generator = new AndroidApplicationGenerator(MetaModelGenerator.generateMetaModel());
		generator.generate();*/

		EnfieldMetaModel enfieldMetaModel = new EnfieldMetaModel();
		ApplicationGenerator generator = new ApplicationGenerator(enfieldMetaModel.create());

		generator.generate();

	}


}
