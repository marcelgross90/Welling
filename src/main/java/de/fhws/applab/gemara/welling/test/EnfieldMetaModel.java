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
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PostPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.states.primary.PutPrimaryResourceState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;

public class EnfieldMetaModel {

	private Model metaModel;

	public Model create() {
		metaModel = new Model();

		metaModel.setProducerName("fhws");
		metaModel.setPackagePrefix("de.fhws.applab.gemara");
		metaModel.setProjectName("lecturer");

		addLecturerResource(metaModel);
		addStates(metaModel);

		return metaModel;
	}

	private void addLecturerResource(Model metaModel) {
		metaModel.addSingleResource("Lecturer");

		SingleResource singleResourceLecturer = metaModel.getSingleResource( "Lecturer" );

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
		final SimpleAttribute profileImage = new SimpleAttribute( "profileImage", SimpleDatatype.IMAGE );

		profileImage.setBelongsToResource( singleResourceLecturer );

		singleResourceLecturer.addAttribute( title );
		singleResourceLecturer.addAttribute( firstName );
		singleResourceLecturer.addAttribute( lastName );
		singleResourceLecturer.addAttribute( email );
		singleResourceLecturer.addAttribute( phone );
		singleResourceLecturer.addAttribute( address );
		singleResourceLecturer.addAttribute( roomNumber );
		singleResourceLecturer.addAttribute( homepage );
		singleResourceLecturer.addAttribute( profileImage );

		singleResourceLecturer.setCaching( new CachingByEtag( ) );
	}

