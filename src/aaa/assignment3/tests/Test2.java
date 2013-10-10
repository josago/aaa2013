package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.*;
import aaa.assignment3.algorithms.QLearningMulti;

public class Test2 
{
	public static final int NUM_PREDATORS = 1;
	
	public static final float ALPHA         = 0.9f;
	public static final float GAMMA         = 0.9f;
	public static final float EPSILON       = 1.0f;
	public static final float VALUE_INITIAL = 0;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		StateMulti     env = new StateMulti(prey, prey, predators);
		QLearningMulti ql  = new QLearningMulti(env, prey, predators, ALPHA, GAMMA, EPSILON, VALUE_INITIAL, false, false);
		
		Agent preyNew = ql.buildAgent(prey);
		List<Agent> predatorsNew = new ArrayList<Agent>();
		
		for (Agent predator: predators)
		{
			predatorsNew.add(ql.buildAgent(predator));
		}
		
		env = new StateMulti(preyNew, preyNew, predatorsNew);
		int turns = SimulatorMulti.runSimulation(env, preyNew, predatorsNew, 250, true);
		
		System.out.println("The game finished in " + turns + " turns.");
		
		if (env.getReward(prey) > 0)
		{
			System.out.println("The prey escaped.");
		}
		else
		{
			System.out.println("The prey was catched.");
		}
	}
}
