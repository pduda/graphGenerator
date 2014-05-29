package pl.waw.pduda.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import edu.asu.emit.qyan.alg.model.Pair;

public class SimpleGraphGenerator
{
    static int _nodesNum=0;
    static Random randomizer = new Random();
    
    static Map<Pair<Integer, Integer>, Integer> _edges = new Hashtable<Pair<Integer, Integer>, Integer>();
   
    
    public static void generateGraph(String file_name,int nodes,int edges,int max_routing_cost,boolean random_routing)
    {
	_nodesNum = nodes;

	while(_edges.size()<2*edges)//zeby od razu generowalo kierunkowe
	{
	    int s = randomizer.nextInt(nodes);
	    int e = randomizer.nextInt(nodes);
	    
	    Pair<Integer,Integer> temp1 = new Pair<Integer,Integer>(s,e);
	    Pair<Integer,Integer> temp2 = new Pair<Integer,Integer>(e,s);
		    
	    if(!_edges.containsKey(temp1))//dodaje od razu w dwie strony
	    {
		int tempCost =0;
		
		if(random_routing)
		    tempCost = randomizer.nextInt(max_routing_cost);
		else
		    tempCost = max_routing_cost;
		
		_edges.put(temp1,tempCost);
		_edges.put(temp2,tempCost);

	    }
	}
	
	saveToFile(file_name);
    }
    protected static String getStringRepresentation()
    {
	StringBuffer sb = new StringBuffer();
	
	for(Pair<Integer, Integer> cur_edge_pair : _edges.keySet())
	{
	    int starting_pt_id = cur_edge_pair.first();
	    int ending_pt_id = cur_edge_pair.second();
	    int weight = _edges.get(cur_edge_pair);
	    sb.append(starting_pt_id+" "+ending_pt_id+" "+weight+"\n");
	}
	sb.append("\n\n");
	return sb.toString();
    }
    
    private static void saveToFile(final String file_name)
    {
	StringBuffer sb = new StringBuffer();
	sb.append(_nodesNum+"\n\n");
	
	sb.append(SimpleGraphGenerator.getStringRepresentation());
	
	//2. open the file and put the data into the file. 
	Writer output = null;
	try {
		// use buffering
		// FileWriter always assumes default encoding is OK!
		output = new BufferedWriter(new FileWriter(new File(file_name)));
		output.write(sb.toString());
	}catch(FileNotFoundException e)
	{
		e.printStackTrace();
	}catch(IOException e)
	{
		e.printStackTrace();
	}finally {
		// flush and close both "output" and its underlying FileWriter
		try
		{
			if (output != null) output.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
    }

}
