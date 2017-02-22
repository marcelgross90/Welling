package de.fhws.applab.gemara.welling;

import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleDatatype;
import de.fhws.applab.gemara.enfield.metamodel.attributes.sub.ResourceCollectionAttribute;
import de.fhws.applab.gemara.enfield.metamodel.caching.CachingByEtag;
import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;
import de.fhws.applab.gemara.enfield.metamodel.states.GetDispatcherState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.AbstractPrimaryState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.DeletePrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetImageState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetPrimaryCollectionResourceByQueryState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetPrimarySingleResourceByIdState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostImageState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PutPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ContentTransition;
import de.fhws.applab.gemara.enfield.metamodel.views.SingleResourceView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.cardView.CardView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.displayViews.detailView.DetailView;
import de.fhws.applab.gemara.enfield.metamodel.wembley.frondentSpecifics.AppSpecifics;
import de.fhws.applab.gemara.enfield.metamodel.wembley.frondentSpecifics.FrontendColor;
import de.fhws.applab.gemara.enfield.metamodel.wembley.frondentSpecifics.InputException;
import de.fhws.applab.gemara.enfield.metamodel.wembley.inputView.InputView;
import de.fhws.applab.gemara.welling.modelGenerator.CardViewModelGenerator;
import de.fhws.applab.gemara.welling.modelGenerator.DetailViewModelGenerator;
import de.fhws.applab.gemara.welling.modelGenerator.InputViewModelGenerator;

class MyEnfieldModel {

	private final Model metaModel;

	private SingleResource lecturerResource;

	private SingleResource chargeResource;

	private GetDispatcherState dispatcherState;

	private GetPrimarySingleResourceByIdState getLecturerByIdState;

	private GetPrimaryCollectionResourceByQueryState getCollectionOfLecturersState;

	private GetPrimarySingleResourceByIdState getChargeByIdState;

	private GetPrimaryCollectionResourceByQueryState getCollectionOfChargesState;

	public MyEnfieldModel() {
		this.metaModel = new Model();

		this.metaModel.setProducerName("fhws");
		this.metaModel.setPackagePrefix("de.fhws.applab.gemara");
		this.metaModel.setProjectName("Lecturer");

		this.metaModel.setAppSpecifics(getAppSpecifics());

	}

	public Model create() {
		createSingleResourceLecturer();

		createDispatcherState();

		createGetLecturerByIdState();

		createGetCollectionOfLecturersState();

		createDeleteLecturerState();

		createPostNewLecturerState();

		createUpdateLecturerState();

		createPostLecturerImageState();

		createGetLecturerImageState();

		createGetCollectionOfChargesState();

		createGetChargeByIdState();

		createPostNewChargeState();

		createUpdateChargeState();

		createDeleteChargeState();

		return this.metaModel;
	}

	private AppSpecifics getAppSpecifics() {
		AppSpecifics appSpecifics = new AppSpecifics("https://apistaging.fiw.fhws.de/mig/api/");
		appSpecifics.setFrontendColor(getFrontendColor());

		return appSpecifics;

	}

	private FrontendColor getFrontendColor() {
		try {

			return new FrontendColor("#3F51B5", "#303F9F", "#FF4081", "#fff");
		} catch (InputException ex) {

			return null;
		}
	}

	private void createSingleResourceLecturer() {
		this.metaModel.addSingleResource("Lecturer");

		this.lecturerResource = this.metaModel.getSingleResource("Lecturer");

		this.lecturerResource.setModel(this.metaModel);
		this.lecturerResource.setResourceName("Lecturer");
		this.lecturerResource.setMediaType("application/vnd.fhws-lecturer.default+json");

		final SimpleAttribute title = new SimpleAttribute("title", SimpleDatatype.STRING);
		final SimpleAttribute firstName = new SimpleAttribute("firstName", SimpleDatatype.STRING);
		final SimpleAttribute lastName = new SimpleAttribute("lastName", SimpleDatatype.STRING);
		final SimpleAttribute email = new SimpleAttribute("email", SimpleDatatype.STRING);
		final SimpleAttribute phone = new SimpleAttribute("phone", SimpleDatatype.STRING);
		final SimpleAttribute address = new SimpleAttribute("address", SimpleDatatype.STRING);
		final SimpleAttribute roomNumber = new SimpleAttribute("roomNumber", SimpleDatatype.STRING);
		final SimpleAttribute homepage = new SimpleAttribute("homepage", SimpleDatatype.LINK);

		createSingleResourceCharge();
		final ResourceCollectionAttribute charge = new ResourceCollectionAttribute("chargeUrl", this.chargeResource);

		this.lecturerResource.addAttribute(title);
		this.lecturerResource.addAttribute(firstName);
		this.lecturerResource.addAttribute(lastName);
		this.lecturerResource.addAttribute(email);
		this.lecturerResource.addAttribute(phone);
		this.lecturerResource.addAttribute(address);
		this.lecturerResource.addAttribute(roomNumber);
		this.lecturerResource.addAttribute(homepage);

		this.lecturerResource.addAttribute(charge);

		addImageAttributeForLecturerResource();

	}

