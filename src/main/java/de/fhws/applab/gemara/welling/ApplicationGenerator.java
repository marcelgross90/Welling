package de.fhws.applab.gemara.welling;

import de.fhws.applab.gemara.dalston.generator.utils.VisitStatesOnlyOnce;
import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.enfield.metamodel.states.AbstractState;
import de.fhws.applab.gemara.enfield.metamodel.states.GetDispatcherState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.AbstractTransition;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;
import de.fhws.applab.gemara.welling.application.lib.generic.ManifestGenerator;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Attr;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Colors;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.RestApi;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Strings;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.PrepareGradleGenerator;
import de.fhws.applab.gemara.welling.generator.PrepareAppGenerator;
import de.fhws.applab.gemara.welling.generator.PrepareLibGenerator;
import de.fhws.applab.gemara.welling.metaModel.AppColor;
import de.fhws.applab.gemara.welling.metaModel.InputException;
import de.fhws.applab.gemara.welling.visitors.StateIdentifierVisitor;
import de.fhws.applab.gemara.welling.visitors.StateVisitorImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		prepareAppGenerator = new PrepareAppGenerator(appDescription, getNameOfStartResource());
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

		Collection<ActionTransition> allActionTransitions = getAllActionTransitionsFromState(dispatcherState);

		Collection<AbstractState> allNextStates = getAllNextStates(allActionTransitions);

		for (AbstractState state : allNextStates) {

			StateIdentifierVisitor stateIdentifierVisitor = new StateIdentifierVisitor();
			state.generate(stateIdentifierVisitor);
			StateHolder stateHolder = null;
			if (stateIdentifierVisitor.getStateType() != StateHolder.StateType.DISPATCHER) {
				stateHolder = getStateHolder(getTransitionFromState(state));
			}

			StateVisitorImpl visitor = new StateVisitorImpl(appDescription, stateHolder);
			state.generate(new VisitStatesOnlyOnce(visitor));
		}
	}

	private List<AbstractTransition> getTransitionFromState(AbstractState state) {
		return state.getTransitions().stream().collect(Collectors.toList());
	}

	private StateHolder getStateHolder(List<AbstractTransition> abstractTransitions) {
		StateHolder stateHolder = new StateHolder();

		for (AbstractTransition abstractTransition : abstractTransitions) {
			AbstractState currentState = abstractTransition.getNextState();

			StateIdentifierVisitor stateIdentifierVisitor1 = new StateIdentifierVisitor();
			currentState.generate(stateIdentifierVisitor1);

			String relType = "";
			if (abstractTransition instanceof ActionTransition) {
				relType = ((ActionTransition) abstractTransition).getRelationType();
				generateRestApi(relType);
			}
			stateHolder.setNextStates(stateIdentifierVisitor1.getStateType(), relType);
		}

		return stateHolder;
	}

	private Collection<ActionTransition> getAllActionTransitionsFromState(AbstractState state) {
		Collection<ActionTransition> allActionTransitions = new LinkedList<>();

		for (AbstractTransition transition : state.getTransitions()) {
			if (transition instanceof ActionTransition) {
				ActionTransition actionTransition = (ActionTransition) transition;
				allActionTransitions.add(actionTransition);
			}
		}

		return allActionTransitions;
	}

	private Collection<AbstractState> getAllNextStates(Collection<ActionTransition> transitions) {
		Collection<AbstractState> allNextStates = new LinkedList<>();

		for (ActionTransition transition : transitions) {
			allNextStates.add(transition.getNextState());
		}

		return allNextStates;
	}

	private void getFirstRelType(GetDispatcherState dispatcherState) {
		for (ActionTransition actionTransition : getAllActionTransitionsFromState(dispatcherState)) {
			appDescription.setRestApi("rel_type_first_state", actionTransition.getRelationType());
		}
	}

	private void generateRestApi(Collection<ActionTransition> allTransitions) {
		Map<String, String> api = new HashMap<>();

		for (ActionTransition allTransition : allTransitions) {
			api.put("rel_type_" + allTransition.getRelationType().toLowerCase(), allTransition.getRelationType());
		}
		appDescription.setRestApi(api);
	}

	private void generateRestApi(String relType) {

		appDescription.setRestApi("rel_type_" + relType.toLowerCase(), relType);
	}

	private String getNameOfStartResource() {
		//todo use right resourceName;
		return "Lecturer";
	}

}
