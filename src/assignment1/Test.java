package assignment1;

public class Test
{
	public static final int TEST_NUM_RUNS = 100000;
	
	public static void main(String[] args)
	{
		int[] length = new int[TEST_NUM_RUNS];
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			Prey prey = new Prey();
			Predator predator = new PredatorRandom();
			
			Environment env = new EnvironmentSimple(0, 0, 5, 5);
			
			int turns = 0;
			
			while (true)
			{
				// Simulator.printEnvironment(env);
				
				prey.move(env);
				predator.move(env);
				
				turns++;
				
				if (env.getPredatorX() == env.getPreyX() && env.getPredatorY() == env.getPreyY())
				{
					// System.out.println("The predator catched the prey in " + turns + " turns.");
					
					break;
				}
			}
			
			length[r] = turns;
		}
		
		long mean = 0;
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			mean += length[r];
		}
		
		mean /= TEST_NUM_RUNS;
		
		System.out.println("Mean length of " + TEST_NUM_RUNS + " games: " + mean + " turns.");
		
		long std = 0;
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			std += Math.pow(length[r] - mean, 2);
		}
		
		std = (long) Math.pow(std / TEST_NUM_RUNS, 0.5);
		
		System.out.println("Standard deviation: " + std);
	}
}
