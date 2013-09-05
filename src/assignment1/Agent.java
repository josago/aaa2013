package assignment1;

public interface Agent
{
	public static final int TYPE_PREY       = 0;
	public static final int TYPE_PREDATOR   = 1;
	
	public static final int ACTION_STAY  = 0;
	public static final int ACTION_NORTH = -1;
	public static final int ACTION_SOUTH = 1;
	public static final int ACTION_EAST  = 2;
	public static final int ACTION_WEST  = -2;
	
	public int    getType();
	public String getSymbol();
	
	public float pi(State env, int action);
}
