package assignment1;

public class Test6
{
	public static final float THETA = 0.000000001f;
	public static final float GAMMA = 0.9f;
	
	public static void main(String[] args)
	{
		State stateSimple  = new StateSimple(0, 0, 5, 5);
		State stateReduced = new StateReduced(0, 0, 5, 5);
		Agent prey         = new PreySimple();

		long timeStartSimple = System.nanoTime();
		new ValueIteration(stateSimple, prey, THETA, GAMMA);
		long timeEndSimple = System.nanoTime();
		
		System.out.println("Execution time of the Value Iteration algorithm for StateSimple: " + (timeEndSimple - timeStartSimple) / 1000000L + " ms.");
		
		long timeStartReduced = System.nanoTime();
		ValueIteration viReduced = new ValueIteration(stateReduced, prey, THETA, GAMMA);
		long timeEndReduced = System.nanoTime();
		
		System.out.println("Execution time of the Value Iteration algorithm for StateReduced: " + (timeEndReduced - timeStartReduced) / 1000000L + " ms.");
		
		Agent predator = viReduced.buildAgent();
		
		int turns = Simulator.runSimulation(stateReduced, prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
