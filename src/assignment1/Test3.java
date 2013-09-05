package assignment1;

public class Test3
{
	public static final float THETA = 0.000000001f;
	public static final float GAMMA = 0.8f;
	
	public static void main(String[] args)
	{
		State stateInitial = new StateSimple(0, 0, 5, 5);
		
		Agent predator = new PredatorRandom();
		Agent prey     = new PreySimple();
		
		IterativePolicyEvaluation ipe = new IterativePolicyEvaluation(stateInitial, predator, prey, THETA, GAMMA);
		
		System.out.println(ipe.getEvaluation(new StateSimple(0, 0, 5, 5)));
		System.out.println(ipe.getEvaluation(new StateSimple(2, 3, 5, 4)));
		System.out.println(ipe.getEvaluation(new StateSimple(2, 10, 10, 0)));
		System.out.println(ipe.getEvaluation(new StateSimple(10, 10, 0, 0)));

		System.out.println("The IPE algorithm converged in " + ipe.getIterations() + " iterations.");
	}

}
