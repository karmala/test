package bbn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import norsys.netica.NeticaException;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;


//sdfdsfsfsdf
public class Agent 
{
	
	private Collection<Agent> Predecessors = new ArrayList<Agent>();
	private Collection<Agent> Succesors = new ArrayList<Agent>();
	
	
	private int PES;
	private double ESI_INCR;
	private double ESI_SCND;
	private double ESI_FRST;
	private double ESI_LAST;
	private double PercPast;
	private double HHSize;
	private double SelfEff_Peers;
	private double UpSelfEff_Peers;
	private double SelfEff_Own;
	private double NrContacts;
	private double PRJEAR=1;
	
	private double nxtEsiIncr;
	
	private String hhId;
	private double id;
	
	///Opinion
	private double upOpinion;//temporary holder until all agents have looped through other agents' opinions
	private double opinion;
	private double d=1-2*(Math.abs(opinion-0.5));
	
	
	
	public Agent(String id)
	{
		hhId=id;
		this.id=Double.parseDouble(id);
	}
/////////////////////////////INI///////////////////////////////////////////////////////////////	
	public void setAttributes(Map<String, List<Double>> amap)
	{
		List<Double> alist = amap.get(hhId);
		ESI_FRST = alist.get(1);//TODO check if this is really an ESI and not an ESI_incr!!!!!!
		ESI_LAST = alist.get(1);
		ESI_SCND = alist.get(2);
		opinion = alist.get(3);
		PES = alist.get(4).intValue();
		HHSize = alist.get(5);
		SelfEff_Own =alist.get(6);
		PercPast=alist.get(7);
		SelfEff_Peers=alist.get(8);
		UpSelfEff_Peers=alist.get(8);
		NrContacts=alist.get(9);	
	}
	
	public void setContacts()
	{
		Network aNet = (Network)RunState.getInstance().getMasterContext().getProjection("FileNetwork");
		this.Predecessors =   (Collection) aNet.getPredecessors(this);
		this.Succesors =   (Collection) aNet.getSuccessors(this);
		
		for(Agent anAgenta: this.Predecessors)
		{
			System.out.println("Predecessor : "+anAgenta.getHhId());			
		}
	}
	
//////////////////////////BBN probability readers/////////////////////////////////////////////
	private int Cat_calculator( float[] listOfProbs)
	{	
		int cat=999;
		double x1= listOfProbs[0];
		double x2 = x1+listOfProbs[1];
		double x3 = x2+listOfProbs[2];
		double x4 = x3+listOfProbs[3];

		double randomDraw = RandomHelper.createUniform(0, 1).nextDouble();
		if(randomDraw<x1)
			cat=1;
		else if(randomDraw>=x1&&randomDraw<x2)
			cat=2;
		else if(randomDraw>=x2&&randomDraw<x3)
			cat=3;
		else if(randomDraw>=x3&&randomDraw<x4)
			cat=4;
		else if(randomDraw>=x4)
			cat=5;
		else
			System.out.println("no category found! randomdraw was :"+randomDraw+"cat was :"+cat);
		return cat;
	}
	
	private double prob_calculator_cont(int category,double [] levels)
	{
		double prob=999;
		if(category==1)
			prob=RandomHelper.getUniform().nextDoubleFromTo(levels[0], levels[1]);
		if(category==2)
			prob=RandomHelper.getUniform().nextDoubleFromTo(levels[1], levels[2]);
		if(category==3)
			prob=RandomHelper.getUniform().nextDoubleFromTo(levels[2], levels[3]);
		if(category==4)
			prob=RandomHelper.getUniform().nextDoubleFromTo(levels[3], levels[4]);
		if(category==5)
			prob=RandomHelper.getUniform().nextDoubleFromTo(levels[4], levels[5]);
		return prob;
	}
	
