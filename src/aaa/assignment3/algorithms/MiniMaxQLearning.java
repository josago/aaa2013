package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;
import aaa.assignment3.*;

public class MiniMaxQLearning
{
	public static final int NUM_EPISODES = 50000;
	
	private final HashMap<State, Float> V;
	private final HashMap<StateActionOpponent, Float> Q;
	
	private final HashMap<StateActionPair, Float> pi;
	
	public MiniMaxQLearning(float epsilon, float decay, float gamma)
	{
		V = new HashMap<State, Float>();
		Q = new HashMap<StateActionOpponent, Float>();
		
		pi = new HashMap<StateActionPair, Float>();
		
		float alpha = 1.0f;
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		List<Agent> predators = new ArrayList<Agent>();
		predators.add(predator);
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		// Q-Learning:
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			//if (i % 100 == 0)
			//{
				System.out.println(i);
			//}
			
			StateMulti s = (StateMulti) env.clone();
			
			while (!s.isFinal())
			{
				StateMulti stateBefore = (StateMulti) s.clone();
				
				stateBefore.changeViewPoint(predator);
				int action   = getAction(stateBefore, epsilon, predator);
				
				stateBefore.changeViewPoint(prey);
				int opponent = getAction(stateBefore, epsilon, prey);
				
				StateMulti stateAfter = (StateMulti) stateBefore.clone();
				stateAfter.move(predator, action);
				stateAfter.move(prey,     opponent);
				
				int reward = stateAfter.getReward(predator);
				
				StateActionOpponent sao = new StateActionOpponent(stateBefore, action, opponent);
				
				if (!Q.containsKey(sao))
				{
					Q.put(sao, 1.0f);
				}
				
				if (!V.containsKey(stateAfter))
				{
					V.put(stateAfter, 1.0f);
				}
				
				Q.put(sao, (1 - alpha) * Q.get(sao) + alpha * (reward + gamma * V.get(stateAfter)));
				
				linearProg(stateBefore);
				
				alpha *= decay;
				
				s = stateAfter;
			}
		}
	}
	
	public Agent buildAgent(Agent agent)
	{
		HashMap<State, List<Integer>> pi = new HashMap<State, List<Integer>>();
		
		for (StateActionPair sa: this.pi.keySet())
		{
			if (!pi.containsKey(sa.state))
			{
				pi.put(sa.state, new ArrayList<Integer>());
			}
			
			if (this.pi.get(sa) > 0 && agent.getType() == Agent.TYPE_PREDATOR)
			{
				pi.get(sa.state).add(sa.action);
			}
			else if (this.pi.get(sa) == 0 && agent.getType() == Agent.TYPE_PREY)
			{
				pi.get(sa.state).add(sa.action);
			}
		}
		
		return AgentUtils.buildPredator(pi);
	}
	
	private int getAction(State s, float epsilon, Agent agent)
	{
		float random = (float) Math.random();
		
		if (random < epsilon) // Random action:
		{
			float sum = 0;
			
			for (int action: State.AGENT_ACTIONS)
			{
				sum += epsilon / State.AGENT_ACTIONS.length;
					
				if (sum > random)
				{
					return action;
				}
			}
		}
		else // Random action following pi(s):
		{
			float sum = epsilon;
			
			for (int action: State.AGENT_ACTIONS)
			{
				StateActionPair sa = new StateActionPair((State) s.clone(), action);
				
				if (!pi.containsKey(sa))
				{
					pi.put(sa, 1.0f / State.AGENT_ACTIONS.length);
				}
				
				if (agent.getType() == Agent.TYPE_PREDATOR)
				{
					sum += (1 - epsilon) * pi.get(sa);
				}
				else // TYPE_PREY:
				{
					sum += (1 - epsilon) * (1 - pi.get(sa));
				}
					
				if (sum > random)
				{
					return action;
				}
			}
		}
		
		return State.AGENT_MOVE_STAY;
	}
	
	private void linearProg(State s)
	{
		float DELTA = 0.01f;
		
		if (!V.containsKey(s))
		{
			V.put(s, 1.0f);
		}
		
		float newV;
		
		int iterations = 0;
		
		do
		{
			// Opponent minimization:
			
			int opponentMin = Agent.ACTION_STAY;
			float sumMin = Float.POSITIVE_INFINITY;
			
			for (int opponent: State.AGENT_ACTIONS)
			{
				float sum = 0;
				
				for (int action: State.AGENT_ACTIONS)
				{
					StateActionPair sa = new StateActionPair((State) s.clone(), action);
					
					if (!pi.containsKey(sa))
					{
						pi.put(sa, 1.0f / State.AGENT_ACTIONS.length);
					}
					
					StateActionOpponent sao = new StateActionOpponent((State) s.clone(), action, opponent);
					
					if (!Q.containsKey(sao))
					{
						Q.put(sao, 1.0f);
					}
					
					sum += Q.get(sao) * pi.get(sa);
				}
				
				if (sum < sumMin)
				{
					sumMin = sum;
					opponentMin = opponent;
				}
			}
			
			// Action maximization:
			
			float actionMax = Float.NEGATIVE_INFINITY;
			List<Integer> actionsGreedy = new ArrayList<Integer>();
			
			for (int action: State.AGENT_ACTIONS)
			{
				StateActionOpponent sao = new StateActionOpponent((State) s.clone(), action, opponentMin);
				
				float value = Q.get(sao);
				
				if (value > actionMax)
				{
					actionMax = value;
					
					actionsGreedy.clear();
					actionsGreedy.add(action);
				}
				else if (Math.abs(value - actionMax) < DELTA)
				{
					actionsGreedy.add(action);
				}
			}
			
			for (int action: State.AGENT_ACTIONS)
			{
				StateActionPair sa = new StateActionPair((State) s.clone(), action);
				
				if (actionsGreedy.contains(action))
				{
					pi.put(sa, 1.0f / actionsGreedy.size());
				}
				else
				{
					pi.put(sa, 0.0f);
				}
			}
			
			// Final update:
			
			newV = 0.0f;
			
			for (int action: State.AGENT_ACTIONS)
			{
				StateActionPair sa = new StateActionPair((State) s.clone(), action);

				StateActionOpponent sao = new StateActionOpponent((State) s.clone(), action, opponentMin);
				
				newV += Q.get(sao) * pi.get(sa);
			}
			
			V.put(s, newV);
		//} while (Math.abs(oldV - newV) > DELTA);
		} while (++iterations < 5);
	}
}
