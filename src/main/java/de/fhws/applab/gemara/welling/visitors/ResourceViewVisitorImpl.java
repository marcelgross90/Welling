package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ResourceViewVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.StateHolder;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.resourceViewGenerator.CardViewGenerator;

public class ResourceViewVisitorImpl implements ResourceViewVisitor {

	private final AppDescription appDescription;
	private final StateHolder stateHolder;

	public ResourceViewVisitorImpl(AppDescription appDescription, StateHolder stateHolder) {
		this.appDescription = appDescription;
		this.stateHolder = stateHolder;
	}

	@Override
	public void visit(InputView inputView) {

	}

	@Override
	public void visit(CardView cardView) {
		CardViewGenerator cardViewGenerator = new CardViewGenerator(cardView, appDescription, stateHolder);
		cardViewGenerator.generate();


	}

	@Override
	public void visit(DetailView detailView) {

	}
}
