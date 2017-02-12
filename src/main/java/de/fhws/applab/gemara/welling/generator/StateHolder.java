package de.fhws.applab.gemara.welling.generator;

import java.util.ArrayList;
import java.util.List;

public class StateHolder {

	public enum StateType {
		GET_SINGLE, GET_COLLECTION, POST, PUT, DELETE, DISPATCHER, POST_IMAGE, GET_IMAGE
	}

	public class State {
		private StateType stateType;
		private String relType;

		public State(StateType stateType, String relType) {
			this.stateType = stateType;
			this.relType = relType;
		}

		public StateType getStateType() {
			return stateType;
		}

		public String getRelType() {
			return relType;
		}
	}

	private String relType;
	private final List<State> nextStates = new ArrayList<>();

	public StateHolder() {
	}

	public String getRelType() {
		return relType;
	}

	public boolean contains(StateType stateType) {
		for (State nextState : nextStates) {
			if (nextState.getStateType() == stateType) {
				return true;
			}
		}
		return false;
	}

	public String getRelType(StateType stateType) {
		for (State nextState : nextStates) {
			if (nextState.getStateType() == stateType) {
				return nextState.getRelType();
			}
		}
		return "";
	}

	public void setNextStates(StateType nextState, String relType) {
		this.nextStates.add(new State(nextState, relType));
		this.relType = relType;
	}
}
