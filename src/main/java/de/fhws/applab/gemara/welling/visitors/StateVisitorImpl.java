package de.fhws.applab.gemara.welling.visitors;

import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;
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
import de.fhws.applab.gemara.enfield.metamodel.views.SingleResourceView;
import de.fhws.applab.gemara.welling.application.lib.specific.java.model.ResourceGenerator;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;

public class StateVisitorImpl implements IStateVisitor {

	private final AppDescription appDescription;

	public StateVisitorImpl(AppDescription appDescription) {
		this.appDescription = appDescription;
	}

	public void visit(GetPrimarySingleResourceByIdState getPrimarySingleResourceByIdState) {
		SingleResource resource = getPrimarySingleResourceByIdState.getResourceType();

	}

	public void visit(GetPrimaryCollectionResourceByQueryState getPrimaryCollectionResourceByQueryState) {
		SingleResource resource = getPrimaryCollectionResourceByQueryState.getResourceType();
		FileWriter.writeJavaFiles(new ResourceGenerator(appDescription, resource), appDescription.getLibJavaDirectory());

		SingleResourceView resourceView = getPrimaryCollectionResourceByQueryState.getSingleResourceView();
		resourceView.getCardView().accept(new ResourceViewVisitorImpl(appDescription));


	}

	public void visit(PostPrimaryResourceState postPrimaryResourceState) {
		SingleResource resource = postPrimaryResourceState.getResourceType();
	}

	public void visit(PutPrimaryResourceState putPrimaryResourceState) {
		SingleResource resource = putPrimaryResourceState.getResourceType();

	}

	public void visit(DeletePrimaryResourceState deletePrimaryResourceState) {

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
}