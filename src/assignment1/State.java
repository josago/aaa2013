package assignment1;

import java.util.Iterator;

/**
 * This interface contains the basic methods all state representations must implement.
 * @author josago
 */
public interface State
{
	public static final int ENVIRONMENT_SIZE = 11; // Size of the world.
	
	public static final int AGENT_MOVE_STAY  = 0;
	public static final int AGENT_MOVE_NORTH = -1;
	public static final int AGENT_MOVE_SOUTH = 1;
	public static final int AGENT_MOVE_EAST  = 2;
	public static final int AGENT_MOVE_WEST  = -2;
	
	static final int[] AGENT_ACTIONS = {0, -1, 1, 2, -2};
	
	/**
	 * Returns an iterator which will loop through each and every possible state of this representation only once.
	 * @return A state iterator
	 */
	public Iterator<State> stateIterator();
	
	/**
	 * Returns true if the state is final (predator and prey are on the same square), false otherwise.
	 * @return True if the state is final, false otherwise.
	 */
	public boolean isFinal();
	
	/**
	 * Returns the X coordinate of an agent.
	 * @param agent An agent.
	 * @return The X coordinate of the specified agent.
	 */
	public int getX(Agent agent);
	
	/**
	 * Returns the Y coordinate of an agent.
	 * @param agent An agent.
	 * @return The Y coordinate of the specified agent.
	 */
	public int getY(Agent agent);
	
	/**
	 * Executes the policy of an agent and then moves it.
	 * @param agent The agent to move.
	 */
	public void move(Agent agent);
	
	/**
	 * Moves an agent in the specified direction (from AGENT_MOVE_*), even if the movement is not possible by its policy.
	 * @param agent The agent to move.
	 * @param action The action to take (from AGENT_MOVE_*).
	 */
	public void move(Agent agent, int action);

	/**
	 * Clones this object.
	 * @return A clone of this object.
	 */
	public Object clone();
}