	private double prob_calculator_descr(int category,double [] levels)
	{
		double prob=999;
		if(category==1)
			prob=levels[0];
		if(category==2)
			prob=levels[1];
		if(category==3)
			prob=levels[2];
		if(category==4)
			prob=levels[3];
		if(category==5)
			prob=levels[4];
		return prob;
	}

///////////////////////////////////BBN data feeder/reciever/////////////////////////////////////////
	
//	public void setTheNextEsiIncr(BBNDecision theBBNDecision,boolean isIni)
//	{
//		double [] ESI_INCR_Levels = null;
//		try {
//			ESI_INCR_Levels = theBBNDecision.getTheNet().getNode("ESI_INCR").getLevels();
//		} catch (NeticaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		float [] listOfProbs = theBBNDecision.getVectorsOfProbabilities(this,"ESI_INCR",isIni);////FEEDS DATA TO BBN!!!!
//		int mostLikelyCategory = Cat_calculator(listOfProbs);
//		nxtEsiIncr = prob_calculator_cont(mostLikelyCategory,ESI_INCR_Levels);
//	}
//	
//	public void updateSelfEfficOwn(BBNDecision theBBNDecision,boolean isIni)
//	{
//		double [] SelfEff_Levels = null;
//		try {
//			SelfEff_Levels = theBBNDecision.getTheNet().getNode("SelfEff_Own").getLevels();
//		} catch (NeticaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		float [] listOfProbs = theBBNDecision.getVectorsOfProbabilities(this,"SelfEff_Own",isIni);////FEEDS DATA TO BBN!!!!
//		int mostLikelyCategory = Cat_calculator(listOfProbs);
//		double newSelfEffOwn = prob_calculator_cont(mostLikelyCategory,SelfEff_Levels);
//		
//		System.out.println("old self eff : "+SelfEff_Own+" updated self eff : "+newSelfEffOwn);
//		
//		UpSelfEff_Own=newSelfEffOwn;
//	}
	
