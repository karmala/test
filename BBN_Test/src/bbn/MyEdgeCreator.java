package bbn;

import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.RepastEdge;

public class MyEdgeCreator implements EdgeCreator
{

	@Override
	public Class getEdgeType() {
		// TODO Auto-generated method stub
		return Agent.class;
	}

	@Override
	public RepastEdge createEdge(Object source, Object target,boolean isDirected, double weight) 
	{
		// TODO Auto-generated method stub
		return new RepastEdge(source,target,false);
	}
	

}
