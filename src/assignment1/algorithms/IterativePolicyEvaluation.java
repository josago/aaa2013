package assignment1.algorithms;

import java.util.Iterator;

import assignment1.Agent;
import assignment1.State;

/**
 * This class implements the Iterative Policy Evaluation algorithm (assignment 1.2).
 * @author josago
 */
public class IterativePolicyEvaluation extends BellmanAlgorithm
{
	public IterativePolicyEvaluation(State env, Agent predator, Agent prey, float theta, float gamma)
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
				float valueNew = 0;
				
				for (int actionPredator: State.AGENT_ACTIONS)
				{
					float probPredator = predator.pi(s, actionPredator);

					valueNew += probPredator * innerLoop(s, actionPredator, prey, gamma);
				}
				
				v.put(s, valueNew);
				
				delta = Math.max(delta, Math.abs(valueOld - valueNew));
			}
		}
		while (delta >= theta);
	}
}
