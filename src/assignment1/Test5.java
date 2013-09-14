package assignment1;

public class Test5
{
	public static final float THETA = 0.000000001f;
	public static final float GAMMA = 0.9f;
	
	public static void main(String[] args)
	{
		State stateInitial = new StateSimple(0, 0, 5, 5);
		Agent prey         = new PreySimple();

		ValueIteration vi = new ValueIteration(stateInitial, prey, THETA, GAMMA);

		Agent predator = vi.buildAgent();
		
		int turns = Simulator.runSimulation(stateInitial, prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