	private void addImageAttributeForLecturerResource() {
		final SimpleAttribute profileImage = new SimpleAttribute("profileImageUrl", SimpleDatatype.IMAGE);
		profileImage.setBelongsToResource(this.lecturerResource);
		this.lecturerResource.addAttribute(profileImage);
		this.lecturerResource.setCaching(new CachingByEtag());
	}

	private void createSingleResourceCharge() {
		this.metaModel.addSingleResource("Charge");

		this.chargeResource = this.metaModel.getSingleResource("Charge");

		this.chargeResource.setModel(this.metaModel);
		this.chargeResource.setResourceName("Charge");
		this.chargeResource.setMediaType("application/vnd.fhws-charge.default+json");

		final SimpleAttribute id = new SimpleAttribute("id", SimpleDatatype.INT);
		final SimpleAttribute titleOfCharge = new SimpleAttribute("title", SimpleDatatype.STRING);
		final SimpleAttribute fromDate = new SimpleAttribute("fromDate", SimpleDatatype.DATE);
		final SimpleAttribute toDate = new SimpleAttribute("toDate", SimpleDatatype.DATE);

		this.chargeResource.addAttribute(id);
		this.chargeResource.addAttribute(titleOfCharge);
		this.chargeResource.addAttribute(fromDate);
		this.chargeResource.addAttribute(toDate);
	}

	private void createDispatcherState() {
		final GetDispatcherState dispatcherState = new GetDispatcherState();
		dispatcherState.setName("Dispatcher");
		dispatcherState.setModel(this.metaModel);
		this.metaModel.setDispatcherState(dispatcherState);
		this.dispatcherState = dispatcherState;
	}

	private void createGetLecturerByIdState() {
		final GetPrimarySingleResourceByIdState getLecturerByIdState = new GetPrimarySingleResourceByIdState();
		getLecturerByIdState.setName("GetOneLecturer");
		getLecturerByIdState.setResourceType(this.lecturerResource);
		getLecturerByIdState.setModel(this.metaModel);
		this.lecturerResource.setDefaultStateForSelfUri(getLecturerByIdState);

		//        final StateEntryConstraint stateEntryConstraint = new StateEntryConstraint( );
		//        stateEntryConstraint.setExpression( "${user.firstName.equals(model.firstName)}" );
		//        stateEntryConstraint.setName( "DemoConstraintToCheckEqualNames" );
		//        stateEntryConstraint.setModel( this.metaModel);
		//getSingleUserPrimarySingleResource.addStateEntryConstraint( stateEntryConstraint );

		this.metaModel.addState(getLecturerByIdState.getName(), getLecturerByIdState);

		this.getLecturerByIdState = getLecturerByIdState;

		addLecturerDetailView();
	}

	private void addLecturerDetailView() {
		final DetailView lecturerDetailView = DetailViewModelGenerator.lecturer();

		final SingleResourceView resourceView = new SingleResourceView();

		resourceView.setDetailView(lecturerDetailView);

		this.getLecturerByIdState.setSingleResourceView(resourceView);
	}

