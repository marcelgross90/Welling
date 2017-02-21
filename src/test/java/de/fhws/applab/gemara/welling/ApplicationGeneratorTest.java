package de.fhws.applab.gemara.welling;

import org.junit.Test;

public class ApplicationGeneratorTest {

	@Test
	public void generatorTest() {
		MyEnfieldModel enfieldModel = new MyEnfieldModel();
		ApplicationGenerator generator = new ApplicationGenerator(enfieldModel.create());

		generator.generate();
	}
}
