package bbn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public  class  ReaderHelper 
{

	
	/**
	 * reads and csv file containing all participant data. Csv file can have and nr of rows and columns
	 * @return a double [][] with participant data in matrix form
	 * @throws IOException
	 */
	public static double [][] readAllData () throws IOException
	{
		//Read the file
		BufferedReader CSVFile =  new BufferedReader(new FileReader(getFullPathName()));
		//Read the first line
		String dataRow = CSVFile.readLine();
		
		//initilize local variables
		int rows =0;
		int helper = 0;
		int cols =0;
		
		//first calculate the amount of rows and colums of the file
		while (dataRow != null)
		  {
			String[] dataArray = dataRow.split(";");//get array of cells of one row in the file
			   for (String item:dataArray) 
				   { 			   	  
				   	  helper++;//count the cells of the line
				   }
			   rows++;//count the rows 
			   dataRow = CSVFile.readLine();//read in the next line
		  }
		cols=helper/rows;//colums are all cells (with values) devided by the number of rows
		int agents = rows-1;//first row is a string in the file which means it is not an agent
		
//System.out.println("Agents : "+agents+" Attributes : "+cols);
		
		//read the file again to start from the top with readLine()
		CSVFile =  new BufferedReader(new FileReader(getFullPathName()));
		dataRow = CSVFile.readLine();//first line are headers
		dataRow = CSVFile.readLine();//second line is beginning of data
		
		double [][] data = new double [rows][cols];// first row is for meta data in the new array matrix
		data[0][0]=agents;// Number of participants
		data[0][1]=cols;// Number of attributes
		int i=1;// not zero because is second line (first line is for meta data)s
		int j=0;
		//get the data in 
		while (dataRow != null)
		  {	
			   String[] dataArray = dataRow.split(";");
			   for (String item:dataArray) 
				   { 
						   data[i][j]=Double.parseDouble(item);
						   j++;
				   }
			   j=0;
			   i++;
			   dataRow = CSVFile.readLine();
		  }
		CSVFile.close();
		return data;
	}

	
	public static Map<String, List<Double>> getDataMap() throws IOException
	{
		Map<String, List<Double>> agentAttributes = new HashMap<String, List<Double>>();  
		BufferedReader CSVFile =  new BufferedReader(new FileReader(getFullPathName()));
		//Read the first line
		String dataRow = CSVFile.readLine();
		while (dataRow != null)
		  {	
			  List<Double> vals = new ArrayList<Double>();
			   String[] dataArray = dataRow.split(";");
			   String key = dataArray[0];
			   for(int i=1;i<dataArray.length;i++)
			   {
				   vals.add((new Double(dataArray[i])));
			   }
			   agentAttributes.put(key, vals);
			   dataRow = CSVFile.readLine();
		  }
		CSVFile.close();
		
		//List<Double> alist = agentAttributes.get("GH3");
//		System.out.println("gh2 first arg : "+alist.get(0));
//		System.out.println("gh2 second arg : "+alist.get(1));
		return agentAttributes;
	}
	

	
	private static String getFullPathName()
	{
		return "./BBN_09_data.csv";
	}
}
