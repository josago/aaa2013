package aaa;

/**
 * This class runs and graphically displays simulations of the proposed world.
 * Please note that you must configure your encoding to UTF-8 to correctly see the graphical output of this class.
 * @author josago
 * TODO: Change predator/prey movement behaviour!!!!
 */
public class Simulator
{
	/**
	 * Runs a single simulation.
	 * @param env Initial state.
	 * @param prey The prey to use (which includes a policy for it).
	 * @param predator The predator to use (which include a policy for it).
	 * @param wait Time in milliseconds to wait between each simulation step. Set it to zero for the fastest simulation speed.
	 * @param show True if the board must be graphically shown at each simulation step, false otherwise.
	 * @return The number of steps the game lasted.
	 */
	public static int runSimulation(State env, Agent prey, Agent predator, int wait, boolean show)
	{
		int turns = 0;
		
		while (true)
		{
			turns++;
			
			if (show)
			{
				Simulator.printEnvironment(env, prey, predator);
			}
			
			env.move(predator);
			
			if (env.isFinal())
			{
				return turns;
			}
			
			env.move(prey);
			
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
	}
	
	/**
	 * Graphically shows a state.
	 * @param env The state to show.
	 * @param prey The prey.
	 * @param predator The predator.
	 */
	public static void printEnvironment(State env, Agent prey, Agent predator)
	{
		System.out.print("┌");
		
		for (int i = 0; i < State.ENVIRONMENT_SIZE - 1; i++)
		{
			System.out.print("───┬");
		}
		
		System.out.println("───┐");
		
		printEmptyLine(0, env, prey, predator);
		
		for (int i = 1; i < State.ENVIRONMENT_SIZE; i++)
		{
			System.out.print("├");
			
			for (int j = 0; j < State.ENVIRONMENT_SIZE - 1; j++)
			{
				System.out.print("───┼");
			}
			
			System.out.println("───┤");
			
			printEmptyLine(i, env, prey, predator);
		}
		
		System.out.print("└");
		
		for (int i = 0; i < State.ENVIRONMENT_SIZE - 1; i++)
		{
			System.out.print("───┴");
		}
		
		System.out.println("───┘");
	}
	
	private static void printEmptyLine(int i, State env, Agent prey, Agent predator)
	{
		System.out.print("│");
		
		for (int j = 0; j < State.ENVIRONMENT_SIZE; j++)
		{
			if (env.getX(predator) == j && env.getY(predator) == i)
			{
				System.out.print(" " + predator.getSymbol() + " │");
			}
			else if (env.getX(prey) == j && env.getY(prey) == i)
			{
				System.out.print(" " + prey.getSymbol() + " │");
			}
			else
			{
				System.out.print("   │");
			}
		}
		
		System.out.println("");
	}
}
