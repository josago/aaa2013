package aaa.assignment2.algorithms;

import aaa.*;
import aaa.assignment2.StateActionPair;

public class Sarsa extends ModelFreeAlgorithm
{
	public Sarsa(State env, float alpha, float gamma, float epsilon, float valueInitial)
	{
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
	
		super.valueInitial = valueInitial;
		super.env = env;
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			State s = (State) env.clone();
			
			int action = epsilonGreedy(s, epsilon);
			
			while (!s.isFinal())
			{
				State sPrime = (State) s.clone();
				
				sPrime.move(predator, action);
				sPrime.move(prey);
				
				int actionPrime = epsilonGreedy(sPrime, epsilon);
				
				// Q-value update:
				
				StateActionPair sa      = new StateActionPair(s,      action);
				StateActionPair saPrime = new StateActionPair(sPrime, actionPrime);
				
				float r = s.isFinal() ? 10 : 0;
				
				float oldQ = Q.get(sa);
				float newQ = oldQ + alpha * (r + gamma * Q.get(saPrime) - oldQ);
				
				Q.put(sa, newQ);
				
				s      = sPrime;
				action = actionPrime;
			}
		}
	}
}
