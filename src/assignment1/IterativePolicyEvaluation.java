package assignment1;

import java.util.HashMap;
import java.util.Iterator;

public class IterativePolicyEvaluation
{
	private final HashMap<State, Float> v;

	private int iterations;
	
	public IterativePolicyEvaluation(State env, Agent predator, Agent prey, float theta, float gamma)
	{
		v = new HashMap<State, Float>();
		
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
					
					float factor = 0;
					
					State s2 = (State) s.clone();
					s2.move(predator, actionPredator);
					
					for (int actionPrey: State.AGENT_ACTIONS)
					{
						float probPrey = prey.pi(s2, actionPrey);
						
						State sPrime = (State) s2.clone();
						sPrime.move(prey, actionPrey);
						
						float reward = 0;
						
						if (sPrime.isFinal())
						{
							reward = 10;
						}
						
						if (!v.containsKey(sPrime))
						{
							v.put(sPrime, 0f);
						}
						
						factor += probPrey * (reward + gamma * v.get(sPrime));
					}
					
					valueNew += probPredator * factor;
				}
				
				v.put(s, valueNew);
				
				delta = Math.max(delta, Math.abs(valueOld - valueNew));
			}
		}
		while (delta >= theta);
	}
	
	public float getEvaluation(State env)
	{
		return v.get(env);
	}
	
	public int getIterations()
	{
		return iterations;
	}
}
