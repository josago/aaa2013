package assignment1;

public class Simulator
{
	public static int runSimulation(State env, Agent prey, Agent predator, int wait, boolean show)
	{
		int turns = 0;
		
		while (true)
		{
			if (show)
			{
				Simulator.printEnvironment(env, prey, predator);
			}
			
			env.move(prey);
			env.move(predator);
			
			turns++;
			
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
