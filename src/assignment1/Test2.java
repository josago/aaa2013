package assignment1;

public class Test2
{
	public static final int TEST_NUM_RUNS = 100000;
	
	public static void main(String[] args)
	{
		int[] length = new int[TEST_NUM_RUNS];
	
		StateSimple env = new StateSimple(0, 0, 5, 5);
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			length[r] = Simulator.runSimulation(env, prey, predator, 0, false);
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
