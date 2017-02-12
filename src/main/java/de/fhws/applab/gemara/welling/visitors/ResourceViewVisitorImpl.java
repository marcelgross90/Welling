package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ResourceViewVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.resourceViewGenerator.CardViewGenerator;
import de.fhws.applab.gemara.welling.generator.resourceViewGenerator.DetailViewGenerator;
import de.fhws.applab.gemara.welling.generator.resourceViewGenerator.InputViewGenerator;

public class ResourceViewVisitorImpl implements ResourceViewVisitor {

	public enum InputType {
		POST, PUT, NONE
	}

	private final AppDescription appDescription;
	private final StateHolder stateHolder;
	private final InputType inputType;

	public ResourceViewVisitorImpl(AppDescription appDescription, StateHolder stateHolder, InputType inputType) {
		this.appDescription = appDescription;
		this.stateHolder = stateHolder;
		this.inputType = inputType;
	}

	@Override
	public void visit(InputView inputView) {
		InputViewGenerator inputViewsGenerator = new InputViewGenerator(inputView, appDescription, stateHolder);
		inputViewsGenerator.generate();
	}

	@Override
	public void visit(CardView cardView) {
		CardViewGenerator cardViewGenerator = new CardViewGenerator(cardView, appDescription, stateHolder);
		cardViewGenerator.generate();


	}

	@Override
	public void visit(DetailView detailView) {
		DetailViewGenerator detailViewGenerator = new DetailViewGenerator(detailView, appDescription, stateHolder);
		detailViewGenerator.generate();

	}
}
