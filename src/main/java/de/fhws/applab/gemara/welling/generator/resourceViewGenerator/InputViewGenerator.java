package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.List;

public class InputViewGenerator extends ResourceViewGenerator<InputView> {

	public InputViewGenerator(InputView inputView, AppDescription appDescription, StateHolder stateHolder) {
		super(inputView, appDescription, stateHolder);
	}

	@Override
	protected List<AbstractModelClass> getLibJavaClasses() {
		return null;
	}

	@Override
	protected List<GeneratedFile> getLibXMLClasses() {
		return null;
	}

	@Override
	protected List<AbstractModelClass> getAppJavaClasses() {
		return null;
	}

	@Override
	protected void addStrings() {

	}

	//todo implement me
}
