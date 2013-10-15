package aaa.assignment3;

import aaa.State;

public class StateActionOpponent
{
	public final State state;
	public final int action, opponent;
	
	public StateActionOpponent(State state, int action, int opponent)
	{
		this.state    = state;
		this.action   = action;
		this.opponent = opponent;
	}

	// Automatically-generated methods by Eclipse:
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + action;
		result = prime * result + opponent;
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
		StateActionOpponent other = (StateActionOpponent) obj;
		if (action != other.action)
			return false;
		if (opponent != other.opponent)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}
