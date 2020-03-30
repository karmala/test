package bbn;

import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Streamer;
import norsys.netica.gui.NetPanel;
import norsys.netica.gui.NodePanel;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

import javax.swing.*;

public class BBNBuilder 
{
	private static BBNBuilder instance = null;
	
	/**
     * The bayesian net.
     */
    private Net theBayesianNet;
    /**
     * Instantiates a new bayesian belief network.
     */
    
    
    private BBNBuilder() 
    {
        try 
        {
            Environ environ = new Environ("+HeckleiT/UBonn/310-7-A/1303");//where is that going???
        	Parameters params = RunEnvironment.getInstance().getParameters();
        	boolean isOneYearBBN = (boolean) params.getValue("isOneYearBBN");
        	if(!isOneYearBBN)
        		theBayesianNet = new Net(new Streamer("BBN_09_02.dne"));
        	else
        		theBayesianNet = new Net(new Streamer("BBN_alternative_threeYears_02.dne"));
        } 
        catch (NeticaException e) 
        {
            e.printStackTrace();
        }
    } 
    
    public static BBNBuilder getInstance() 
    {
        if (instance == null) 
        {
            synchronized(BBNBuilder.class) //Bisrat uses synchronized. He has to teach me that!
            {
                instance = new BBNBuilder();
            }
        }
        return instance;
    }
    
    /**
     * Display net.
     *
     * @throws Exception the exception
     */
    public void displayNet() throws Exception 
    {
        JFrame frame = new JFrame();
        theBayesianNet.compile();
        NetPanel netPanel = new NetPanel(theBayesianNet, NodePanel.NODE_STYLE_BELIEF_BARS);
        frame.getContentPane().add(new JScrollPane(netPanel));
        netPanel.setLinkPolicy(NetPanel.LINK_POLICY_BELOW);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        netPanel.refreshDataDisplayed();
    }
    
    public NetPanel getJpanel() throws Exception 
    {
        theBayesianNet.compile();
        NetPanel netPanel = new NetPanel(theBayesianNet, NodePanel.NODE_STYLE_BELIEF_BARS);
        netPanel.setLinkPolicy(NetPanel.LINK_POLICY_BELOW);
        netPanel.refreshDataDisplayed();

        return netPanel;
    }
    
    /**
     * Gets the the bayesian net.
     *
     * @return the the bayesian net
     */
    public Net getTheBayesianNet() 
    {
        return theBayesianNet;
    }

    /**
     * Sets the the bayesian net.
     *
     * @param theBayesianNet the new the bayesian net
     */
    public void setTheBayesianNet(Net theBayesianNet) 
    {
        this.theBayesianNet = theBayesianNet;
    }
    
}
