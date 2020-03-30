package bbn;

import java.io.IOException;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.graph.NetworkFileFormat;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class Model extends DefaultContext implements ContextBuilder 
{
	private AgentSpace theAgentSpace;
	private Network <Agent>globalNetwork;

	@Override
	public Context build(Context context) 
	{
		context.setId("BBN_Test");
		theAgentSpace=new AgentSpace();
		context.add(theAgentSpace);
		
		@SuppressWarnings("rawtypes")
		NetworkBuilder fileNetBuilder = new NetworkBuilder("FileNetwork", context, true);
			try {
				fileNetBuilder.load("./graphQ38.xls", NetworkFileFormat.EXCEL, new MyNodeCreator(context));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			globalNetwork = fileNetBuilder.buildNetwork();
		
		//int simLenght = (Integer) RepastEssentials.GetParameter("simLenght");
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		ScheduleParameters initialStep = ScheduleParameters.createOneTime(0);
		schedule.schedule(initialStep, this, "initialStep");
		ScheduleParameters mainStep = ScheduleParameters.createRepeating(1, 1, 1);
		schedule.schedule(mainStep, this, "mainStep");
		ScheduleParameters endStep = ScheduleParameters.createAtEnd(2);
		schedule.schedule(endStep, this, "endStep");
		//RunEnvironment.getInstance().endAt(simLenght);	
		
		return context;
	}
	
	public void initialStep()
	{
		theAgentSpace.ini();
		theAgentSpace.displayBBN();
		theAgentSpace.bBNstep();
		theAgentSpace.computeOpinions();
		theAgentSpace.computeSelfEff_peers();
	}
	
	public void mainStep()
	{
		theAgentSpace.bBNstep();
		theAgentSpace.computeOpinions();
		theAgentSpace.computeSelfEff_peers();
	}
	
	public void endStep()
	{
		
	}

}
