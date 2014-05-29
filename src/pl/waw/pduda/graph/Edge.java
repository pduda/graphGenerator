package pl.waw.pduda.graph;

public class Edge
{
    public int instalation_cost;
    public int routing_cost;
    public String id;
    
    public Edge(int i_cost,int r_cost,String i)
    {
	instalation_cost = i_cost;
	routing_cost = r_cost;
	id = i;
    }
}
