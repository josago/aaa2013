package assignment1;

public class Test4
{
	public static final float THETA = 0.000000001f;
	
	public static void main(String[] args)
	{
		State stateInitial = new StateSimple(0, 0, 5, 5);
		Agent prey         = new PreySimple();
		
		ValueIteration vi;
		
		for (float GAMMA: new float[] {0.1f, 0.5f, 0.7f, 0.9f})
		{
			vi = new ValueIteration(stateInitial, prey, THETA, GAMMA);
			
			System.out.println("Number of iterations for gamma = " + GAMMA + ": " + vi.getIterations());
			
			if (GAMMA == 0.9f)
			{
				for (int x = 0; x < State.ENVIRONMENT_SIZE; x++)
				{
					for (int y = 0; y < State.ENVIRONMENT_SIZE; y++)
					{
						System.out.println("Value for Predator(" + x + ", " + y + ") Prey(5, 5): " + vi.getEvaluation(new StateSimple(x, y, 5, 5)));
					}
				}
			}
		}
	}
}
