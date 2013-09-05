package assignment1;

public class StateSimple implements State
{	
	private final int[] x, y;
	
	public StateSimple(int predatorX, int predatorY, int preyX, int preyY)
	{
		x = new int[2];
		y = new int[2];
		
		x[Agent.TYPE_PREY]     = preyX;
		x[Agent.TYPE_PREDATOR] = predatorX;

		y[Agent.TYPE_PREY]     = preyY;
		y[Agent.TYPE_PREDATOR] = predatorY;
	}
	
	public static int fixCoord(int c)
	{
		return (c + ENVIRONMENT_SIZE) % ENVIRONMENT_SIZE;
	}
	
	@Override
	public boolean isFinal()
	{
		return x[Agent.TYPE_PREY] == x[Agent.TYPE_PREDATOR]
			&& y[Agent.TYPE_PREY] == y[Agent.TYPE_PREDATOR];
	}

	@Override
	public int getX(Agent agent)
	{
		return x[agent.getType()];
	}
	
	@Override
	public int getY(Agent agent)
	{
		return y[agent.getType()];
	}

	@Override
	public void move(Agent agent)
	{
		float sum    = 0;
		float random = (float) Math.random();
		
		for (int action: AGENT_ACTIONS)
		{
			sum += agent.pi(this, action);
			
			if (sum <= random)
			{
				move(agent, action);
			}
		}
	}
	
	@Override
	public void move(Agent agent, int action)
	{
		int type = agent.getType();
		
		if (action % 2 == 0)
		{
			x[type] = fixCoord(x[type] + action / 2);
		}
		else
		{
			y[type] = fixCoord(y[type] + action);
		}
	}
	
	@Override
	public Object clone()
	{
		return new StateSimple(x[Agent.TYPE_PREDATOR], y[Agent.TYPE_PREDATOR], x[Agent.TYPE_PREY], y[Agent.TYPE_PREY]);
	}
}
