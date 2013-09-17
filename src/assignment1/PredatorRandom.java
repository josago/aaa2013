package assignment1;

/**
 * This class implements a predator that always moves randomly.
 * @author josago
 */
public class PredatorRandom implements Agent
{
	@Override
	public int getType()
	{
		return TYPE_PREDATOR;
	}

	@Override
	public String getSymbol()
	{
		return "●";
	}

	@Override
	public float pi(State env, int action)
	{
		return 0.2f;
	}
}
