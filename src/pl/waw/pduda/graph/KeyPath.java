package pl.waw.pduda.graph;

import java.awt.List;
import java.util.Vector;

import edu.asu.emit.qyan.alg.model.Pair;
import edu.asu.emit.qyan.alg.model.Path;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;

public class KeyPath
{
    Path path;
    Pair<String, String> demand;
    public String id;
    
    public KeyPath(String id,Path p,Pair<String, String> d)
    {
	this.path = p;
	this.demand = d;
	this.id = id;
    }
    public String getDemandKeyRepresentation(NodesTranslator trans)
    {
	return demand.first() + " "  + demand.second() + " "+id;
    }
    public int getCost()
    {
	return (int) this.path.get_weight();
    }
    public String getNodesKeyRepresentation(NodesTranslator trans)
    {
	Vector<BaseVertex> nodes = new Vector<BaseVertex>();
	nodes.addAll(this.path.get_vertices());
	
	StringBuilder sb = new StringBuilder();
	
	for(BaseVertex v : nodes)
	{
	    sb.append("("+this.id+", "+trans.translateNumberToKey(v.get_id())+") ");
	}
	return sb.toString();
    }
    public String getEdgesKeyRepresentation(NodesTranslator trans)
    {
	Vector<BaseVertex> nodes = new Vector<BaseVertex>();
	
	BaseVertex first=null,second=null;
	nodes.addAll(this.path.get_vertices());
	
	StringBuilder sb = new StringBuilder();
	
	for(BaseVertex v : nodes)
	{
	    if(first==null && second == null)
	    {
		first = v;
	    }
	    else if(second == null)
	    {
		second = v;
		sb.append("("+this.id+", "+trans.translateNumberToKey(first.get_id())+", "+trans.translateNumberToKey(second.get_id())+") ");
	    }
	    else
	    {
		first = second;
		second = v;
		sb.append("("+this.id+", "+trans.translateNumberToKey(first.get_id())+", "+trans.translateNumberToKey(second.get_id())+") ");
	    }
	}
	return sb.toString();
    }
}
