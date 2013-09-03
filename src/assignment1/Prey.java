package assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Prey
{
	public static final String PREY_SYMBOL = "○";
	
	public void move(Environment env)
	{
		double r = Math.random();
		
		if (r <= 0.8)
		{
			env.movePrey(Environment.AGENT_MOVE_STAY);
		}
		else
		{
			List<Integer> availableMoves = new ArrayList<Integer>();
			
			if (env.getPredatorX() != (env.getPreyX() + 1 + Environment.ENVIRONMENT_SIZE) % Environment.ENVIRONMENT_SIZE || env.getPredatorY() != env.getPreyY())
			{
				availableMoves.add(Environment.AGENT_MOVE_EAST);
			}
			
			if (env.getPredatorX() != (env.getPreyX() - 1 + Environment.ENVIRONMENT_SIZE) % Environment.ENVIRONMENT_SIZE || env.getPredatorY() != env.getPreyY())
			{
				availableMoves.add(Environment.AGENT_MOVE_WEST);
			}
			
			if (env.getPredatorY() != (env.getPreyY() + 1 + Environment.ENVIRONMENT_SIZE) % Environment.ENVIRONMENT_SIZE || env.getPredatorX() != env.getPreyX())
			{
				availableMoves.add(Environment.AGENT_MOVE_SOUTH);
			}
			
			if (env.getPredatorY() != (env.getPreyY() - 1 + Environment.ENVIRONMENT_SIZE) % Environment.ENVIRONMENT_SIZE || env.getPredatorX() != env.getPreyX())
			{
				availableMoves.add(Environment.AGENT_MOVE_NORTH);
			}
			
			Collections.shuffle(availableMoves);
			
			env.movePrey(availableMoves.get(0));
		}
	}
}
