package assignment1;

public class PreySimple implements Agent
{
	@Override
	public int getType()
	{
		return TYPE_PREY;
	}

	@Override
	public String getSymbol()
	{
		return "○";
	}

	@Override
	public float pi(State env, int action)
	{
		boolean allActions = checkAction(env, ACTION_NORTH)
				          && checkAction(env, ACTION_SOUTH)
				          && checkAction(env, ACTION_EAST)
				          && checkAction(env, ACTION_WEST);
		
		switch (action)
		{
			case ACTION_STAY:
				return 0.8f;
			default:
				if (allActions)
				{
					return 0.2f / 4;
				}
				else if (checkAction(env, action))
				{
					return 0.2f / 3;
				}
				else
				{
					return 0;
				}
		}
	}
	
	private boolean checkAction(State env, int action)
	{
		State temp = (State) env.clone();
		
		temp.move(this, action);
		
		return !temp.isFinal();
	}
}