	private void createGetCollectionOfLecturersState() {
		final GetPrimaryCollectionResourceByQueryState getAllLecturersCollectionState = new GetPrimaryCollectionResourceByQueryState();
		getAllLecturersCollectionState.setName("GetAllLecturers");
		getAllLecturersCollectionState.setModel(this.metaModel);
		getAllLecturersCollectionState.setResourceType(this.lecturerResource);

		this.dispatcherState.addTransition(new ActionTransition(getAllLecturersCollectionState, "getAllLecturers"));
		getAllLecturersCollectionState.addTransition(this.getLecturerByIdState);

		this.metaModel.addState(getAllLecturersCollectionState.getName(), getAllLecturersCollectionState);

		this.getCollectionOfLecturersState = getAllLecturersCollectionState;

		addLecturerCardView();
	}

	private void addLecturerCardView() {
		final CardView lecturerCardView = CardViewModelGenerator.lecturer();

		final SingleResourceView resourceView = new SingleResourceView();

		resourceView.setCardView(lecturerCardView);

		this.getCollectionOfLecturersState.setSingleResourceView(resourceView);
	}

	private void createDeleteLecturerState() {
		final DeletePrimaryResourceState deleteLecturerState = new DeletePrimaryResourceState();
		deleteLecturerState.setName("DeleteOneLecturer");
		deleteLecturerState.setResourceType(this.lecturerResource);
		deleteLecturerState.setModel(this.metaModel);
		deleteLecturerState.addTransition(this.getCollectionOfLecturersState, "getAllLecturers");

		this.getLecturerByIdState.addTransition(deleteLecturerState, "deleteLecturer");

		this.metaModel.addState(deleteLecturerState.getName(), deleteLecturerState);
	}

	private void createPostNewLecturerState() {
		final PostPrimaryResourceState postNewLecturerState = new PostPrimaryResourceState();
		postNewLecturerState.setName("CreateOneLecturer");
		postNewLecturerState.setResourceType(this.lecturerResource);
		postNewLecturerState.setModel(this.metaModel);
		this.getCollectionOfLecturersState.addTransition(new ActionTransition(postNewLecturerState, "createNewLecturer"));

		this.metaModel.addState(postNewLecturerState.getName(), postNewLecturerState);

		addLecturerInputView(postNewLecturerState);
	}

	private void addLecturerInputView(AbstractPrimaryState state) {
		final InputView lecturerInputView = InputViewModelGenerator.lecturer();

		final SingleResourceView resourceView = new SingleResourceView();

		resourceView.setInputView(lecturerInputView);

		state.setSingleResourceView(resourceView);
	}

	private void createUpdateLecturerState() {
		final PutPrimaryResourceState updateLecturerState = new PutPrimaryResourceState();
		updateLecturerState.setName("UpdateLecturer");
		updateLecturerState.setResourceType(this.lecturerResource);
		updateLecturerState.setModel(this.metaModel);

		this.getLecturerByIdState.addTransition(new ActionTransition(updateLecturerState, "updateLecturer"));

		this.metaModel.addState(updateLecturerState.getName(), updateLecturerState);

		addLecturerInputView(updateLecturerState);
	}

	private void createPostLecturerImageState() {
		final PostImageState postProfileImageState = new PostImageState();
		postProfileImageState.setName("PostProfileImage");
		postProfileImageState.setResourceType(this.lecturerResource);
		postProfileImageState.setModel(this.metaModel);
		postProfileImageState.setImageAttribute((SimpleAttribute) this.lecturerResource.getAttributeByName("profileImageUrl"));

		this.getLecturerByIdState.addTransition(postProfileImageState, "postImage");
		postProfileImageState.setName("PostProfileImage");
		postProfileImageState.setResourceType(this.lecturerResource);
		postProfileImageState.setModel(this.metaModel);
		postProfileImageState.setImageAttribute((SimpleAttribute) this.lecturerResource.getAttributeByName("profileImageUrl"));

		this.metaModel.addState(postProfileImageState.getName(), postProfileImageState);
	}

	private void createGetLecturerImageState() {
		final GetImageState getProfileImageState = new GetImageState();
		getProfileImageState.setName("GetProfileImage");
		getProfileImageState.setResourceType(this.lecturerResource);
		getProfileImageState.setModel(this.metaModel);
		getProfileImageState.setImageAttribute((SimpleAttribute) this.lecturerResource.getAttributeByName("profileImageUrl"));

		this.getLecturerByIdState.addTransition(getProfileImageState, "getProfileImage");

		this.metaModel.addState(getProfileImageState.getName(), getProfileImageState);
	}

