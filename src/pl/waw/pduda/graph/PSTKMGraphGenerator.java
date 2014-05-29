package pl.waw.pduda.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import edu.asu.emit.qyan.alg.model.Pair;

public class PSTKMGraphGenerator extends AbstractGraphGenerator
{
    Random randomizer = new Random();
    public Map<String,Node> aps = new Hashtable<String,Node>();
    public Map<String,Node> cabinets = new Hashtable<String,Node>();
    public Map<String,Node> centrals = new Hashtable<String,Node>();
    public Map<String,Node> nodes = new Hashtable<String,Node>();
    public ArrayList<Node> ends = new ArrayList<Node>();
    public Map<Pair<String, String>, Pair<Integer, Integer>> edges = new Hashtable<Pair<String, String>, Pair<Integer, Integer>>();
    NodesTranslator trans;
    Node end; 
    
    public static final int MAX_WRONG_RANDOM = 100;
    
    public void generateGraph(String file_name,int aps_num,int cabinets_num,int centrals_num,int edges_num,int basic_routing_cost,int max_random_routing_cost,int basic_install_cost,int max_install_random_cost)
    {
	int edges_number = edges_num;
	
	aps = genNodeSet(Node.AP, 0, 0, aps_num);
	cabinets = genNodeSet(Node.CABINET, 10, 0, cabinets_num);
	centrals = genNodeSet(Node.CENTRAL, 15, 0, centrals_num);

	nodes.putAll(aps);
	nodes.putAll(cabinets);
	nodes.putAll(centrals);
	
	// na poczatku losuje aps sciezek - moga byc one albo do innego ap albo do centralki
	int ap_cabinet_num = randomizer.nextInt(aps_num-1)+1;//musi byc min 1 sciezka
	int ap_ap_num = aps_num - ap_cabinet_num;
	
	edges = genPairMap(edges, aps.values(), aps.values(), ap_ap_num, basic_routing_cost, max_random_routing_cost,basic_install_cost,max_install_random_cost);
	edges = genPairMap(edges, aps.values(), cabinets.values(), ap_cabinet_num, basic_routing_cost, max_random_routing_cost,basic_install_cost,max_install_random_cost);
    
	edges_number -=aps_num;
	// zalatwiona conajmniej jedna sciezka z ap do C
	
	
	//teraz lecimy cabinety zeby mialy polaczenie conajmniej do 1 centralki
	int cabinet_centrals_num = randomizer.nextInt(cabinets_num)+1;//musi byc min 1 sciezka
	int cabinet_cabinet_num = cabinets_num - cabinet_centrals_num;
	
	edges = genPairMap(edges, cabinets.values(), cabinets.values(), cabinet_cabinet_num, basic_routing_cost, max_random_routing_cost,basic_install_cost,max_install_random_cost);
	edges = genPairMap(edges, cabinets.values(), centrals.values(), cabinet_centrals_num, basic_routing_cost, max_random_routing_cost,basic_install_cost,max_install_random_cost);
    
	edges_number -=cabinets_num;
	// zalatwiona conajmniej jedna sciezka z cabinets do L
	    
	
	//losowanie pozostalych krawedzi
	if(edges_number>0)
	    edges = genPairMap(edges, nodes.values(), nodes.values(), edges_number, basic_routing_cost, max_random_routing_cost,basic_install_cost,max_install_random_cost);
	
	//krawedzie z L do END
	end =new Node(Node.END,"END1",0,this.vertexNum);
	this.vertexNum++;
	nodes.put(end.id,end);
	ends.add(end);
	
	edges = genPairMap(edges, centrals.values(), ends, cabinets_num, 0, 0,0,0);
	
	saveToFile(file_name+"_key",this.getKeyRepresentation());
	
	trans = new NodesTranslator(nodes);
	saveToFile(file_name+"_numbers",this.getNumberRepresentation());
    }
    private Map<Pair<String, String>, Pair<Integer, Integer>> genPairMap(Map<Pair<String, String>, Pair<Integer, Integer>> map,Collection<Node> dict1,Collection<Node> dict2,int iterations,int basic_routing_cost,int max_routing_random_cost,int basic_install_cost,int max_install_random_cost)
    {
	int s,e,i = 0;
	int dict1_size = dict1.size();
	int dict2_size = dict2.size();
	Node[] temp_dict1 = dict1.toArray(new Node[]{});
	Node[] temp_dict2 = dict2.toArray(new Node[]{});
	
	int wrong = MAX_WRONG_RANDOM;
	
	while(i < iterations && wrong >= 1)
	{
	    s = randomizer.nextInt(dict1_size);
	    e = randomizer.nextInt(dict2_size);
	   // System.out.println(s+" "+ e);
	    
	    try
	    {
	    //jesli nie ten sam node lub jesli nie krawedz z ap do L
	    if(temp_dict1[s].id!=temp_dict2[e].id)
	    {
		if(!((temp_dict1[s].type==Node.AP && temp_dict2[e].type==Node.CENTRAL) || (temp_dict1[s].type==Node.CENTRAL && temp_dict2[e].type==Node.AP)))
		{
        	    Pair<String,String> temp1 = new Pair<String,String>(temp_dict1[s].id,temp_dict2[e].id);
        	    Pair<String,String> temp2 = new Pair<String,String>(temp_dict2[e].id,temp_dict1[s].id);
        		    
        	    if(!map.containsKey(temp1))//dodaje od razu w dwie strony
        	    {
        		
        		int tempCost1,tempCost2=0;
        		
        		if(max_routing_random_cost>0)
        		    tempCost1 = randomizer.nextInt(max_routing_random_cost)+basic_routing_cost;
        		else
        		    tempCost1 = basic_routing_cost;
        		
        		if(max_install_random_cost>0)
        		    tempCost2 = randomizer.nextInt(max_install_random_cost)+basic_install_cost;
        		else
        		    tempCost2 = basic_install_cost;
        		
        		Pair<Integer,Integer> costs = new Pair<Integer,Integer>(tempCost1,tempCost2);
        		
        		map.put(temp1,costs);
        		map.put(temp2,costs);
        		
        		i++;
        	    }
        	    else
        	    {
        		wrong--;
        		System.out.println("error - "+wrong);
        	    }
		}
	    }
	    else
	    {
		wrong--;
		System.out.println("error - "+wrong);
	    }
	    }
	    catch(Exception error)
	    {
		System.out.println("error critical");
	    }
	}
	System.out.println("done");
	return map;
    }
    private Map<String,Node> genNodeSet(int type,int basic_cost,int max_random_cost,int number)
    {
	Map<String,Node> nodes = new Hashtable<String,Node>();
	
	String key;
	
	for(int i=0;i<number;i++)
	{
	    switch(type)
	    {
		case Node.AP:
		    key = "AP"+i;
		    break;
		case Node.CABINET:
		    key = "C"+i;
		    break;
		default: 
		    key = "L"+i;
		    break;
	    }
	    int tempCost=0;
	    if(max_random_cost>0)
		tempCost = randomizer.nextInt(max_random_cost)+basic_cost;
	    else
		tempCost = basic_cost;
	    
	    Node n = new Node(type,key,tempCost,this.vertexNum);
	    
	    nodes.put(key, n);
	    
	    this.vertexNum++;
	}
	
	return nodes;
    }
    
