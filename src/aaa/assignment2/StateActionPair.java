package aaa.assignment2;

import aaa.State;

public class StateActionPair
{
	public final State state;
	public final int action;
	
	public StateActionPair(State state, int action)
	{
		this.state  = state;
		this.action = action;
	}
	
	// Automatically-generated methods by Eclipse:

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + action;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateActionPair other = (StateActionPair) obj;
		if (action != other.action)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}
