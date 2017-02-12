package de.fhws.applab.gemara.welling;

import de.fhws.applab.gemara.dalston.generator.utils.VisitStatesOnlyOnce;
import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.enfield.metamodel.states.AbstractState;
import de.fhws.applab.gemara.enfield.metamodel.states.GetDispatcherState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;
import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Attr;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Colors;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.RestApi;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Strings;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.preparation.PrepareGradleGenerator;
import de.fhws.applab.gemara.welling.generator.preparation.PrepareAppGenerator;
import de.fhws.applab.gemara.welling.generator.preparation.PrepareLibGenerator;
import de.fhws.applab.gemara.welling.metaModel.AppColor;
import de.fhws.applab.gemara.welling.metaModel.InputException;
import de.fhws.applab.gemara.welling.visitors.StateVisitorImpl;

import java.util.Collection;
import java.util.LinkedList;

public class ApplicationGenerator {

	//todo add baseUrl to metamodel
	private final String baseUrl = "https://apistaging.fiw.fhws.de/mig/api/";
	private final String startDir = "gemara/android/src-gen/generated/";
	private final AppDescription appDescription;

	private final PrepareGradleGenerator prepareGradleGenerator;
	private final PrepareLibGenerator prepareLibGenerator;
	private final PrepareAppGenerator prepareAppGenerator;

	private Model metaModel;

	public ApplicationGenerator(Model metaModel) {
		this.metaModel = metaModel;

		this.appDescription = new AppDescription(metaModel, startDir, baseUrl);

		prepareLibGenerator = new PrepareLibGenerator(appDescription);
		prepareAppGenerator = new PrepareAppGenerator(appDescription, getNameOfStartResource(metaModel.getDispatcherState()));
		prepareGradleGenerator = new PrepareGradleGenerator(appDescription);

	}

	public void generate() {

		prepareLibGenerator.generate();
		prepareAppGenerator.generate();
		prepareGradleGenerator.generate();

		iterateOverStates();

		writeDeclareStyleables();
		writeRestApi();
//todo add colors to metamodel
		writeColors();
		writeAppManifest();
		writeStrings();
	}

	private void writeStrings() {
		FileWriter.writeGeneratedFiles(new Strings(appDescription.getAppResDirectory(), appDescription.getAppString()));
		FileWriter.writeGeneratedFiles(new Strings(appDescription.getLibResDirectory(), appDescription.getLibString()));
	}

	private void writeAppManifest() {
		FileWriter.writeGeneratedFiles(new ManifestGenerator(appDescription.getAppManifestDirectory(), appDescription.getAppManifest()));
	}

	private void writeDeclareStyleables() {
		FileWriter.writeGeneratedFiles(new Attr(appDescription.getLibResDirectory(), appDescription.getAppDeclareStyleable()));
	}

	private void writeRestApi() {
		FileWriter.writeGeneratedFiles(new RestApi(appDescription.getLibResDirectory(), appDescription.getAppRestAPI()));
	}

	private void writeColors() {
		try {
			AppColor app = new AppColor("#3F51B5", "#303F9F", "#FF4081", "#fff", "#8080FF");
			AppColor lib = new AppColor("#3F51B5", "#303F9F", "#FF4081", "#fff", "#8080FF");
			Colors appColor = new Colors(appDescription.getAppResDirectory(), app);
			Colors libColor = new Colors(appDescription.getLibResDirectory(), lib);

			FileWriter.writeGeneratedFiles(appColor);
			FileWriter.writeGeneratedFiles(libColor);
		} catch (InputException ex) {
			//do nothing
		}
	}

	private void iterateOverStates() {
		GetDispatcherState dispatcherState = metaModel.getDispatcherState();

		getFirstRelType(dispatcherState);


		dispatcherState.generate(new VisitStatesOnlyOnce(new StateVisitorImpl(appDescription)));
	}



	private void getFirstRelType(GetDispatcherState dispatcherState) {
		for (ActionTransition actionTransition : getAllActionTransitionsFromState(dispatcherState)) {
			appDescription.setRestApi("entry_relType", "rel_type_first_state", actionTransition.getRelationType());
		}
	}

	private Collection<ActionTransition> getAllActionTransitionsFromState(AbstractState state) {
		Collection<ActionTransition> allActionTransitions = new LinkedList<>();

		state.getTransitions().stream().filter(transition -> transition instanceof ActionTransition).forEach(transition -> {
			ActionTransition actionTransition = (ActionTransition) transition;
			allActionTransitions.add(actionTransition);
		});

		return allActionTransitions;
	}



	private String getNameOfStartResource(GetDispatcherState dispatcherState) {
		for (ActionTransition actionTransition : getAllActionTransitionsFromState(dispatcherState)) {
			return actionTransition.getNextState().getResourceType().getResourceName();
		}
		return "";
	}

}
