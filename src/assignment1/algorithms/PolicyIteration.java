package assignment1.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import assignment1.Agent;
import assignment1.AgentUtils;
import assignment1.State;

/**
 * This class implements the Policy Iteration algorithm (assignment 1.3).
 * @author josago
 */
public class PolicyIteration extends BellmanAlgorithm
{
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
			v = ipe.v;
		
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
					float factor = innerLoop(s, actionPredator, prey, gamma);
					
					valueMax = maxActions(factor, valueMax, theta, newActions, actionPredator);
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
	}
}
