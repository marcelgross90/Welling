package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.welling.AndroidApplicationGenerator;
import de.fhws.applab.gemara.welling.metaModel.AndroidMetaModel;
import de.fhws.applab.gemara.welling.metaModel.InputException;

public class Main {

	public static void main(String[] args) throws InputException {
		AndroidApplicationGenerator generator = new AndroidApplicationGenerator(MetaModelGenerator.generateMetaModel());
		generator.generate();

		/*EnfieldMetaModel enfieldMetaModel = new EnfieldMetaModel();
		Model model = enfieldMetaModel.create();
		model.toString();*/

	}

}
