package pl.waw.pduda.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import edu.asu.emit.qyan.alg.model.Pair;

public class AMPLSaver
{
    public ArrayList<KeyPath> paths;
    public Map<String,Node> nodes = new Hashtable<String,Node>();
    public Map<Pair<String, String>,Integer> demands = new Hashtable<Pair<String, String>,Integer>();
    public Map<Pair<String, String>, Pair<Integer, Integer>> edges = new Hashtable<Pair<String, String>, Pair<Integer, Integer>>();
    public NodesTranslator trans;
    
    private String getLinksSET()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("set LINKS := ");
	
	for(Pair<String,String> pair : edges.keySet())
	{
	    sb.append("("+pair.first()+", "+pair.second()+") ");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getLinksCost()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("param:\tlink_cost :=\n");
	
	for(Pair<String,String> pair : edges.keySet())
	{
	    sb.append(pair.first()+" "+pair.second()+"\t"+edges.get(pair).second()+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getNodesSET()
    {
	StringBuffer sb = new StringBuffer();
	
	String apoints = "set APOINTS :=";
	String cabinets = "set CABINETS := ";
	String centrals = "set CLOCATIONS := ";
	String end = "set END := ";
	
	for(Node n: nodes.values())
	{
	    if(n.type == Node.AP)
		apoints += " "+n.id;
	    else if(n.type == Node.CABINET)
		cabinets += " "+n.id;
	    else if(n.type == Node.CENTRAL)
		centrals += " "+n.id;
	    else if(n.type == Node.END)
		end += " "+n.id;
	}
	
	sb.append(apoints+";\n");
	sb.append(cabinets+";\n");
	sb.append(centrals+";\n");
	sb.append(end+";\n");
	
	return sb.toString();
    }
    private String getNodesCost()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("param:\tnode_cost :=\n");
	
	for(Node n: nodes.values())
	{
	    sb.append(n.id+"\t"+n.instalation_cost+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getDemandsSET()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("set DEMANDS := ");
	
	for(Pair<String,String> pair : demands.keySet())
	{
	    sb.append("("+pair.first()+", "+pair.second()+") ");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getDemandsCost()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("param:\tdemand_val :=\n");
	
	for(Pair<String,String> pair : demands.keySet())
	{
	    sb.append(pair.first()+" "+pair.second()+"\t"+demands.get(pair)+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getPathsSET()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("set PATHS :=");
	
	for(KeyPath p : paths)
	{
	    sb.append(" "+ p.id);
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getPath_LinkSET()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("set PATH_LINK :=\n");
	
	for(KeyPath p : paths)
	{
	    sb.append(p.getEdgesKeyRepresentation(trans)+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getPath_NodeSET()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("set PATH_NODE :=\n");
	
	for(KeyPath p : paths)
	{
	    sb.append(p.getNodesKeyRepresentation(trans)+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getDemand_PathSET()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("set DEMAND_PATH :=\n");
	
	for(KeyPath p : paths)
	{
	    sb.append(p.getDemandKeyRepresentation(trans)+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    private String getPathsCost()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("param: routing_cost :=\n");
	
	for(KeyPath p : paths)
	{
	    sb.append(p.id+" "+p.getCost()+"\n");
	}
	
	sb.append(";");
	
	return sb.toString();
    }
    public void saveToFile(String file_name)
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append("data;\n\n");
	
	sb.append(this.getNodesSET()+"\n\n");
	
	sb.append(this.getLinksSET()+"\n\n");
	
	sb.append(this.getDemandsSET()+"\n\n");
	
	sb.append(this.getPathsSET()+"\n\n");
	
	sb.append(this.getPath_LinkSET()+"\n\n");
	
	sb.append(this.getPath_NodeSET()+"\n\n");
	
	sb.append(this.getDemand_PathSET()+"\n\n");
	
	sb.append(this.getDemandsCost()+"\n\n");
	
	sb.append(this.getLinksCost()+"\n\n");
	
	sb.append(this.getNodesCost()+"\n\n");
	
	sb.append(this.getPathsCost()+"\n\n");
	
	
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
