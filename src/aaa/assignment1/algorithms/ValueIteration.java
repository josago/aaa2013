package aaa.assignment1.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import aaa.Agent;
import aaa.AgentUtils;
import aaa.State;

/**
 * This class implements the Value Iteration algorithm (assignment 1.4).
 * @author josago
 */
public class ValueIteration extends BellmanAlgorithm
{
	private final HashMap<State, List<Integer>> pi;
	
	public ValueIteration(State env, Agent prey, float theta, float gamma)
	{
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
					float factor = innerLoop(s, actionPredator, prey, gamma);
					
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
				float factor = innerLoop(s, actionPredator, prey, gamma);
				
				valueMax = maxActions(factor, valueMax, theta, bestActions, actionPredator);
			}

			pi.put(s, bestActions);
		}
	}
	
	public Agent buildAgent()
	{
		return AgentUtils.buildPredator(pi);
	}
}
