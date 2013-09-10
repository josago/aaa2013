package assignment1;

import java.util.Iterator;

public interface State
{
	public static final int ENVIRONMENT_SIZE = 11;
	
	public static final int AGENT_MOVE_STAY  = 0;
	public static final int AGENT_MOVE_NORTH = -1;
	public static final int AGENT_MOVE_SOUTH = 1;
	public static final int AGENT_MOVE_EAST  = 2;
	public static final int AGENT_MOVE_WEST  = -2;
	
	static final int[] AGENT_ACTIONS = {0, -1, 1, 2, -2};
	
	public Iterator<State> stateIterator();
	
	public boolean isFinal();
	
	public int getX(Agent agent);
	public int getY(Agent agent);
	
	public void move(Agent agent);
	public void move(Agent agent, int action);

	public Object clone();
}
