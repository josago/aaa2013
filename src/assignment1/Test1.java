package assignment1;

public class Test1
{
	public static void main(String[] args)
	{
		int turns = Simulator.runSimulation(250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
