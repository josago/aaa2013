package aaa.assignment3;

import java.util.*;

import aaa.*;

public class StateMulti implements State
{
	private Agent me;
	
	private final Agent prey;
	private final List<Agent> predators;
	
	private HashMap<Agent, Coordinate> positions;
	private List<Coordinate> distances;
	
	public StateMulti(Agent me, Agent prey, List<Agent> predators)
	{
		this.me        = me;
		this.prey      = prey;
		this.predators = predators;
		
		positions = new HashMap<Agent, Coordinate>();
		distances = new ArrayList<Coordinate>();
		
		positions.put(prey, new Coordinate(5, 5));
		
		switch (predators.size())
		{
			case 4:
				positions.put(predators.get(3), new Coordinate(0, 10));
			case 3:
				positions.put(predators.get(2), new Coordinate(10, 0));
			case 2:
				positions.put(predators.get(1), new Coordinate(10, 10));
			case 1:
				positions.put(predators.get(0), new Coordinate(0, 0));
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
		return positions.get(agent).x;
	}

	@Override
	public int getY(Agent agent)
	{
		return positions.get(agent).y;
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
			int newX, newY;
			
			if (action % 2 == 0)
			{
				newX = StateSimple.fixCoord(positions.get(agent).x + action / 2);
				newY = positions.get(agent).y;
			}
			else
			{
				newX = positions.get(agent).x;
				newY = StateSimple.fixCoord(positions.get(agent).y + action);
			}
			
			positions.put(agent, new Coordinate(newX, newY));
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
	
	@SuppressWarnings("unchecked")
	private void calculateDistances()
	{
		// Initial calculation of distances:
		
		List<Coordinate> distances = new ArrayList<Coordinate>();
		
		for (Agent predator: predators)
		{
			if (predator != me)
			{
				distances.add(calculateDistance(predator));
			}
		}
		
		// Ordering of the predators by distance:
		
		Collections.sort(distances);
		
		// We finally add the prey:
		
		if (me.getType() == Agent.TYPE_PREDATOR)
		{
			distances.add(0, calculateDistance(prey));
		}
		
		this.distances = distances;
	}
	
	private Coordinate calculateDistance(Agent agent)
	{
		int distX, distY;
		
		int innerX = positions.get(me).x - positions.get(agent).x;
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
			distX = innerX;
		}
		else
		{
			distX = outterX;
		}
		
		int innerY = positions.get(me).y - positions.get(agent).y;
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
			distY = innerY;
		}
		else
		{
			distY = outterY;
		}
		
		return new Coordinate(distX, distY);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone()
	{
		StateMulti clone = new StateMulti(me, prey, predators);

		clone.positions = (HashMap<Agent, Coordinate>) this.positions.clone();
		clone.distances = new ArrayList<Coordinate>();
		
		clone.calculateDistances();
		
		return clone;
	}

	@Override
	public int hashCode()
	{
		final int[] primes = new int[] {19, 23, 29, 31};
		final int   prime  = primes[distances.size() - 1];
		
		int result = 1;
		
		for (Coordinate c: distances)
		{
			result = prime * result + c.x;
			result = prime * result + c.y;
		}
		
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		StateMulti other = (StateMulti) obj;
		
		if (this.distances.size() == other.distances.size())
		{
			for (int i = 0; i < distances.size(); i++)
			{
				if (!this.distances.get(i).equals(other.distances.get(i)))
				{
					return false;
				}
			}

			return true;
		}
		
		return false;
	}
}
