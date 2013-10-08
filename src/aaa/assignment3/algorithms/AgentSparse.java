package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;

public class AgentSparse implements Agent
{
	private final int type;
	private final float valueInitial;
	private final HashMap<StateActionPair, Float> Q;
	
	public AgentSparse(int type, float valueInitial, HashMap<StateActionPair, Float> Q)
	{
		this.type         = type;
		this.valueInitial = valueInitial;
		this.Q            = Q;
	}
	
	@Override
	public int getType()
	{
		return type;
	}

	@Override
	public String getSymbol()
	{
		if (getType() == Agent.TYPE_PREDATOR)
		{
			return "●";
		}
		else
		{
			return "○";
		}
	}

	@Override
	public float pi(State env, int action)
	{
		StateActionPair sa = new StateActionPair(env, action);
		
		int   optimalActions = 0;
		float valueAction    = valueInitial;
		
		try
		{
			valueAction = Q.get(sa);
		}
		catch (NullPointerException ex)
		{
			
		}
		
		for (int possibleAction: State.AGENT_ACTIONS)
		{
			sa = new StateActionPair(env, possibleAction);
			
			float valuePossibleAction = valueInitial;
			
			try
			{
				valuePossibleAction = Q.get(sa);
			}
			catch (NullPointerException ex)
			{
				
			}
				
			if (valuePossibleAction > valueAction)
			{
				return 0;
			}
			else if (valuePossibleAction == valueAction)
			{
				optimalActions++;
			}
		}
		
		return 1.0f / optimalActions;
	}
}
