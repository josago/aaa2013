package assignment1;

public interface Environment
{
	public static final int ENVIRONMENT_SIZE = 11;
	
	public static final int AGENT_MOVE_STAY  = 0;
	public static final int AGENT_MOVE_NORTH = -1;
	public static final int AGENT_MOVE_SOUTH = 1;
	public static final int AGENT_MOVE_EAST  = 2;
	public static final int AGENT_MOVE_WEST  = -2;
	
	public int getPredatorX();
	public int getPredatorY();
	public int getPreyX();
	public int getPreyY();
	
	public void movePredator(int mov);
	public void movePrey(int mov);
}
