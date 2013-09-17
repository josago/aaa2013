package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PolicyIteration
{
	public final Agent optimalPredator;
	
	private final IterativePolicyEvaluation ipe;

	private int iterations;
	
	public PolicyIteration(State env, Agent predator, Agent prey, float theta, float gamma)
	{
		HashMap<State, List<Integer>> pi = new HashMap<State, List<Integer>>();
		
		IterativePolicyEvaluation ipe;
		
		iterations = 0;
		
		boolean policyStable;
		
		do
		{
			policyStable = true;
			
			ipe = new IterativePolicyEvaluation(env, predator, prey, theta, gamma);
		
			Iterator<State> it = env.stateIterator();
			
			while (it.hasNext())
			{
				State s = it.next();
				
				List<Integer> oldActions = new ArrayList<Integer>();
				
				for (int action: State.AGENT_ACTIONS)
				{
					if (predator.pi(s, action) > 0)
					{
						oldActions.add(action);
					}
				}
				
				float valueMax = Float.MIN_VALUE;
				List<Integer> newActions = new ArrayList<Integer>();
				
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
								factor += probPrey * gamma * ipe.getEvaluation(sPrime);
							}
						}
					}
					
					if (Math.abs(factor - valueMax) <= theta)
					{
						newActions.add(actionPredator);
					}
					else if (factor > valueMax)
					{
						valueMax    = factor;
						
						newActions = new ArrayList<Integer>();
						newActions.add(actionPredator);
					}
				}

				pi.put(s, newActions);
				
				if (!oldActions.equals(newActions))
				{
					policyStable = false;
				}
			}
			
			predator = AgentUtils.buildPredator(pi);
			
			iterations++;
		}
		while (!policyStable);
		
		optimalPredator = predator;
		
		this.ipe = ipe;
	}
	
	public float getEvaluation(State env)
	{
		return ipe.getEvaluation(env);
	}
	
	public int getIterations()
	{
		return iterations;
	}
}
