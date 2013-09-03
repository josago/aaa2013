package assignment1;

public class EnvironmentSimple implements Environment
{	
	private int predatorX, predatorY, preyX, preyY;
	
	public EnvironmentSimple(int predatorX, int predatorY, int preyX, int preyY)
	{
		this.predatorX = predatorX;
		this.predatorY = predatorY;
		this.preyX = preyX;
		this.preyY = preyY;
	}

	@Override
	public int getPredatorX()
	{
		return predatorX;
	}

	@Override
	public int getPredatorY()
	{
		return predatorY;
	}

	@Override
	public int getPreyX()
	{
		return preyX;
	}

	@Override
	public int getPreyY()
	{
		return preyY;
	}
	

	@Override
	public void movePredator(int mov)
	{
		if (mov % 2 == 0)
		{
			predatorX = (predatorX + mov / 2 + ENVIRONMENT_SIZE) % ENVIRONMENT_SIZE;
		}
		else
		{
			predatorY = (predatorY + mov + ENVIRONMENT_SIZE) % ENVIRONMENT_SIZE;
		}
	}

	@Override
	public void movePrey(int mov)
	{
		if (mov % 2 == 0)
		{
			preyX = (preyX + mov / 2 + ENVIRONMENT_SIZE) % ENVIRONMENT_SIZE;
		}
		else
		{
			preyY = (preyY + mov + ENVIRONMENT_SIZE) % ENVIRONMENT_SIZE;
		}
	}
}
