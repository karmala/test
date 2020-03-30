package bbn;

import java.util.List;
import java.util.Objects;


import norsys.netica.NeticaException;
import norsys.netica.Node;



public class BBNDecisionImpl extends BBNDecision
{


	@Override
	  public float[] getVectorsOfProbabilities(Agent farmer, String targetNodeName) 
	{
		Node targetNode = null;
	    float[] targetNodeBeliefs = null;
	    
	    try 
	    {
	      feedDataToBBN(farmer);
	      targetNode = getTheNet().getNode(targetNodeName);
	      targetNodeBeliefs = targetNode.getBeliefs();

	    } catch (NeticaException e) 
	    {    
	    	System.out.println("Netica exception");
	    }

	    for (float f : Objects.requireNonNull(targetNodeBeliefs)) 
	    {
	      if (f < 0)
	        throw new IllegalArgumentException(
	            "Probability of target node category can not be a negative number");
	    }
	    //System.out.println("prob of cat 0 : "+targetNodeBeliefs[0]);
	    return targetNodeBeliefs;
	  }

	  @Override
	  public double getChosenDecision(Agent farmer, String targetNodeName, boolean isIni) 
	  {
	    float[] vectorsOfProbability = getVectorsOfProbabilities(farmer,targetNodeName);
	    //farmer.setATargetNodeProb(vectorsOfProbability[2]);
	    return  vectorsOfProbability[0];//the number is the probability of the respective node category starting with 0
	  }


	  
	 
}