    @Override
    protected String getKeyRepresentation()
    {
	StringBuffer sb = new StringBuffer();
	sb.append("# node.id node.type node.instalation_cost\n");
	sb.append("NODES\n");
	
	for(Node n : nodes.values())
	{
	    sb.append(n.id+" "+ n.type + " " + n.instalation_cost+"\n");
	}
	sb.append("\n# link.startNode link.endNode link.routingCost\n");
	sb.append("LINKS\n");
	for(Pair<String,String> pair : edges.keySet())
	{
	    String starting_pt_id = pair.first();
	    String ending_pt_id = pair.second();
	    Pair<Integer,Integer> costs = edges.get(pair);
	    sb.append(starting_pt_id+" "+ending_pt_id+" "+costs.first()+" "+costs.second()+"\n");
	}
	
	return sb.toString();
    }
    
    @Override
    protected String getNumberRepresentation()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append(this.vertexNum+"\n\n");

	for(Pair<String,String> pair : edges.keySet())
	{
	    int starting_pt_id = trans.translateKeyToNumber(pair.first());
	    int ending_pt_id = trans.translateKeyToNumber(pair.second());
	    Pair<Integer,Integer> costs = edges.get(pair);
	    sb.append(starting_pt_id+" "+ending_pt_id+" "+costs.first()+"\n");
	}
	
	return sb.toString();
    }
    
    public NodesTranslator getTranslator()
    {
	return this.trans;
    }
    public Map<String,Node> getAps()
    {
	return this.aps;
    }
    public Node getEnd()
    {
	return this.end;
    }
}
