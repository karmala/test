package bbn;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NodeCreator;
import repast.simphony.random.RandomHelper;

@SuppressWarnings("rawtypes")
public class MyNodeCreator implements NodeCreator
{
	private Context myContext;
	
	public MyNodeCreator(Context theContext)
	{
		myContext=theContext;
	}

	@Override
	public Object createNode(String label) 
	{
		Agent aTestAgent = new Agent(label);
		myContext.add(aTestAgent);
		//aTestAgent.printId();
		return aTestAgent;
	}

}
