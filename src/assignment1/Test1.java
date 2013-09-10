package assignment1;

public class Test1
{
	public static void main(String[] args)
	{
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		int turns = Simulator.runSimulation(prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
