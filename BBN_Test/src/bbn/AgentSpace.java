package bbn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunState;
import repast.simphony.util.collections.IndexedIterable;

public class AgentSpace 
{
	//private ArrayList<Agent> agents = new ArrayList<Agent>();
	private BBNDecision theBBNDecision;
	private IndexedIterable<Agent> agents; 
	
	public AgentSpace()
	{
		
		
	}
	
	public void ini()
	{
		Map<String, List<Double>> amap = null;
		try {
			amap = ReaderHelper.getDataMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		agents = RunState.getInstance().getMasterContext().getObjects(Agent.class);
		for(Agent aFarmer: agents)
		{
			aFarmer.setAttributes(amap);
		}
		theBBNDecision=new BBNDecisionImpl();
		for(Agent aFarmer: agents)
		{
			aFarmer.setContacts();
		}
	}
	
	public void displayBBN()
	{
		try {
			theBBNDecision.getBbnCreator().displayNet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void bBNstep()
	{
		for(Agent anAgent:agents )
		{
			anAgent.BBNstep(theBBNDecision);//takes new ESIs past, Opinion and Self-eff_peers as BBN inputs and gives ESI_incr and Self-eff_own as output
			anAgent.computeTheNewESIs();
		}	
	}
	
	public void computeOpinions()
	{
		for(Agent anAgent:agents )
		{
			anAgent.updateOpinion(false);
		}	
		for(Agent anAgent:agents )
		{
			anAgent.setUpdatedOpinion();//Sets new opinions based on changed other's opinions (from last ABM step) and changed Self-efficacy (from last BBN step)
		}
	}
	
	public void computeSelfEff_peers ()
	{
		for(Agent anAgent:agents )
		{
			anAgent.updateNewSelfEfficacyPeers(false);
		}	
		for(Agent anAgent:agents )
		{
			anAgent.setUpdatedSelfEfficacyPeers();//Sets new Self-eff_peers from other agent's Self-eff_own
		}
	}
	
//	public void computeESI_INCR(boolean isIni)
//	{
//		//System.out.println("prob of cat 1:"+theBBNDecision.getChosenDecision(agents.get(0))+"for agent with id :"+agents.get(0).getId());	
//		for(Agent anAgent:agents )
//		{
//			anAgent.setTheNextEsiIncr(this.theBBNDecision,isIni);	
//			anAgent.computeTheNewESIs();
//		}			
//	}
//	

}
