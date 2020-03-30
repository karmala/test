package bbn;


import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public abstract class BBNDecision 
{
	private BBNBuilder bbnCreator = BBNBuilder.getInstance();
    private Net theNet = bbnCreator.getTheBayesianNet();
    
//test
    
    public void feedDataToBBN(Agent farmer) throws NeticaException 
    {
    	
    	Parameters params = RunEnvironment.getInstance().getParameters();
    	boolean isOneYearBBN = (boolean) params.getValue("isOneYearBBN");
    	
        if(!isOneYearBBN)
        {
           Node ESI_SCND = theNet.getNode("ESI_SCND");
           Node ESI_FRST = theNet.getNode("ESI_FRST");
           
           Node PES = theNet.getNode("PES");
           Node Opinion = theNet.getNode("Opinion");
           Node HHSize = theNet.getNode("HHSize");
           Node SelfEff_Peers = theNet.getNode("SelfEff_Peers");
           Node SelfEff_Own = theNet.getNode("SelfEff_Own");
           Node NrContacts = theNet.getNode("NrContacts");
           Node PercPast = theNet.getNode("PercPast");
           Node ESI_INCR = theNet.getNode("ESI_INCR");
           
           theNet.compile();
           theNet.retractFindings();
           
           Opinion.finding().enterReal(farmer.getOpinion());
           PES.finding().enterState(farmer.getPES());
           ESI_SCND.finding().enterReal(farmer.getESI_SCND());
           ESI_FRST.finding().enterReal(farmer.getESI_FRST());
           HHSize.finding().enterReal(farmer.getHHSize());
           SelfEff_Peers.finding().enterReal(farmer.getSelfEff_Peers());
           NrContacts.finding().enterReal(farmer.getNrContacts());
           PercPast.finding().enterReal(farmer.getPercPast());
          
        }
        else
        {
        	Node ESI_LAST = theNet.getNode("ESI_LAST");
        	Node PRJEAR = theNet.getNode("PRJEAR");
        	
            Node PES = theNet.getNode("PES");
            Node Opinion = theNet.getNode("Opinion");
            Node HHSize = theNet.getNode("HHSize");
            Node SelfEff_Peers = theNet.getNode("SelfEff_Peers");
            Node SelfEff_Own = theNet.getNode("SelfEff_Own");
            Node NrContacts = theNet.getNode("NrContacts");
            Node PercPast = theNet.getNode("PercPast");
            Node ESI_INCR = theNet.getNode("ESI_INCR");
                       
            theNet.compile();
            theNet.retractFindings();
            
            Opinion.finding().enterReal(farmer.getOpinion());
            PES.finding().enterState(farmer.getPES());
            ESI_LAST.finding().enterReal(farmer.getESI_LAST());
            PRJEAR.finding().enterReal(farmer.getPRJEAR());
            HHSize.finding().enterReal(farmer.getHHSize());
            SelfEff_Peers.finding().enterReal(farmer.getSelfEff_Peers());
            NrContacts.finding().enterReal(farmer.getNrContacts());
            PercPast.finding().enterReal(farmer.getPercPast());
           
        }


//        System.out.println("number of levels :"+ESI_INCR.getLevels().length);
//        System.out.println("level 1 :"+ESI_INCR.getLevels()[0]);
//        System.out.println("level 2 :"+ESI_INCR.getLevels()[1]);
//        System.out.println("level 3 :"+ESI_INCR.getLevels()[2]);
//        System.out.println("level 4 :"+ESI_INCR.getLevels()[3]);
//        System.out.println("level 5 :"+ESI_INCR.getLevels()[4]);
//        System.out.println("level 6 :"+ESI_INCR.getLevels()[5]);
        


//        farmer.setOpinion(1);
//        farmer.setSelfEff_Peers(0.5);
//        farmer.setPES(0);
//        
//        System.out.println(" famer id/opinion from BBNDecision : "+farmer.getId()+" /  "+farmer.getOpinion());
//        System.out.println(" famer PES from BBNDecision : "+farmer.getPES());
//        System.out.println(" famer ESI_FRST from BBNDecision : "+farmer.getESI_FRST());
//        System.out.println(" famer ESI_SCND from BBNDecision : "+farmer.getESI_SCND());
//        System.out.println(" famer HHSIze from BBNDecision : "+farmer.getHHSize());
//        System.out.println(" famer SelfEff_Peers from BBNDecision : "+farmer.getSelfEff_Peers());
//        System.out.println(" famer NrContacts from BBNDecision : "+farmer.getNrContacts());
//        System.out.println(" famer PercPast from BBNDecision : "+farmer.getPercPast());
        

    }
    
    public abstract float[] getVectorsOfProbabilities(Agent farmer, String TargetNode);

    public abstract double getChosenDecision(Agent farmer, String targetNodeName, boolean isIni) ;

    public Net getTheNet() 
    {
        return theNet;
    }

	public BBNBuilder getBbnCreator() {
		return bbnCreator;
	}

	public void setBbnCreator(BBNBuilder bbnCreator) {
		this.bbnCreator = bbnCreator;
	}
    
    
    


}
