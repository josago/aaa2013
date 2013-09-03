package assignment1;

public class Simulator
{
	public static void printEnvironment(Environment env)
	{
		System.out.print("┌");
		
		for (int i = 0; i < Environment.ENVIRONMENT_SIZE - 1; i++)
		{
			System.out.print("───┬");
		}
		
		System.out.println("───┐");
		
		printEmptyLine(0, env);
		
		for (int i = 1; i < Environment.ENVIRONMENT_SIZE; i++)
		{
			System.out.print("├");
			
			for (int j = 0; j < Environment.ENVIRONMENT_SIZE - 1; j++)
			{
				System.out.print("───┼");
			}
			
			System.out.println("───┤");
			
			printEmptyLine(i, env);
		}
		
		System.out.print("└");
		
		for (int i = 0; i < Environment.ENVIRONMENT_SIZE - 1; i++)
		{
			System.out.print("───┴");
		}
		
		System.out.println("───┘");
	}
	
	private static void printEmptyLine(int i, Environment env)
	{
		System.out.print("│");
		
		for (int j = 0; j < Environment.ENVIRONMENT_SIZE; j++)
		{
			if (env.getPredatorX() == j && env.getPredatorY() == i)
			{
				System.out.print(" " + Predator.PREDATOR_SYMBOL + " │");
			}
			else if (env.getPreyX() == j && env.getPreyY() == i)
			{
				System.out.print(" " + Prey.PREY_SYMBOL + " │");
			}
			else
			{
				System.out.print("   │");
			}
		}
		
		System.out.println("");
	}
}
