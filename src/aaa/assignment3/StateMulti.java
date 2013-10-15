package aaa.assignment3;

import java.util.*;

import aaa.*;

public class StateMulti implements State
{
	private Agent me;
	
	private final Agent prey;
	private final List<Agent> predators;
	
	private HashMap<Agent, Integer> x;
	private HashMap<Agent, Integer> y;
	
	private int[] xDist;
	private int[] yDist;
	
	public StateMulti(Agent me, Agent prey, List<Agent> predators)
	{
		this.me        = me;
		this.prey      = prey;
		this.predators = predators;
		
		x = new HashMap<Agent, Integer>();
		y = new HashMap<Agent, Integer>();
		
		xDist = new int[predators.size()];
		yDist = new int[predators.size()];
		
		x.put(prey, 5);
		y.put(prey, 5);
		
		switch (predators.size())
		{
			case 4:
				x.put(predators.get(3), 0);
				y.put(predators.get(3), 10);
			case 3:
				x.put(predators.get(2), 10);
				y.put(predators.get(2), 0);
			case 2:
				x.put(predators.get(1), 10);
				y.put(predators.get(1), 10);
			case 1:
				x.put(predators.get(0), 0);
				y.put(predators.get(0), 0);
		}
		
		calculateDistances();
	}

	@Override
	public Iterator<State> stateIterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFinal()
	{
		return collisionPrey() || collisionPredators();
	}

	@Override
	public int getX(Agent agent)
	{
		return x.get(agent);
	}

	@Override
	public int getY(Agent agent)
	{
		return y.get(agent);
	}

	@Override
	public void move(Agent agent)
	{
		float sum    = 0;
		float random = (float) Math.random();
		
		for (int action: AGENT_ACTIONS)
		{
			sum += agent.pi(this, action);
			
			if (sum >= random)
			{
				move(agent, action);
				
				break;
			}
		}
	}
	
	@Override
	public void move(Agent agent, int action)
	{
		float random = (float) Math.random();
		
		if (agent.getType() != Agent.TYPE_PREY || random > 0.2f) // Prey tripping behaviour.
		{
			if (action % 2 == 0)
			{
				x.put(agent, StateSimple.fixCoord(x.get(agent) + action / 2));
			}
			else
			{
				y.put(agent, StateSimple.fixCoord(y.get(agent) + action));
			}
		}
		
		calculateDistances();
	}
	
	public void changeViewPoint(Agent me)
	{
		this.me = me;
		
		calculateDistances();
	}
	
	public int getReward(Agent agent)
	{
		if (collisionPredators())
		{
			if (agent.getType() == Agent.TYPE_PREDATOR)
			{
				return -10;
			}
			else // Agent.TYPE_PREY
			{
				return +10;
			}
		}
		else if (collisionPrey())
		{
			if (agent.getType() == Agent.TYPE_PREDATOR)
			{
				return +10;
			}
			else // Agent.TYPE_PREY
			{
				return -10;
			}
		}
		else
		{
			return 0;
		}
	}
	
	private boolean collisionPredators()
	{
		for (int i = 0; i < predators.size(); i++)
		{
			for (int j = i + 1; j < predators.size(); j++)
			{
				Agent predatorA = predators.get(i);
				Agent predatorB = predators.get(j);
				
				if (getX(predatorA) == getX(predatorB) && getY(predatorA) == getY(predatorB))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean collisionPrey()
	{
		for (Agent predator: predators)
		{
			if (getX(predator) == getX(prey) && getY(predator) == getY(prey))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void calculateDistances()
	{
		int index = 0;
		
		if (me.getType() == Agent.TYPE_PREDATOR)
		{
			calculateDistance(prey, index++);
		}
		
		for (Agent predator: predators)
		{
			if (predator != me)
			{
				calculateDistance(predator, index++);
			}
		}
	}
	
	private void calculateDistance(Agent agent, int index)
	{
		int innerX = x.get(me) - x.get(agent);
		int outterX;
		
		if (innerX < 0)
		{
			outterX = innerX + ENVIRONMENT_SIZE;
		}
		else
		{
			outterX = innerX - ENVIRONMENT_SIZE;
		}
		
		if (Math.abs(innerX) < Math.abs(outterX))
		{
			xDist[index] = innerX;
		}
		else
		{
			xDist[index] = outterX;
		}
		
		int innerY = y.get(me) - y.get(agent);
		int outterY;
		
		if (innerY < 0)
		{
			outterY = innerY + ENVIRONMENT_SIZE;
		}
		else
		{
			outterY = innerY - ENVIRONMENT_SIZE;
		}
		
		if (Math.abs(innerY) < Math.abs(outterY))
		{
			yDist[index] = innerY;
		}
		else
		{
			yDist[index] = outterY;
		}
	}
	
	@Override
	public Object clone()
	{
		StateMulti clone = new StateMulti(me, prey, predators);
		
		clone.x = new HashMap<Agent, Integer>();
		clone.y = new HashMap<Agent, Integer>();
		
		clone.x.put(prey, this.x.get(prey));
		clone.y.put(prey, this.y.get(prey));
		
		for (Agent predator: predators)
		{
			clone.x.put(predator, this.x.get(predator));
			clone.y.put(predator, this.y.get(predator));
		}
		
		clone.calculateDistances();
		
		return clone;
	}

	@Override
	public int hashCode()
	{
		final int[] primes = new int[] {19, 23, 29, 31};
		final int   prime  = primes[xDist.length - 1];
		
		int result = 1;
		
		for (int v: xDist)
		{
			result = prime * result + v;
		}
		
		for (int v: yDist)
		{
			result = prime * result + v;
		}
		
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		StateMulti other = (StateMulti) obj;
		
		if (this.xDist.length == other.xDist.length)
		{
			for (int i = 0; i < xDist.length; i++)
			{
				if (this.xDist[i] != other.xDist[i] || this.yDist[i] != other.yDist[i])
				{
					return false;
				}
			}

			return true;
		}
		
		return false;
	}
}