	private void addStates(Model metaModel) {
		SingleResource singleResourceLecturer = metaModel.getSingleResource("Lecturer");

		final GetDispatcherState dispatcherState = new GetDispatcherState( );
		dispatcherState.setName( "Dispatcher" );
		dispatcherState.setModel( metaModel );
		metaModel.setDispatcherState( dispatcherState );

		//Lecturer By Id
		final GetPrimarySingleResourceByIdState getSingleLecturerPrimarySingleResource =
				new GetPrimarySingleResourceByIdState( );
		getSingleLecturerPrimarySingleResource.setName( "GetOneLecturer" );
		getSingleLecturerPrimarySingleResource.setResourceType( singleResourceLecturer );
		getSingleLecturerPrimarySingleResource.setModel( metaModel );
		singleResourceLecturer.setDefaultStateForSelfUri( getSingleLecturerPrimarySingleResource );

		metaModel.addState( getSingleLecturerPrimarySingleResource.getName( ), getSingleLecturerPrimarySingleResource );

		//Lecturer Collection
		final GetPrimaryCollectionResourceByQueryState getAllLecturersCollectionState =
				new GetPrimaryCollectionResourceByQueryState( );
		getAllLecturersCollectionState.setName( "GetAllLecturers" );
		getAllLecturersCollectionState.setModel( metaModel );
		getAllLecturersCollectionState.setResourceType( singleResourceLecturer );

		dispatcherState.addTransition( new ActionTransition( getAllLecturersCollectionState, "getAllLecturers" ) );
		getAllLecturersCollectionState.addTransition( getSingleLecturerPrimarySingleResource );

		metaModel.addState( getAllLecturersCollectionState.getName( ), getAllLecturersCollectionState );

		//Lecturer Delete
		final DeletePrimaryResourceState deleteSingleLecturerPrimaryState = new DeletePrimaryResourceState( );
		deleteSingleLecturerPrimaryState.setName( "DeleteOneLecturer" );
		deleteSingleLecturerPrimaryState.setResourceType( singleResourceLecturer );
		deleteSingleLecturerPrimaryState.setModel( metaModel );
		deleteSingleLecturerPrimaryState.addTransition( getAllLecturersCollectionState, "getAllLecturers" );

		getSingleLecturerPrimarySingleResource.addTransition( deleteSingleLecturerPrimaryState, "deleteOneLecturer" );

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



		//Lecturer Image Get
		final GetImageState getProfileImageState = new GetImageState( );
		getProfileImageState.setName( "GetProfileImage" );
		getProfileImageState.setResourceType( singleResourceLecturer );
		getProfileImageState.setModel( metaModel );
		getProfileImageState.setImageAttribute( (SimpleAttribute)singleResourceLecturer.getAttributeByName("profileImage") );

		getSingleLecturerPrimarySingleResource.addTransition( getProfileImageState, "getProfileImage" );

		metaModel.addState( getProfileImageState.getName( ), getProfileImageState );
	}

}
/*
RestApi.create( )
			   .producer( "fhws" )
			   .projectName( "lecturer" )
			   .contextPath( CONTEXT_PATH )
			   .generateInPackageWithPrefix( "de.fhws.fiw.opendata.lecturer.api" )
			   .noApiKey()
			   .usingDatabase( "fiwlecturer" )
			   		.atHost( "localhost" )
			   		.usingUsername( "braunp" ).andPassword( "willi" )
			   		.done()
			   .resources( "Lecturer", "Charge" )
					.defineResourceWithName( "Lecturer" )
						.withSimpleAttribute( "firstName" ).asString( )
						.withSimpleAttribute( "lastName" ).asString( )
						.withSimpleAttribute( "address" ).asString( )
						.withSimpleAttribute( "email" ).asString( )
						.withSimpleAttribute( "phone" ).asString( )
						.withSimpleAttribute( "roomNumber" ).asString( )
						.withSimpleAttribute( "title" ).asString( )
						.withSimpleAttribute( "profileImage" ).asImage()
			   			.withSimpleAttribute( "homepage" ).asExternalLink()
			   			.withSubResourceNamed( "charge" ).toResource( "Charge" ).withDefaultView()
			   			.allAttributesDefined()
						.andCachingByValidatingEtags()
			   		.defineResourceWithName( "Charge" )
			   			.withSimpleAttribute( "title" ).asString()
			   			.withSimpleAttribute( "fromDate" ).asDate()
			   			.withSimpleAttribute( "toDate" ).asDate()
			   			.allAttributesDefined()
			   			.andCachingByValidatingEtags()
			   		.allResourcesDefined()
			   .states()
			   		.dispatcherState()
			   			.named( "Dispatcher" ).done()
					.primaryGetSingle()
			   			.named( "GetOneLecturer" )
			   			.withResource( "Lecturer" )
			   			.done()
			   		.primaryGetCollection()
			   			.named( "GetAllLecturers" )
			   			.withResource( "Lecturer" )
			   			.withQuery()
			   				.name( "SelectByName" )
			   				.path( "" )
			   				.orderBy( "lastName" ).and( "lastName" ).ascending( )
			   				.queryTemplate( "{model.lastName} like {query.q.concat('%')}" )
			   				.queryParameters()
			   					.named( "q" ).withDefaultValue( "%" ).asString()
			   				.doneWithQuery()
			   			.offsetSize()
			   				.offsetParameterIsNamed( "offset" ).withDefaultValue( 0 )
			   				.sizeParameterIsNamed( "size" ).withDefaultValue( 10 )
					.primaryPost().named( "CreateOneLecturer" )
						.withResource( "Lecturer" )
						.done()
					.primaryPut().named( "UpdateLecturer" )
						.withResource( "Lecturer" )
						.done()
					.primaryDelete().named( "DeleteOneLecturer" )
						.withResource( "Lecturer" )
						.done()
					.imagePost().named( "PostProfileImage" )
						.withResource( "Lecturer" )
						.withImageAttribute( "profileImage" )
						.done()
					.imageGet().named( "GetProfileImage" )
						.withResource( "Lecturer" )
						.withImageAttribute( "profileImage" )
						.done()
			   		.primaryGetSingle()
			   			.named( "GetOneCharge" )
			   			.withResource( "Charge" )
			   			.done()
			   		.secondaryGetSingle()
			   			.asSubOfPrimaryState( "GetAllLecturers" )
			   			.usingAttributeOfPrimaryResource( "charge" )
			   			.named( "GetOneChargeOfLecturer" )
			   			.withResource( "Charge" )
			   			.done()
			   		.secondaryGetCollection()
			   			.asSubOfPrimaryState( "GetAllLecturers" )
			   			.usingAttributeOfPrimaryResource( "charge" )
			   			.named( "GetAllChargesOfLecturer" )
			   			.withResource( "Charge" )
			   			.withQuery()
			   				.name( "FilterAllChargesOfLecturer" )
			   				.path( "" )
			   				.queryTemplate( "{model.title} like {query.t.concat('%')}" )
			   				.queryParameters().named( "t" ).withDefaultValue( "%" ).asString().doneWithQuery()
						.offsetSize()
			   				.offsetParameterIsNamed( "offset" ).withDefaultValue( 0 )
			   				.sizeParameterIsNamed( "size" ).withDefaultValue( 10 )
			   		.secondaryPost()
			   			.asSubOfPrimaryState( "GetAllLecturers" )
			   			.usingAttributeOfPrimaryResource( "charge" )
			   			.named( "CreateOneCharge" )
			   			.withResource( "Charge" )
			   			.done()
			   		.secondaryPut()
			   			.asSubOfPrimaryState( "GetAllLecturers" )
			   			.usingAttributeOfPrimaryResource( "charge" )
			   			.named( "UpdateOneChargeOfLecturer" )
			   			.withResource( "Charge" )
			   			.done()
			   		.secondaryDelete()
			   			.asSubOfPrimaryState( "GetAllLecturers" )
			   			.usingAttributeOfPrimaryResource( "charge" )
			   			.named( "DeleteOneChargeOfLecturer" )
			   			.withResource( "Charge" )
			   			.done()
					.allStatesDefined()
			   .transitions()
					.fromState( "Dispatcher" )
			   			.asHeaderLink().toState( "GetAllLecturers" )
			   			.usingRelationType( "getAllLecturers" )
			   		.fromState( "GetAllLecturers" )
			   			.asContentLink().toState( "GetOneLecturer")
			   		.fromState( "GetAllLecturers" )
			   			.asHeaderLink().toState( "CreateOneLecturer" )
			   			.usingRelationType( "createNewLecturer" )
			   		.fromState( "GetOneLecturer" )
			   			.asHeaderLink().toState( "UpdateLecturer" )
			   			.usingRelationType( "updateLecturer" )
			   		.fromState( "GetOneLecturer" )
			   			.asHeaderLink().toState( "DeleteOneLecturer" )
			   			.usingRelationType( "deleteLecturer" )
			   		.fromState( "GetOneLecturer" )
			   			.asHeaderLink().toState( "PostProfileImage" )
			   			.usingRelationType( "postImage" )
			   		.fromState( "GetOneLecturer" )
			   			.asHeaderLink().toState( "CreateOneCharge" )
			   			.usingRelationType( "createChargeOfLecturer" )
			   		.fromState( "GetOneLecturer" )
			   			.asContentLink().toState( "GetProfileImage" )
			   		.fromState( "GetOneLecturer" )
			   			.asContentLink().toState( "GetAllChargesOfLecturer" )
			   		.fromState( "DeleteOneLecturer" )
			   			.asHeaderLink().toState( "GetAllLecturers" )
			   			.usingRelationType( "getAllLecturers" )
			   		.fromState( "GetAllChargesOfLecturer" )
			   			.asHeaderLink().toState( "GetOneChargeOfLecturer" ).usingRelationType( "getOneChargeOfLecturer" )
			   		.fromState( "GetAllChargesOfLecturer" )
			   			.asContentLink().toState( "GetOneCharge" )
			   		.fromState( "GetOneChargeOfLecturer" )
			   			.asHeaderLink().toState( "UpdateOneChargeOfLecturer" ).usingRelationType( "updateCharge" )
			   		.fromState( "GetOneChargeOfLecturer" )
			   			.asHeaderLink().toState( "DeleteOneChargeOfLecturer" ).usingRelationType( "deleteCharge" )
			   		.fromState( "GetAllChargesOfLecturer" )
			   			.asHeaderLink().toState( "CreateOneCharge" ).usingRelationType( "createChargeOfLecturer" )
			   		.fromState( "DeleteOneChargeOfLecturer" )
			   			.asHeaderLink().toState( "GetAllChargesOfLecturer" ).usingRelationType( "getAllCharges" )
			   		.fromState( "CreateOneCharge" )
			   			.asHeaderLink().toState( "GetAllChargesOfLecturer" ).usingRelationType( "getAllCharges" )
			   		.fromState( "UpdateOneChargeOfLecturer" )
			   			.asHeaderLink().toState( "GetOneChargeOfLecturer" ).usingRelationType( "getOneChargeOfLecturer" )
			   		.allTransitionsDefined()
			   .noAuthentication( )
			   .generate();
 */