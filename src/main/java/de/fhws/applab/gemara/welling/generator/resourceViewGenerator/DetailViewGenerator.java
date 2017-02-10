package de.fhws.applab.gemara.welling.generator.resourceViewGenerator;

import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.AbstractModelClass;
import de.fhws.applab.gemara.welling.generator.abstractGenerator.GeneratedFile;

import java.util.List;

public class DetailViewGenerator extends ResourceViewGenerator<DetailView> {


	public DetailViewGenerator(DetailView detailView, AppDescription appDescription, StateHolder stateHolder) {
		super(detailView, appDescription, stateHolder);
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
