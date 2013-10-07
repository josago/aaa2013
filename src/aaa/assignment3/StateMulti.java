package aaa.assignment3;

import java.util.*;

import aaa.*;

public class StateMulti implements State
{
	private final Agent prey;
	private final List<Agent> predators;
	
	private HashMap<Agent, Integer> x;
	private HashMap<Agent, Integer> y;
	
	public StateMulti(Agent prey, List<Agent> predators)
	{
		this.prey      = prey;
		this.predators = predators;
		
		x = new HashMap<Agent, Integer>();
		y = new HashMap<Agent, Integer>();
		
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
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object clone()
	{
		StateMulti clone = new StateMulti(prey, predators);
		
		clone.x = (HashMap<Agent, Integer>) x.clone();
		clone.y = (HashMap<Agent, Integer>) y.clone();
		
		return clone;
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
}
