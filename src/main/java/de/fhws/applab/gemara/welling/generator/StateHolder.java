package de.fhws.applab.gemara.welling.generator;

import java.util.ArrayList;
import java.util.List;

public class StateHolder {

	public enum StateType {
		GET_SINGLE, GET_COLLECTION, POST, PUT, DELETE, DISPATCHER, POST_IMAGE, GET_IMAGE
	}

	public class State {
		private StateType stateType;

		public State(StateType stateType) {
			this.stateType = stateType;
		}

		public StateType getStateType() {
			return stateType;
		}
	}

	private final List<State> nextStates = new ArrayList<>();

	public StateHolder() {
	}

	public boolean contains(StateType stateType) {
		for (State nextState : nextStates) {
			if (nextState.getStateType() == stateType) {
				return true;
			}
		}
		return false;
	}

	public void setNextStates(StateType nextState) {
		this.nextStates.add(new State(nextState));
	}
}
