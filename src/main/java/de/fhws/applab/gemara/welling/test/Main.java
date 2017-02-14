package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.welling.ApplicationGenerator;
import de.fhws.applab.gemara.welling.metaModelExtension.InputException;

public class Main {

	public static void main(String[] args) throws InputException {
		MyEnfieldModel enfieldMetaModel = new MyEnfieldModel();
		ApplicationGenerator generator = new ApplicationGenerator(enfieldMetaModel.create());

		generator.generate();

	}


}