	private void createGetCollectionOfChargesState() {
		final GetPrimaryCollectionResourceByQueryState getCollectionOfChargesState = new GetPrimaryCollectionResourceByQueryState();
		getCollectionOfChargesState.setName("GetAllChargesOfLecturer");
		getCollectionOfChargesState.setModel(this.metaModel);
		getCollectionOfChargesState.setResourceType(this.chargeResource);

		this.getLecturerByIdState.addTransition(new ContentTransition(getCollectionOfChargesState));

		this.metaModel.addState(getCollectionOfChargesState.getName(), getCollectionOfChargesState);

		this.getCollectionOfChargesState = getCollectionOfChargesState;

		addChargeCardView();
	}

	private void addChargeCardView() {
		final CardView chargeCardView = CardViewModelGenerator.charges();

		final SingleResourceView resourceView = new SingleResourceView();

		resourceView.setCardView(chargeCardView);

		this.getCollectionOfChargesState.setSingleResourceView(resourceView);
	}

	private void createGetChargeByIdState() {
		final GetPrimarySingleResourceByIdState getChargeByIdState = new GetPrimarySingleResourceByIdState();
		getChargeByIdState.setName("GetOneChargeOfLecturer");
		getChargeByIdState.setResourceType(this.chargeResource);
		getChargeByIdState.setModel(this.metaModel);
		this.chargeResource.setDefaultStateForSelfUri(getChargeByIdState);

		this.getCollectionOfChargesState.addTransition(getChargeByIdState);

		this.metaModel.addState(getChargeByIdState.getName(), getChargeByIdState);

		this.getChargeByIdState = getChargeByIdState;

		addChargeDetailView();
	}

	private void addChargeDetailView() {
		final DetailView chargeDetailView = DetailViewModelGenerator.charge();

		final SingleResourceView resourceView = new SingleResourceView();

		resourceView.setDetailView(chargeDetailView);

		this.getChargeByIdState.setSingleResourceView(resourceView);
	}

	private void createPostNewChargeState() {
		final PostPrimaryResourceState createSingleChargePrimaryState = new PostPrimaryResourceState();
		createSingleChargePrimaryState.setName("CreateOneCharge");
		createSingleChargePrimaryState.setResourceType(this.chargeResource);
		createSingleChargePrimaryState.setModel(this.metaModel);
		this.getCollectionOfChargesState.addTransition(new ActionTransition(createSingleChargePrimaryState, "createChargeOfLecturer"));

		this.metaModel.addState(createSingleChargePrimaryState.getName(), createSingleChargePrimaryState);

		addChargeInputView(createSingleChargePrimaryState);
	}

	private void addChargeInputView(AbstractPrimaryState state) {
		final InputView chargeInputView = InputViewModelGenerator.charge();

		final SingleResourceView resourceView = new SingleResourceView();

		resourceView.setInputView(chargeInputView);

		state.setSingleResourceView(resourceView);
	}

	private void createUpdateChargeState() {
		final PutPrimaryResourceState editSingleChargePrimaryState = new PutPrimaryResourceState();
		editSingleChargePrimaryState.setName("UpdateOneChargeOfLecturer");
		editSingleChargePrimaryState.setResourceType(this.chargeResource);
		editSingleChargePrimaryState.setModel(this.metaModel);

		this.getChargeByIdState.addTransition(new ActionTransition(editSingleChargePrimaryState, "updateCharge"));

		this.metaModel.addState(editSingleChargePrimaryState.getName(), editSingleChargePrimaryState);

		addChargeInputView(editSingleChargePrimaryState);
	}

	private void createDeleteChargeState() {
		final DeletePrimaryResourceState deleteSingleChargePrimaryState = new DeletePrimaryResourceState();
		deleteSingleChargePrimaryState.setName("DeleteOneChargeOfLecturer");
		deleteSingleChargePrimaryState.setResourceType(this.chargeResource);
		deleteSingleChargePrimaryState.setModel(this.metaModel);

		deleteSingleChargePrimaryState.addTransition(this.getCollectionOfChargesState, "getAllCharges");
		this.getChargeByIdState.addTransition(deleteSingleChargePrimaryState, "deleteCharge");

		this.metaModel.addState(deleteSingleChargePrimaryState.getName(), deleteSingleChargePrimaryState);
	}
}