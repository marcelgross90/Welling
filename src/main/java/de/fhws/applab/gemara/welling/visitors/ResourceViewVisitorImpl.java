package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.wembley.ResourceViewVisitor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.resourceViewGenerator.CardViewGenerator;

public class ResourceViewVisitorImpl implements ResourceViewVisitor {

	private final AppDescription appDescription;

	public ResourceViewVisitorImpl(AppDescription appDescription) {
		this.appDescription = appDescription;
	}

	@Override
	public void visit(InputView inputView) {

	}

	@Override
	public void visit(CardView cardView) {
		CardViewGenerator cardViewGenerator = new CardViewGenerator(cardView, appDescription);
		cardViewGenerator.generate();


	}

	@Override
	public void visit(DetailView detailView) {

	}
}
