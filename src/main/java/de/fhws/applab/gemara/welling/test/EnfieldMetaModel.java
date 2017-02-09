package de.fhws.applab.gemara.welling.test;

import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleAttribute;
import de.fhws.applab.gemara.enfield.metamodel.attributes.simple.SimpleDatatype;
import de.fhws.applab.gemara.enfield.metamodel.caching.CachingByEtag;
import de.fhws.applab.gemara.enfield.metamodel.resources.SingleResource;
import de.fhws.applab.gemara.enfield.metamodel.states.GetDispatcherState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.DeletePrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetImageState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetPrimaryCollectionResourceByQueryState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.GetPrimarySingleResourceByIdState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostImageState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PutPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;
import de.fhws.applab.gemara.enfield.metamodel.views.SingleResourceView;
import de.fhws.applab.gemara.welling.test.modelGenerator.CardViewModelGenerator;

public class EnfieldMetaModel {

	public Model create() {
		Model metaModel = new Model( );

		metaModel.setProducerName("fhws");
		metaModel.setPackagePrefix("de.fhws.applab.gemara");
		metaModel.setProjectName("Lecturer");

		//Lecturer
		metaModel.addSingleResource( "lecturer" );

		SingleResource singleResourceLecturer = metaModel.getSingleResource( "lecturer" );

		singleResourceLecturer.setModel( metaModel );
		singleResourceLecturer.setResourceName( "Lecturer" );
		singleResourceLecturer.setMediaType( "application/vnd.fhws-lecturer.default+json" );

		final SimpleAttribute title = new SimpleAttribute( "title", SimpleDatatype.STRING );
		final SimpleAttribute firstName = new SimpleAttribute( "firstName", SimpleDatatype.STRING );
		final SimpleAttribute lastName = new SimpleAttribute( "lastName", SimpleDatatype.STRING );
		final SimpleAttribute email = new SimpleAttribute( "email", SimpleDatatype.STRING );
		final SimpleAttribute phone = new SimpleAttribute( "phone", SimpleDatatype.STRING );
		final SimpleAttribute address = new SimpleAttribute( "address", SimpleDatatype.STRING );
		final SimpleAttribute roomNumber = new SimpleAttribute( "roomNumber", SimpleDatatype.STRING );
		final SimpleAttribute homepage = new SimpleAttribute( "homepage", SimpleDatatype.LINK );
		final SimpleAttribute profileImg = new SimpleAttribute("profileImageUrl", SimpleDatatype.IMAGE);

		singleResourceLecturer.addAttribute( title );
		singleResourceLecturer.addAttribute( firstName );
		singleResourceLecturer.addAttribute( lastName );
		singleResourceLecturer.addAttribute( email );
		singleResourceLecturer.addAttribute( phone );
		singleResourceLecturer.addAttribute( address );
		singleResourceLecturer.addAttribute( roomNumber );
		singleResourceLecturer.addAttribute( homepage );
		singleResourceLecturer.addAttribute(profileImg);

/*		//Charge
		metaModel.addSingleResource( "charge" );

		SingleResource singleResourceCharge = metaModel.getSingleResource( "charge" );

		singleResourceCharge.setModel( metaModel );
		singleResourceCharge.setResourceName( "charge" );
		singleResourceCharge.setMediaType( "application/vnd.fhws-charge.default+json" );

		final SimpleAttribute titleOfCharge = new SimpleAttribute( "title", SimpleDatatype.STRING );
		final SimpleAttribute fromDate = new SimpleAttribute( "fromDate", SimpleDatatype.DATE );
		final SimpleAttribute toDate = new SimpleAttribute( "toDate", SimpleDatatype.DATE );

		singleResourceCharge.addAttribute( titleOfCharge );
		singleResourceCharge.addAttribute( fromDate );
		singleResourceCharge.addAttribute( toDate );*/

		//Dispatcher State
		final GetDispatcherState dispatcherState = new GetDispatcherState( );
		dispatcherState.setName( "Dispatcher" );
		dispatcherState.setModel( metaModel );
		metaModel.setDispatcherState( dispatcherState );

		//---Lecturer States


		//Lecturer By Id
		final GetPrimarySingleResourceByIdState getSingleLecturerPrimarySingleResource =
				new GetPrimarySingleResourceByIdState( );
		getSingleLecturerPrimarySingleResource.setName( "GetOneLecturer" );
		getSingleLecturerPrimarySingleResource.setResourceType( singleResourceLecturer );
		getSingleLecturerPrimarySingleResource.setModel( metaModel );
		singleResourceLecturer.setDefaultStateForSelfUri( getSingleLecturerPrimarySingleResource );

		//        final StateEntryConstraint stateEntryConstraint = new StateEntryConstraint( );
		//        stateEntryConstraint.setExpression( "${user.firstName.equals(model.firstName)}" );
		//        stateEntryConstraint.setName( "DemoConstraintToCheckEqualNames" );
		//        stateEntryConstraint.setModel( metaModel);
		//getSingleUserPrimarySingleResource.addStateEntryConstraint( stateEntryConstraint );

		metaModel.addState( getSingleLecturerPrimarySingleResource.getName( ), getSingleLecturerPrimarySingleResource );

		//Lecturer Collection

		SingleResourceView lecturerSingleResourceView = new SingleResourceView();
		lecturerSingleResourceView.setResource(singleResourceLecturer);
		lecturerSingleResourceView.setCardView(CardViewModelGenerator.lecturer());

		final GetPrimaryCollectionResourceByQueryState getAllLecturersCollectionState =
				new GetPrimaryCollectionResourceByQueryState( );
		getAllLecturersCollectionState.setName( "GetAllLecturers" );
		getAllLecturersCollectionState.setModel( metaModel );
		getAllLecturersCollectionState.setResourceType( singleResourceLecturer );
		getAllLecturersCollectionState.setSingleResourceView(lecturerSingleResourceView);

		dispatcherState.addTransition( new ActionTransition( getAllLecturersCollectionState, "getAllLecturers" ) );
		//getAllLecturersCollectionState.addTransition( getSingleLecturerPrimarySingleResource );

		metaModel.addState( getAllLecturersCollectionState.getName( ), getAllLecturersCollectionState );


		//Lecturer Delete
		final DeletePrimaryResourceState deleteSingleLecturerPrimaryState = new DeletePrimaryResourceState( );
		deleteSingleLecturerPrimaryState.setName( "DeleteOneLecturer" );
		deleteSingleLecturerPrimaryState.setResourceType( singleResourceLecturer );
		deleteSingleLecturerPrimaryState.setModel( metaModel );
		deleteSingleLecturerPrimaryState.addTransition( getAllLecturersCollectionState, "getAllLecturers" );

		getSingleLecturerPrimarySingleResource.addTransition( deleteSingleLecturerPrimaryState, "deleteLecturer" );

		metaModel.addState( deleteSingleLecturerPrimaryState.getName( ), deleteSingleLecturerPrimaryState );

		//Lecturer Create
		final PostPrimaryResourceState createSingleLecturerPrimaryState = new PostPrimaryResourceState( );
		createSingleLecturerPrimaryState.setName( "CreateOneLecturer" );
		createSingleLecturerPrimaryState.setResourceType( singleResourceLecturer );
		createSingleLecturerPrimaryState.setModel( metaModel );
		getAllLecturersCollectionState
				.addTransition( new ActionTransition( createSingleLecturerPrimaryState, "createNewLecturer" ) );

		metaModel.addState( createSingleLecturerPrimaryState.getName( ), createSingleLecturerPrimaryState );


		//Lecturer Update
		final PutPrimaryResourceState editSingleLecturerPrimaryState = new PutPrimaryResourceState( );
		editSingleLecturerPrimaryState.setName( "UpdateLecturer" );
		editSingleLecturerPrimaryState.setResourceType( singleResourceLecturer );
		editSingleLecturerPrimaryState.setModel( metaModel );

		getSingleLecturerPrimarySingleResource
				.addTransition( new ActionTransition( editSingleLecturerPrimaryState, "updateLecturer" ) );

		metaModel.addState( editSingleLecturerPrimaryState.getName( ), editSingleLecturerPrimaryState );


		//Lecturer Image
		final SimpleAttribute profileImage = new SimpleAttribute( "profileImage", SimpleDatatype.IMAGE );
		profileImage.setBelongsToResource( singleResourceLecturer );
		singleResourceLecturer.addAttribute( profileImage );
		singleResourceLecturer.setCaching( new CachingByEtag( ) );
		//Hier nochmal setModel?


		//Lecturer Image Post
		final PostImageState postProfileImageState = new PostImageState( );
		postProfileImageState.setName( "PostProfileImage" );
		postProfileImageState.setResourceType( singleResourceLecturer );
		postProfileImageState.setModel( metaModel );
		postProfileImageState.setImageAttribute( profileImage );

		getSingleLecturerPrimarySingleResource.addTransition( postProfileImageState, "postImage" );
		postProfileImageState.setName( "PostProfileImage" );
		postProfileImageState.setResourceType( singleResourceLecturer );
		postProfileImageState.setModel( metaModel );
		postProfileImageState.setImageAttribute( profileImage );

		metaModel.addState( postProfileImageState.getName( ), postProfileImageState );


		//Lecturer Image Get
		final GetImageState getProfileImageState = new GetImageState( );
		getProfileImageState.setName( "GetProfileImage" );
		getProfileImageState.setResourceType( singleResourceLecturer );
		getProfileImageState.setModel( metaModel );
		getProfileImageState.setImageAttribute( profileImage );

		getSingleLecturerPrimarySingleResource.addTransition( getProfileImageState, "getProfileImage" );

		metaModel.addState( getProfileImageState.getName( ), getProfileImageState );

/*
		//Charge als LinkedResource hinzufügen
		final LinkedResourceAttribute linkToCharges = new LinkedResourceAttribute( "chargeUrl", singleResourceLecturer );
		singleResourceLecturer.addAttribute( linkToCharges );
		linkToCharges.setModel( metaModel);


		//Charge Collection
		final GetPrimaryCollectionResourceByQueryState getAllChargesCollectionState =
				new GetPrimaryCollectionResourceByQueryState( );
		getAllChargesCollectionState.setName( "GetAllChargesOfLecturer" );
		getAllChargesCollectionState.setModel( metaModel );
		getAllChargesCollectionState.setResourceType( singleResourceCharge);

		getSingleLecturerPrimarySingleResource.addTransition( new ContentTransition(getAllChargesCollectionState ) );

		metaModel.addState( getAllChargesCollectionState.getName( ), getAllChargesCollectionState );


		//Charge By ID
		final GetPrimarySingleResourceByIdState getSingleChargePrimarySingleResource =
				new GetPrimarySingleResourceByIdState( );
		getSingleChargePrimarySingleResource.setName( "GetOneChargeOfLecturer" );
		getSingleChargePrimarySingleResource.setResourceType( singleResourceCharge );
		getSingleChargePrimarySingleResource.setModel( metaModel );
		singleResourceCharge.setDefaultStateForSelfUri( getSingleChargePrimarySingleResource );

		getAllChargesCollectionState.addTransition( getSingleChargePrimarySingleResource );

		metaModel.addState( getSingleChargePrimarySingleResource.getName( ), getSingleChargePrimarySingleResource );


		//Charge Post
		final PostPrimaryResourceState createSingleChargePrimaryState = new PostPrimaryResourceState( );
		createSingleChargePrimaryState.setName( "CreateOneCharge" );
		createSingleChargePrimaryState.setResourceType( singleResourceCharge );
		createSingleChargePrimaryState.setModel( metaModel );
		getAllChargesCollectionState
				.addTransition( new ActionTransition( createSingleChargePrimaryState, "createChargeOfLecturer" ) );

		metaModel.addState( createSingleChargePrimaryState.getName( ), createSingleChargePrimaryState );


		//Charge Put
		final PutPrimaryResourceState editSingleChargePrimaryState = new PutPrimaryResourceState( );
		editSingleChargePrimaryState.setName( "UpdateOneChargeOfLecturer" );
		editSingleChargePrimaryState.setResourceType( singleResourceCharge );
		editSingleChargePrimaryState.setModel( metaModel );

		getSingleChargePrimarySingleResource
				.addTransition( new ActionTransition( editSingleChargePrimaryState, "updateCharge" ) );

		metaModel.addState( editSingleChargePrimaryState.getName( ), editSingleChargePrimaryState );


		//Charge Delete
		final DeletePrimaryResourceState deleteSingleChargePrimaryState = new DeletePrimaryResourceState( );
		deleteSingleChargePrimaryState.setName( "DeleteOneChargeOfLecturer" );
		deleteSingleChargePrimaryState.setResourceType( singleResourceCharge);
		deleteSingleChargePrimaryState.setModel( metaModel );

		deleteSingleChargePrimaryState.addTransition( getAllChargesCollectionState, "getAllCharges" );
		getSingleChargePrimarySingleResource.addTransition( deleteSingleChargePrimaryState, "deleteCharge" );

		metaModel.addState( deleteSingleChargePrimaryState.getName( ), deleteSingleChargePrimaryState );
*/
		return metaModel;
	}
}