	public void BBNstep(BBNDecision theBBNDecision)
	{
		double [] ESI_INCR_Levels = null;
		double [] SelfEff_Levels = null;
		
		try {
			ESI_INCR_Levels = theBBNDecision.getTheNet().getNode("ESI_INCR").getLevels();
			SelfEff_Levels = theBBNDecision.getTheNet().getNode("SelfEff_Own").getLevels();
		} catch (NeticaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		float [] listOfProbsSelfEff = theBBNDecision.getVectorsOfProbabilities(this,"SelfEff_Own");////FEEDS DATA TO BBN!!!!
		int mostLikelyCategorySelfEff = Cat_calculator(listOfProbsSelfEff);
		double newSelfEffOwn = prob_calculator_cont(mostLikelyCategorySelfEff,SelfEff_Levels);
		System.out.println("old self eff : "+SelfEff_Own+" updated self eff : "+newSelfEffOwn);
		SelfEff_Own=newSelfEffOwn;
		
		float [] listOfProbsEsiIncr = theBBNDecision.getVectorsOfProbabilities(this,"ESI_INCR");////FEEDS DATA TO BBN!!!!
		int mostLikelyCategoryEsiIncr = Cat_calculator(listOfProbsEsiIncr);
		ESI_INCR = prob_calculator_cont(mostLikelyCategoryEsiIncr,ESI_INCR_Levels);	
	}
	
	
	
	
///////////////////////////////////ESI computations/////////////////////////////////////////////////	
	/**
	 * sets new ESIs in Agent according to the latest ESI_incr
	 */
	public void computeTheNewESIs()
	{
    	Parameters params = RunEnvironment.getInstance().getParameters();
    	boolean isOneYearBBN = (boolean) params.getValue("isOneYearBBN");
    	
    	if(!isOneYearBBN)
    	{
    		ESI_FRST=ESI_SCND;
    		
    		double newESISCND = ESI_SCND+ESI_INCR;
    		if(newESISCND<2)
    			ESI_SCND=newESISCND;
    		else
    			ESI_SCND=2;
    	}
    	else
    	{
    		double newESILAST = ESI_LAST+ESI_INCR;
    		if(newESILAST<2)
    			ESI_LAST=newESILAST;
    		else
    			ESI_LAST=2;
    	}

	}
		
//////////opinion dynamics//////////////////////////////////////////////////////////////
	
	private void updateCertainty()
	{
		d=1-2*(Math.abs(opinion-0.5));
	}
	
	public void updateOpinion(boolean isAllContacts)
	{					
		double opinionsPeers=999;
	
		ArrayList<Agent> allContacts = new ArrayList<Agent>();
		allContacts.addAll(Succesors);
		allContacts.addAll(Predecessors);
		
		double sum = 0;
		if(!isAllContacts)
		{		
			if(!Succesors.isEmpty())
			{
				for(Agent anAgent: Succesors)
				{
					sum=sum+anAgent.getOpinion();		
				}
				opinionsPeers=sum/Succesors.size();
				computeOpinion(opinionsPeers);
			}
		}
		else
		{
			if(!allContacts.isEmpty())
			{
				for(Agent anAgent: allContacts)
				{
					sum=sum+anAgent.getOpinion();		
				}
				opinionsPeers=sum/allContacts.size();
				computeOpinion(opinionsPeers);
			}
		}		
	}
	
	public void setUpdatedOpinion()
	{
		opinion=upOpinion;
	}
	
	private void computeOpinion(double opinionsPeers)
	{
		System.out.println("oldOpinion : "+opinion);
		double firstOpinion=opinion;
		updateCertainty();
		if(Math.abs(opinionsPeers-opinion)<=d)
		{
			double deltaX = SelfEff_Own * (opinionsPeers-opinion);
			firstOpinion = round(opinion + deltaX,2);
			if(firstOpinion<=0)
				firstOpinion=0;
			if(firstOpinion>=1)
				firstOpinion=1;
		}
		upOpinion=firstOpinion;
		System.out.println("newOpinion : "+upOpinion);
	}
	

/////////////////////Self-Efficacies////////////////////////////////////////////////////////	
	

	
	public void setUpdatedSelfEfficacyPeers()
	{
		SelfEff_Peers=UpSelfEff_Peers;
	}
	
	public void updateNewSelfEfficacyPeers(boolean isAllContacts)
	{
		ArrayList<Agent> allContacts = new ArrayList<Agent>();
		allContacts.addAll(Succesors);
		allContacts.addAll(Predecessors);

		double sum = 0;
		if(!isAllContacts)
		{		
			if(!Succesors.isEmpty())
			{
				for(Agent anAgent: Succesors)
				{
					sum=sum+anAgent.getSelfEff_Own();	
				}
				UpSelfEff_Peers=sum/Succesors.size();
			}
		}
		else
		{
			if(!allContacts.isEmpty())
			{
				for(Agent anAgent: allContacts)
				{
					sum=sum+anAgent.getSelfEff_Own();	
				}
				UpSelfEff_Peers=sum/allContacts.size();
			}
		}		
	}
	
//////////////////////////Time////////////////////////////////////////////////////////////
	
	public void incrPrjEar()
	{
		PRJEAR++;
	}
////////////////////////Getters & Setters///////////////////////////////////////////////	
	public double getOpinion() {
		return opinion;
	}

	public void setOpinion(double anOpinion) {
		this.opinion = anOpinion;
	}

	public int getPES() {
		return PES;
	}

	public void setPES(int aPES) {
		this.PES = aPES;
	}
	
	public void setATargetNodeProb(double prob)
	{
		ESI_INCR = prob;
	}
	
	public double getESI_INCR()
	{
		return ESI_INCR;
	}

	public double getESI_SCND() {
		return ESI_SCND;
	}

	public void setESI_SCND(double eSI_SCND) {
		ESI_SCND = eSI_SCND;
	}

	public double getESI_FRST() {
		return ESI_FRST;
	}

	public void setESI_FRST(double eSI_FRST) {
		ESI_FRST = eSI_FRST;
	}

	public double getPercPast() {
		return PercPast;
	}

	public void setPercPast(double percPast) {
		PercPast = percPast;
	}

	public double getHHSize() {
		return HHSize;
	}

	public void setHHSize(double hHSize) {
		HHSize = hHSize;
	}

	public double getSelfEff_Peers() {
		return SelfEff_Peers;
	}

	public void setSelfEff_Peers(double selfEff_Peers) {
		SelfEff_Peers = selfEff_Peers;
	}

	public double getSelfEff_Own() {
		return SelfEff_Own;
	}

	public void setSelfEff_Own(double selfEff_Own) {
		SelfEff_Own = selfEff_Own;
	}

	public double getNrContacts() {
		return NrContacts;
	}

	public void setNrContacts(double nrContacts) {
		NrContacts = nrContacts;
	}

	public String getHhId() {
		return hhId;
	}

	public void setHhId(String hhId) {
		this.hhId = hhId;
	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public void setESI_INCR(double eSI_INCR) {
		ESI_INCR = eSI_INCR;
	}
	
	
public double getESI_LAST() {
		return ESI_LAST;
	}
	public void setESI_LAST(double eSI_LAST) {
		ESI_LAST = eSI_LAST;
	}
	public double getPRJEAR() {
		return PRJEAR;
	}
	public void setPRJEAR(double pRJEAR) {
		PRJEAR = pRJEAR;
	}
	///////////////////////////////////Utility/////////////////////////////////////////////////////	
	public double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
