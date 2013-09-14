package assignment1;

import java.util.HashMap;
import java.util.List;

public class AgentUtils
{
	public static Agent buildPredator(HashMap<State, List<Integer>> pi)
	{
		class PredatorCustom implements Agent
		{
			private final HashMap<State, List<Integer>> pi;
			
			private PredatorCustom(HashMap<State, List<Integer>> pi)
			{
				this.pi = pi;
			}

			@Override
			public int getType()
			{
				return Agent.TYPE_PREDATOR;
			}

			@Override
			public String getSymbol()
			{
				return "●";
			}

			@Override
			public float pi(State env, int action)
			{
				List<Integer> actions = pi.get(env);

				if (actions.contains(action))
				{
					return 1f / actions.size();
				}
				else
				{
					return 0;
				}
			}
		}
		
		return new PredatorCustom(pi);
	}
}
