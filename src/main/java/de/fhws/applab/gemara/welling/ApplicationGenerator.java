package de.fhws.applab.gemara.welling;

import de.fhws.applab.gemara.dalston.generator.utils.VisitStatesOnlyOnce;
import de.fhws.applab.gemara.enfield.metamodel.Model;
import de.fhws.applab.gemara.enfield.metamodel.states.AbstractState;
import de.fhws.applab.gemara.enfield.metamodel.states.GetDispatcherState;
import de.fhws.applab.gemara.enfield.metamodel.transitions.AbstractTransition;
import de.fhws.applab.gemara.enfield.metamodel.transitions.ActionTransition;
import de.fhws.applab.gemara.welling.application.lib.generic.res.values.Attr;
import de.fhws.applab.gemara.welling.generator.AppDescription;
import de.fhws.applab.gemara.welling.generator.FileWriter;
import de.fhws.applab.gemara.welling.generator.PrepareGradleGenerator;
import de.fhws.applab.gemara.welling.generator.PrepareAppGenerator;
import de.fhws.applab.gemara.welling.generator.PrepareLibGenerator;
import de.fhws.applab.gemara.welling.visitors.StateVisitorImpl;

import java.util.Collection;
import java.util.LinkedList;

public class ApplicationGenerator {

	private final String startDir = "gemara/android/src-gen/generated/";
	private final AppDescription appDescription;

	private final PrepareGradleGenerator prepareGradleGenerator;
	private final PrepareLibGenerator prepareLibGenerator;
	private final PrepareAppGenerator prepareAppGenerator;

	private Model metaModel;

	public ApplicationGenerator(Model metaModel) {
		this.metaModel = metaModel;

		this.appDescription = new AppDescription(metaModel, startDir);

		prepareGradleGenerator = new PrepareGradleGenerator(appDescription);
		prepareLibGenerator = new PrepareLibGenerator(appDescription);
		prepareAppGenerator = new PrepareAppGenerator(appDescription, getNameOfStartResource());

	}

	public void generate() {

		prepareGradleGenerator.generate();
		prepareLibGenerator.generate();
		prepareAppGenerator.generate();

		iterateOverStates();
		writeDeclareStyleables();

	}

	private void writeDeclareStyleables() {
		FileWriter.writeGeneratedFiles(new Attr(appDescription.getLibResDirectory(), appDescription.getAppDeclareStyleable()));
	}

	private void iterateOverStates() {
		GetDispatcherState dispatcherState = metaModel.getDispatcherState();

		Collection<ActionTransition> allActionTransitions = getAllActionTransitionsFromState(dispatcherState);

		Collection<AbstractState> allNextStates = getAllNextStates(allActionTransitions);

		for (AbstractState state : allNextStates) {
			StateVisitorImpl visitor = new StateVisitorImpl(appDescription);
			state.generate(new VisitStatesOnlyOnce(visitor));
		}
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

	private String getNameOfStartResource() {
		//todo use right resourceName;
		return "Lecturer";
	}

}
