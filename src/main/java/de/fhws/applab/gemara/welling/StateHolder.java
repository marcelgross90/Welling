package de.fhws.applab.gemara.welling;

import java.util.ArrayList;
import java.util.List;

public class StateHolder {

	public enum StateType {
		GET_SINGLE, GET_COLLECTION, POST, PUT, DELETE, DISPATCHER, POST_IMAGE, GET_IMAGE
	}

	private String relType;
	private final List<StateType> nextStates = new ArrayList<>();

	public StateHolder() {
	}

	public String getRelType() {
		return relType;
	}

	public List<StateType> getNextStates() {
		return nextStates;
	}

	public void setNextStates(StateType nextState, String relType) {
		this.nextStates.add(nextState);
		this.relType = relType;
	}
}
