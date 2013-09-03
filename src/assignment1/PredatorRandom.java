package assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PredatorRandom implements Predator
{
	@Override
	public void move(Environment env)
	{
		List<Integer> availableMoves = new ArrayList<Integer>();
		
		availableMoves.add(Environment.AGENT_MOVE_STAY);
		availableMoves.add(Environment.AGENT_MOVE_NORTH);
		availableMoves.add(Environment.AGENT_MOVE_SOUTH);
		availableMoves.add(Environment.AGENT_MOVE_EAST);
		availableMoves.add(Environment.AGENT_MOVE_WEST);
		
		Collections.shuffle(availableMoves);
		
		env.movePredator(availableMoves.get(0));
	}
}
