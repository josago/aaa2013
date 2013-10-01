package aaa.assignment2.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;

public class MonteCarloOffPolicy extends ModelFreeAlgorithm
{
	public MonteCarloOffPolicy(State env, float gamma, float valueInitial)
	{
		super.valueInitial = valueInitial;
		super.env = env;
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		HashMap<StateActionPair, Float> N = new HashMap<StateActionPair, Float>();
		HashMap<StateActionPair, Float> D = new HashMap<StateActionPair, Float>();
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			State s = (State) env.clone();
			
			List<StateActionPair> stateActions = new ArrayList<StateActionPair>();
			
			while (!s.isFinal())
			{
				int action = epsilonGreedy(s, 1); // Always random action.
				
				stateActions.add(new StateActionPair((State) s.clone(), action));
				
				s.move(predator, action);
				s.move(prey);
			}
			
			int tau = stateActions.size();
			
			for (int t = 0; t < stateActions.size(); t++)
			{
				List<Integer> greedyActions = greedyActions(stateActions.get(t).state);
				
				if (!greedyActions.contains(stateActions.get(t).action))
				{
					tau = t;
				}
			}
			
			HashMap<StateActionPair, Float> w = new HashMap<StateActionPair, Float>();
			
			for (int t = tau; t < stateActions.size(); t++)
			{
				StateActionPair sa = stateActions.get(t);
				
				if (!w.containsKey(sa))
				{
					int T = stateActions.size() - 1;
					int times = Math.max((T - 1) - (t + 1) + 1, 0);
					
					w.put(sa, (float) Math.pow(State.AGENT_ACTIONS.length, times));
					
					float returnThis = (float) (10 * Math.pow(gamma, T - t));
					
					if (!N.containsKey(sa))
					{
						N.put(sa, 0f);
						D.put(sa, 0f);
					}
					
					N.put(sa, N.get(sa) + w.get(sa) * returnThis);
					D.put(sa, D.get(sa) + w.get(sa));
					Q.put(sa, N.get(sa) / D.get(sa));
				}
			}
		}
	}
}
