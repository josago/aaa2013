package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.*;

public class Test1
{
	public static final int NUM_PREDATORS = 2;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		State env = new StateMulti(prey, prey, predators);
		int turns = SimulatorMulti.runSimulation(env, prey, predators, 250, true);
		
		System.out.println("The game finished in " + turns + " turns.");
	}
}
