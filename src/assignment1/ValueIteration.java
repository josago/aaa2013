package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ValueIteration
{
	private int iterations;
	
	private final HashMap<State, Float> v;
	private final HashMap<State, List<Integer>> pi;
	
	public ValueIteration(State env, Agent prey, float theta, float gamma)
	{
		v = new HashMap<State, Float>();
		
		Agent predator = new PredatorRandom();
		
		float delta;
		
		do
		{
			iterations++;
			
			delta = 0;
			
			Iterator<State> it = env.stateIterator();
			
			while (it.hasNext())
			{
				State s = it.next();
				
				if (!v.containsKey(s))
				{
					v.put(s, 0f);
				}
				
				float valueOld = v.get(s);
				float valueNew = Float.MIN_VALUE;
				
				for (int actionPredator: State.AGENT_ACTIONS)
				{
					float factor = 0;
					
					State s2 = (State) s.clone();
					s2.move(predator, actionPredator);
					
					if (s2.isFinal())
					{
						factor = 10;
					}
					else
					{
						for (int actionPrey: State.AGENT_ACTIONS)
						{
							float probPrey = prey.pi(s2, actionPrey);
							
							State sPrime = (State) s2.clone();
							sPrime.move(prey, actionPrey);
							
							if (!v.containsKey(sPrime))
							{
								v.put(sPrime, 0f);
							}
							
							if (sPrime.isFinal())
							{
								factor += probPrey * 10;
							}
							else
							{
								factor += probPrey * gamma * v.get(sPrime);
							}
						}
					}
					
					valueNew = Math.max(valueNew, factor);
				}
				
				v.put(s, valueNew);
				
				delta = Math.max(delta, Math.abs(valueOld - valueNew));
			}
		}
		while (delta >= theta);
		
		pi = new HashMap<State, List<Integer>>();
		
		Iterator<State> it = env.stateIterator();
		
		while (it.hasNext())
		{
			State s = it.next();
			
			float valueMax = Float.MIN_VALUE;
			List<Integer> bestActions = new ArrayList<Integer>();
			
			for (int actionPredator: State.AGENT_ACTIONS)
			{
				float factor = 0;
				
				State s2 = (State) s.clone();
				s2.move(predator, actionPredator);
				
				if (s2.isFinal())
				{
					factor = 10;
				}
				else
				{
					for (int actionPrey: State.AGENT_ACTIONS)
					{
						float probPrey = prey.pi(s2, actionPrey);
						
						State sPrime = (State) s2.clone();
						sPrime.move(prey, actionPrey);
						
						if (sPrime.isFinal())
						{
							factor += probPrey * 10;
						}
						else
						{
							factor += probPrey * gamma * v.get(sPrime);
						}
					}
				}
				
				if (Math.abs(factor - valueMax) <= theta)
				{
					bestActions.add(actionPredator);
				}
				else if (factor > valueMax)
				{
					valueMax    = factor;
					
					bestActions = new ArrayList<Integer>();
					bestActions.add(actionPredator);
				}
			}

			pi.put(s, bestActions);
		}
	}
	
	public float getEvaluation(State env)
	{
		return v.get(env);
	}
	
	public int getIterations()
	{
		return iterations;
	}
	
	public Agent buildAgent()
	{
		return AgentUtils.buildPredator(pi);
	}
}
