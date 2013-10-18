package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;
import aaa.assignment3.*;
import lpsolve.*;

public class MiniMaxQLearning extends QLearningMulti
{
	public static final int NUM_EPISODES = 5100;
	
	public static final float Q_INITIAL = 1;
	
	private final HashMap<State, Float> V;
	private final HashMap<StateActionOpponent, Float> Q;
	
	public final HashMap<StateActionPair, Float> pi;
	
	
	private final float[][] qMatrix = new float [5][5];
	
	public MiniMaxQLearning(float epsilon, float decay, float alpha, float gamma)
	{
		V = new HashMap<State, Float>();
		Q = new HashMap<StateActionOpponent, Float>();
		
		pi = new HashMap<StateActionPair, Float>();
		
		
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		List<Agent> predators = new ArrayList<Agent>();
		predators.add(predator);
		
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		// Q-Learning:
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			StateMulti s = (StateMulti) env.clone();

			int x = 0;
			
			while (!s.isFinal() && x < SimulatorMulti.TURNS_LIMIT)
				
				
			{
				
				
				
				s.changeViewPoint(predator);
				int action   = getAction(s, epsilon, predator);
				
				s.changeViewPoint(prey);
				int opponent = getAction(s, epsilon, prey);
				
				StateMulti sPrime = (StateMulti) s.clone();
				sPrime.move(predator, action);
				sPrime.move(prey,     opponent);
				
				int reward = sPrime.getReward(predator);
				

				s.changeViewPoint(predator);
				StateActionOpponent sao = new StateActionOpponent((StateMulti) s.clone(), action, opponent);

				
				if (!Q.containsKey(sao))
				{
					Q.put(sao, Q_INITIAL);
				}
				
				if (!V.containsKey(s))
				{
					V.put(s, Q_INITIAL);
				}
				
				if (!V.containsKey(sPrime))
				{
					V.put(sPrime, Q_INITIAL);
				}
				
				Q.put(sao, (1 - alpha) * Q.get(sao) + alpha * (reward + gamma * V.get(sPrime)));
				
				linearProg(s);
				
				alpha *= decay;
				
				s = sPrime;
				
				x++;
			}
			
		
			
				if(i % 100 == 0) performanceAdd(i, prey, predators);
		
		}
	}
	
	public Agent buildAgent( final Agent agent)
	{
		
		class mmAgent implements Agent{
			private  HashMap<StateActionPair, Float> pi2 = new HashMap<StateActionPair, Float>();
			
			public mmAgent(HashMap<StateActionPair, Float> pi){
				this.pi2 = pi;
			}
			
			@Override
			public int getType() {
				// TODO Auto-generated method stub
				return agent.getType();
			}

			@Override
			public String getSymbol() {
				// TODO Auto-generated method stub
				return agent.getSymbol();
			}

			@Override
			public float pi(State env, int action) {
				
				
				StateActionPair sa = new StateActionPair((State)env.clone(), action);
			
				if (pi.containsKey(sa)){
						if (agent.getType()== Agent.TYPE_PREDATOR) return pi2.get(sa); 
						else return (1-pi2.get(sa))/4.0f;
					}
				else{
					pi.put(sa, 0.2f);
					return 0.2f;
				}
				
			    
				
			
				}
				
				
			}
			

	
	
		return new mmAgent(pi);
	}
	
	private int getAction(State s, float epsilon, Agent agent)
	{
		float random = (float) Math.random();
		
		if (random < epsilon) // Random action:
		{
			float sum = 0;
			
			for (int action: State.AGENT_ACTIONS)
			{
				sum += epsilon / State.AGENT_ACTIONS.length;
					
				if (sum > random)
				{
					return action;
				}
			}
		}
		else // Random action following pi(s):
		{
			float sum = epsilon;
			
			for (int action: State.AGENT_ACTIONS)
			{
				StateActionPair sa = new StateActionPair((State) s.clone(), action);
				
				if (!pi.containsKey(sa))
				{
					pi.put(sa, 1.0f / State.AGENT_ACTIONS.length);
				}
				
				if (agent.getType() == Agent.TYPE_PREDATOR)
				{
					sum += (1 - epsilon) * pi.get(sa);
				}
				else // TYPE_PREY:
				{
					sum += (1 - epsilon) * (1 - pi.get(sa))/4.0f;
				}
					
				if (sum > random)
				{
					return action;
				}
			}
		}
		
		return State.AGENT_MOVE_STAY;
	}
	
	private void linearProg(State s)
	{
		
	
		if (!V.containsKey(s))
		{
			V.put(s, Q_INITIAL);
		}
	
			
			int i = 0;
			int j = 0;
			
			
			
			for (int opponent: State.AGENT_ACTIONS)
			{
				
				for (int action: State.AGENT_ACTIONS)
				{
					StateActionPair sa = new StateActionPair((State) s.clone(), action);
					
					if (!pi.containsKey(sa))
					{
						pi.put(sa, 1.0f / State.AGENT_ACTIONS.length);
					}
					
					StateActionOpponent sao = new StateActionOpponent((State) s.clone(), action, opponent);
					
					if (!Q.containsKey(sao))
					{
						Q.put(sao, Q_INITIAL);
					}
					
					
					
					qMatrix[j][i] = Q.get(sao);
					
					
					j++;
					
				}
				
				
				
				j = 0;
				i++;
			}
			
			
			try{
				
				miniMax(s);
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
			
			
			
		
	}
	
	public void miniMax(State s) throws LpSolveException{
		
			 
		
		  //System.out.println(qMatrix[0]);
		  LpSolve lp;
          int Ncol, j, ret = 0;
          float coef = 0;
          
          Ncol = 6; 

          
   

          int[] colno = new int[Ncol];
          double[] row = new double[Ncol];

          lp = LpSolve.makeLp(0, Ncol);
          
          if(lp.getLp() == 0)
            ret = 1; 
          
          lp.setColName(1, "pi1");
          lp.setColName(2, "pi2");
          lp.setColName(3, "pi3");
          lp.setColName(4, "pi4");
          lp.setColName(5, "pi5");
          lp.setColName(6, "t");
      
          
          for (int i = 0; i < 5; i++){
          
          if(ret == 0) {
            
        	  
            
            
            lp.setAddRowmode(true);
            for (j = 0; j < 6; j++){
            		
            		if (j == 5) coef = -1;
            		else coef = qMatrix[j][i];
            		
            		

            		colno[j] = j+1; /* first column */
            		row[j] = coef ;
            
            		
          }
            
            lp.addConstraintex(j, row, colno, LpSolve.GE, 0);
          }
          }
          
          for (j = 0; j < 6; j++){
      		
      		if (j == 5) coef = 0;
      		else coef = 1;
      		
      		

      		colno[j] = j+1; /* first column */
      		row[j] = coef ;
      
      		
          	}
          
          lp.addConstraintex(j, row, colno, LpSolve.EQ, 1);
          
          if(ret == 0) {
              lp.setAddRowmode(false); 
              
              
              for (j = 0; j < 6; j++){
          		
          		if (j == 5) coef = 1;
          		else coef = 0;
          		

          		colno[j] = j+1; /* first column */
          		row[j] = coef ;
          
              }

         

              /* set the objective in lpsolve */
              lp.setObjFnex(j, row, colno);
            }
          if(ret == 0) {
         
              lp.setMaxim();

              lp.writeLp("modeladw.lp");
         
              
              lp.setVerbose(LpSolve.IMPORTANT);

              ret = lp.solve();
              
              if(ret == LpSolve.OPTIMAL)
                ret = 0;
              else
                ret = 5;
            }
          
           lp.getObjective();
          
          //lp.getVariables(row);
          double[] var = lp.getPtrVariables();
          
          for(j = 0; j < 5; j++){
        	pi.put(new StateActionPair((State) s.clone(), State.AGENT_ACTIONS[j]), (float) var[j]);
            //System.out.println(lp.getColName(j + 1) + ": " + var[j] );
          }
          
          float min = Float.POSITIVE_INFINITY; 
          float sum = 0;
            
          for(int i = 0; i < 5; i++){
        	  sum = 0;
  
        	  for(j = 0; j < 5; j++)
        	  {
        		sum += var[j] * qMatrix[j][i]; 
        		
        	  }
        	  if (sum < min) min = sum;
          }
          
          V.put(s,  min);
        	  
          if(lp.getLp() != 0)
              lp.deleteLp();
          
          
          
            

	}
}
