package assignment1.algorithms;

import java.util.HashMap;
import java.util.List;

import assignment1.Agent;
import assignment1.PredatorRandom;
import assignment1.State;

/**
 * This class contains the basic code shared by all Bellman-like algorithms implemented for assignment 1.
 * @author josago
 */
public abstract class BellmanAlgorithm
{
	protected int iterations;
	
	protected HashMap<State, Float> v;
	
	public BellmanAlgorithm()
	{
		iterations = 0;
		
		v = new HashMap<State, Float>();
	}
	
	/**
	 * Returns the value of a state.
	 * @param env A state.
	 * @return The value of the given state.
	 */
	public float getEvaluation(State env)
	{
		return v.get(env);
	}
	
	/**
	 * Returns the number of iterations that were needed to make the algorithm converge.
	 * @return The number of iterations that were needed to make the algorithm converge.
	 */
	public int getIterations()
	{
		return iterations;
	}
	
	/**
	 * This method implements the inner loop of the Bellman-like equations that are used in the algorithms.
	 * @param s A state.
	 * @param actionPredator An action for the predator.
	 * @param prey A prey agent (including its policy).
	 * @param gamma Discount factor.
	 * @return The inner factor for the Bellman-like equation.
	 */
	protected float innerLoop(State s, int actionPredator, Agent prey, float gamma)
	{
		float factor = 0;
		
		if (!v.containsKey(s))
		{
			v.put(s, 0f);
		}

		// The predator moves first:
		
		State s2 = (State) s.clone();
		s2.move(new PredatorRandom(), actionPredator);
		
		if (s2.isFinal())
		{
			// If the predator catches the prey, the latter doesn't move and the game is over:
			
			factor = 10;
		}
		else
		{
			for (int actionPrey: State.AGENT_ACTIONS)
			{
				float probPrey = prey.pi(s2, actionPrey);
				
				// The prey moves afterwards:
				
				State sPrime = (State) s2.clone();
				sPrime.move(prey, actionPrey);
				
				if (!v.containsKey(sPrime))
				{
					v.put(sPrime, 0f);
				}
				
				if (sPrime.isFinal())
				{
					// If the game is over, the immediate reward is 10 and future rewards will be 0:
					
					factor += probPrey * 10;
				}
				else
				{
					// If the game is not over, the immediate reward is 0 and future rewards will be weighted by the discount factor:
					
					factor += probPrey * gamma * v.get(sPrime);
				}
			}
		}
		
		return factor;
	}
	
	/**
	 * This method keeps track of the actions that maximize the state-value function.
	 * @param factor Output of innerLoop() within the loop this function is called from.
	 * @param valueMax Maximum state value so far for an action.
	 * @param theta Level of precision desired (stop condition of the algorithms).
	 * @param listActions List of best actions so far.
	 * @param action The current action within the loop this function is called from.
	 * @return Updated maximum state value for an action.
	 */
	protected float maxActions(float factor, float valueMax, float theta, List<Integer> listActions, int action)
	{
		if (Math.abs(factor - valueMax) <= theta)
		{
			// This action is as good as the previous ones, so we add it to the list:
			
			listActions.add(action);
		}
		else if (factor > valueMax)
		{
			// This action is better than the previous ones, so we get rid of the latter and store this one:
			
			listActions.clear();
			listActions.add(action);
			
			return factor; // valueMax is updated.
		}
		
		return valueMax;  // valueMax is not updated.
	}
}
