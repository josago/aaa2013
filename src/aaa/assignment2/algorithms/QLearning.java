package aaa.assignment2.algorithms;

import aaa.*;
import aaa.assignment2.StateActionPair;

public class QLearning extends ModelFreeAlgorithm
{
	public QLearning(State env, float alpha, float gamma, float epsilon, float valueInitial, boolean useSoftmax, boolean wantPerformance)
	{
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
	
		super.valueInitial = valueInitial;
		super.env = env;
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			State s = (State) env.clone();
			
			while (!s.isFinal())
			{
				int action;
				
				if (useSoftmax)
				{
					action = softmax(s, epsilon); // epsilon used as tau when useSoftmax is true.
				}
				else
				{
					action = epsilonGreedy(s, epsilon);
				}
				
				
				// Q-value update:
				
				StateActionPair sa = new StateActionPair(s, action);
				
				State sPrime = (State) s.clone();
				
				sPrime.move(predator, action);
				sPrime.move(prey);
				
				float maxQ = Float.MIN_VALUE;
				
				for (int a: State.AGENT_ACTIONS)
				{
					StateActionPair sa2 = new StateActionPair(sPrime, a);
					
					if (!Q.containsKey(sa2))
					{
						Q.put(sa2, valueInitial);
					
					}
					
					maxQ = Math.max(Q.get(sa2), maxQ);
				}
				
				float R = sPrime.isFinal() ? 10 : gamma * maxQ;
				
				float oldQ = Q.get(sa);
				float newQ = oldQ + alpha * (R - oldQ);
				
				Q.put(sa, newQ);
				
				s = sPrime;
			}

			if (wantPerformance && i % 10 == 0)
			{
				performanceAdd(i, env, buildAgent());
			}
		}
	}
}
