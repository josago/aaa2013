package aaa.assignment2.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;

public class MonteCarloOnPolicy extends ModelFreeAlgorithm
{
	public MonteCarloOnPolicy(State env, float gamma, float epsilon, float valueInitial, boolean wantPerformance)
	{
		HashMap<StateActionPair, Integer> counts = new HashMap<StateActionPair, Integer>();
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
	
		super.valueInitial = valueInitial;
		super.env = env;
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			State s = (State) env.clone();
			
			List<StateActionPair> stateActions = new ArrayList<StateActionPair>();
			
			while (!s.isFinal())
			{
				int action = epsilonGreedy(s, epsilon);
				
				stateActions.add(new StateActionPair((State) s.clone(), action));
				
				s.move(predator, action);
				s.move(prey);
			}
			
			HashMap<StateActionPair, Float> returnsTemp = new HashMap<StateActionPair, Float>();
			
			for (int j = 0; j < stateActions.size(); j++)
			{
				StateActionPair sa = stateActions.get(j);
				
				float returnThis = (float) (10 * Math.pow(gamma, stateActions.size() - 1 - j));
				
				if (!returnsTemp.containsKey(sa) || returnsTemp.get(sa) > returnThis)
				{
					returnsTemp.put(sa, returnThis);
				}
			}
			
			for (StateActionPair sa: returnsTemp.keySet())
			{
				if (!counts.containsKey(sa))
				{
					counts.put(sa, 0);
				}
				
				int countPrev = counts.get(sa);
				counts.put(sa, countPrev + 1);
				
				float returnThis = returnsTemp.get(sa);
				
				if (!Q.containsKey(sa))
				{
					Q.put(sa, valueInitial);
				}
				
				float returnAverage = (Q.get(sa) * countPrev + returnThis) / (countPrev + 1);
				Q.put(sa, returnAverage);
			}
			
			if (wantPerformance && i % 10 == 0)
			{
				performanceAdd(i);
			}
		}
	}
}
