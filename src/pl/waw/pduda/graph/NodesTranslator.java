package pl.waw.pduda.graph;

import java.util.Hashtable;
import java.util.Map;

public class NodesTranslator
{

    Map<String,Integer> keys = new Hashtable<String,Integer>();
    Map<Integer,String> numbers = new Hashtable<Integer,String>();
    
    public NodesTranslator(Map<String,Node> nodes)
    {
	for(Node n: nodes.values())
	{
	    keys.put(n.id, n.number);
	    numbers.put(n.number, n.id);
	}
    }
    public String translateNumberToKey(int n)
    {
	return this.numbers.get(n);
    }
    public Integer translateKeyToNumber(String n)
    {
	return this.keys.get(n);
    }
}
