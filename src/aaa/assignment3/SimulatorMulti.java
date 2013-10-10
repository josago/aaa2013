package aaa.assignment3;

import java.util.List;

import aaa.*;

/**
 * This class runs and graphically displays simulations of the proposed world.
 * Please note that you must configure your encoding to UTF-8 to correctly see the graphical output of this class.
 * @author josago
 */
public class SimulatorMulti
{
	public static int TURNS_LIMIT = 10000;
	
	/**
	 * Runs a single simulation.
	 * @param env Initial state.
	 * @param prey The prey to use (which includes a policy for it).
	 * @param predator The predators to use (which include policies for them).
	 * @param wait Time in milliseconds to wait between each simulation step. Set it to zero for the fastest simulation speed.
	 * @param show True if the board must be graphically shown at each simulation step, false otherwise.
	 * @return The number of steps the game lasted.
	 */
	public static int runSimulation(State env, Agent prey, List<Agent> predators, int wait, boolean show)
	{
		int turns = 0;
		
		while (turns < TURNS_LIMIT)
		{
			turns++;
			
			if (show)
			{
				printEnvironment(env, prey, predators);
			}
			
			env.move(prey);
			
			for (Agent predator: predators)
			{
				env.move(predator);
			}
			
			if (env.isFinal())
			{
				return turns;
			}
			
			if (wait > 0)
			{
				try
				{
					Thread.sleep(wait);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Graphically shows a state.
	 * @param env The state to show.
	 * @param prey The prey.
	 * @param predators The predators.
	 */
	public static void printEnvironment(State env, Agent prey, List<Agent> predators)
	{
		System.out.print("┌");
		
		for (int i = 0; i < State.ENVIRONMENT_SIZE - 1; i++)
		{
			System.out.print("───┬");
		}
		
		System.out.println("───┐");
		
		printEmptyLine(0, env, prey, predators);
		
		for (int i = 1; i < State.ENVIRONMENT_SIZE; i++)
		{
			System.out.print("├");
			
			for (int j = 0; j < State.ENVIRONMENT_SIZE - 1; j++)
			{
				System.out.print("───┼");
			}
			
			System.out.println("───┤");
			
			printEmptyLine(i, env, prey, predators);
		}
		
		System.out.print("└");
		
		for (int i = 0; i < State.ENVIRONMENT_SIZE - 1; i++)
		{
			System.out.print("───┴");
		}
		
		System.out.println("───┘");
	}
	
	private static void printEmptyLine(int i, State env, Agent prey, List<Agent> predators)
	{
		System.out.print("│");
		
		for (int j = 0; j < State.ENVIRONMENT_SIZE; j++)
		{
			boolean done = false;
			
			for (Agent predator: predators)
			{
				if (env.getX(predator) == j && env.getY(predator) == i)
				{
					System.out.print(" " + predator.getSymbol() + " │");
					
					done = true;
				}
			}

			if (env.getX(prey) == j && env.getY(prey) == i)
			{
				System.out.print(" " + prey.getSymbol() + " │");
			}
			else if (!done)
			{
				System.out.print("   │");
			}
		}
		
		System.out.println("");
	}
}
