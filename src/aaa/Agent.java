package aaa;

/**
 * This interface contains the basic methods all agents (both predators and preys) must implement.
 * @author josago
 */
public interface Agent
{
	public static final int TYPE_PREY       = 0;
	public static final int TYPE_PREDATOR   = 1;
	
	public static final int ACTION_STAY  = 0;
	public static final int ACTION_NORTH = -1;
	public static final int ACTION_SOUTH = 1;
	public static final int ACTION_EAST  = 2;
	public static final int ACTION_WEST  = -2;
	
	/**
	 * Returns either TYPE_PREY or TYPE_PREDATOR.
	 * @return Either TYPE_PREY or TYPE_PREDATOR.
	 */
	public int    getType();
	
	/**
	 * Returns the UTF-8 symbol used to represent this agent on the board.
	 * @return The UTF-8 symbol used to represent this agent on the board.
	 */
	public String getSymbol();
	
	/**
	 * Given a state and an action (from ACTION_*), this method returns the probability of taking it.
	 * @param env A state.
	 * @param action An action.
	 * @return The probability (from 0 to 1, both inclusive) of taking the given action in the given state.
	 */
	public float pi(State env, int action);
}
