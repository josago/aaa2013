package assignment1;

public class Test1
{
	public static void main(String[] args)
	{
		StateSimple env = new StateSimple(0, 0, 5, 5);
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		int turns = Simulator.runSimulation(env, prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
