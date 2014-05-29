package pl.waw.pduda.graph;

public class Node
{
    public static final int AP = 0;
    public static final int CABINET =1;
    public static final int CENTRAL =2;
    public static final int END =3;
	
    public int type;
    public String id;
    public int instalation_cost;
    public int number;
	
    public Node(int t, String i,int cost,int n)
    {
        type=t;
        id = i;
        instalation_cost = cost;
        number= n;
    }
    @Override
    public String toString()
    {
	return id+" "+type+" "+instalation_cost+"\n";
    }
}