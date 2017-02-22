package de.fhws.applab.gemara.welling.visitors;

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
import de.fhws.applab.gemara.welling.generator.StateHolder;

public class StateIdentifierVisitor implements IStateVisitor {

	private StateHolder.StateType stateType;

	public StateHolder.StateType getStateType() {
		return stateType;
	}

	@Override
	public void visit(GetPrimarySingleResourceByIdState getPrimarySingleResourceByIdState) {
		this.stateType = StateHolder.StateType.GET_SINGLE;

	}

	@Override
	public void visit(GetPrimaryCollectionResourceByQueryState getPrimaryCollectionResourceByQueryState) {
		this.stateType = StateHolder.StateType.GET_COLLECTION;

	}

	@Override
	public void visit(PostPrimaryResourceState postPrimaryResourceState) {
		this.stateType = StateHolder.StateType.POST;

	}

	@Override
	public void visit(PutPrimaryResourceState putPrimaryResourceState) {
		this.stateType = StateHolder.StateType.PUT;

	}

	@Override
	public void visit(DeletePrimaryResourceState deletePrimaryResourceState) {
		this.stateType = StateHolder.StateType.DELETE;
	}

	@Override
	public void visit(GetSecondarySingleResourceByIdState getSecondarySingleResourceByIdState) {
		this.stateType = StateHolder.StateType.GET_SINGLE;
	}

	@Override
	public void visit(GetSecondaryCollectionResourceByQueryState getSecondaryCollectionResourceByQueryState) {
		this.stateType = StateHolder.StateType.GET_COLLECTION;
	}

	@Override
	public void visit(PostSecondaryResourceState postSecondaryResourceState) {
		this.stateType = StateHolder.StateType.POST;
	}

	@Override
	public void visit(PutSecondaryResourceState putSecondaryResourceState) {
		this.stateType = StateHolder.StateType.PUT;
	}

	@Override
	public void visit(DeleteSecondaryResourceState deleteSecondaryResourceState) {
		this.stateType = StateHolder.StateType.DELETE;
	}

	@Override
	public void visit(GetDispatcherState getDispatcherState) {
		this.stateType = StateHolder.StateType.DISPATCHER;
	}

	@Override
	public void visit(PostImageState postImageState) {
		this.stateType = StateHolder.StateType.POST_IMAGE;
	}

	@Override
	public void visit(GetImageState getImageState) {
		this.stateType = StateHolder.StateType.GET_IMAGE;
	}
}