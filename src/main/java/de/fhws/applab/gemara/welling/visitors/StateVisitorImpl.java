package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;
import de.fhws.applab.gemara.enfield.metamodel.states.AbstractState;
import de.fhws.applab.gemara.enfield.metamodel.states.GetDispatcherState;
import de.fhws.applab.gemara.enfield.metamodel.states.IStateVisitor;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.DeletePrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetImageState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetPrimaryCollectionResourceByQueryState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetPrimarySingleResourceByIdState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostImageState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PutPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.secondary.DeleteSecondaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.secondary.GetSecondaryCollectionResourceByQueryState;
import de.fhws.applab.gemara.enfield.metamodel.states.secondary.GetSecondarySingleResourceByIdState;
import de.fhws.applab.gemara.enfield.metamodel.states.secondary.PostSecondaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.secondary.PutSecondaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.AbstractTransition;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;
import de.fhws.applab.gemara.enfield.metamodel.views.SingleResourceView;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.ResourceGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.StateHolder;
import de.fhws.applab.gemara.welling.generator.resourceViewGenerator.DeleteGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class StateVisitorImpl implements IStateVisitor {

	private final AppDescription appDescription;

	public StateVisitorImpl(AppDescription appDescription) {
		this.appDescription = appDescription;
	}

	public void visit(GetPrimarySingleResourceByIdState getPrimarySingleResourceByIdState) {
		SingleResource resource = getPrimarySingleResourceByIdState.getResourceType();
		FileWriter.writeJavaFiles(new ResourceGenerator(appDescription, resource), appDescription.getLibJavaDirectory());

		StateHolder stateHolder = getStateHolder(getPrimarySingleResourceByIdState,
				getTransitionFromState(getPrimarySingleResourceByIdState));

		SingleResourceView resourceView = getPrimarySingleResourceByIdState.getSingleResourceView();
		resourceView.getDetailView()
				.accept(new ResourceViewVisitorImpl(appDescription, stateHolder, ResourceViewVisitorImpl.InputType.NONE));
	}

	public void visit(GetPrimaryCollectionResourceByQueryState getPrimaryCollectionResourceByQueryState) {
		SingleResource resource = getPrimaryCollectionResourceByQueryState.getResourceType();
		FileWriter.writeJavaFiles(new ResourceGenerator(appDescription, resource), appDescription.getLibJavaDirectory());

		StateHolder stateHolder = getStateHolder(getPrimaryCollectionResourceByQueryState,
				getTransitionFromState(getPrimaryCollectionResourceByQueryState));

		SingleResourceView resourceView = getPrimaryCollectionResourceByQueryState.getSingleResourceView();
		resourceView.getCardView().accept(new ResourceViewVisitorImpl(appDescription, stateHolder, ResourceViewVisitorImpl.InputType.NONE));
	}

	public void visit(PostPrimaryResourceState postPrimaryResourceState) {
		SingleResource resource = postPrimaryResourceState.getResourceType();
		FileWriter.writeJavaFiles(new ResourceGenerator(appDescription, resource), appDescription.getLibJavaDirectory());

		StateHolder stateHolder = getStateHolder(postPrimaryResourceState, getTransitionFromState(postPrimaryResourceState));

		SingleResourceView resourceView = postPrimaryResourceState.getSingleResourceView();
		resourceView.getInputView()
				.accept(new ResourceViewVisitorImpl(appDescription, stateHolder, ResourceViewVisitorImpl.InputType.POST));
	}

	public void visit(PutPrimaryResourceState putPrimaryResourceState) {
		SingleResource resource = putPrimaryResourceState.getResourceType();
		FileWriter.writeJavaFiles(new ResourceGenerator(appDescription, resource), appDescription.getLibJavaDirectory());

		StateHolder stateHolder = getStateHolder(putPrimaryResourceState, getTransitionFromState(putPrimaryResourceState));

		SingleResourceView resourceView = putPrimaryResourceState.getSingleResourceView();
		resourceView.getInputView().accept(new ResourceViewVisitorImpl(appDescription, stateHolder, ResourceViewVisitorImpl.InputType.PUT));
	}

	public void visit(DeletePrimaryResourceState deletePrimaryResourceState) {
		new DeleteGenerator(appDescription).generate();
	}

	public void visit(GetSecondarySingleResourceByIdState getSecondarySingleResourceByIdState) {

	}

	public void visit(GetSecondaryCollectionResourceByQueryState getSecondaryCollectionResourceByQueryState) {

	}

	public void visit(PostSecondaryResourceState postSecondaryResourceState) {

	}

	public void visit(PutSecondaryResourceState putSecondaryResourceState) {

	}

	public void visit(DeleteSecondaryResourceState deleteSecondaryResourceState) {

	}

	public void visit(GetDispatcherState getDispatcherState) {

	}

	public void visit(PostImageState postImageState) {

	}

	public void visit(GetImageState getImageState) {

	}

	private List<AbstractTransition> getTransitionFromState(AbstractState state) {
		return state.getTransitions().stream().collect(Collectors.toList());
	}

	private StateHolder getStateHolder(AbstractState state, List<AbstractTransition> abstractTransitions) {
		StateHolder stateHolder = new StateHolder();

		for (AbstractTransition abstractTransition : abstractTransitions) {
			AbstractState currentState = abstractTransition.getNextState();

			StateIdentifierVisitor stateIdentifierVisitor1 = new StateIdentifierVisitor();
			currentState.generate(stateIdentifierVisitor1);

			if (abstractTransition instanceof ActionTransition) {
				String relType = ((ActionTransition) abstractTransition).getRelationType();

				generateRestApi(stateIdentifierVisitor1.getStateType() + "_" + state.getResourceType().getResourceName(), relType);
			}
			stateHolder.setNextStates(stateIdentifierVisitor1.getStateType());
		}

		return stateHolder;
	}

	private void generateRestApi(String stateKey, String relType) {
		appDescription.setRestApi(stateKey, "rel_type_" + relType.toLowerCase(), relType);
	}
}