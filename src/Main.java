import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import pl.waw.pduda.graph.AMPLSaver;
import pl.waw.pduda.graph.AbstractGraphGenerator;
import pl.waw.pduda.graph.KeyPath;
import pl.waw.pduda.graph.Node;
import pl.waw.pduda.graph.NodesTranslator;
import pl.waw.pduda.graph.PSTKMGraphGenerator;
import pl.waw.pduda.graph.SimpleGraphGenerator;
import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.Pair;
import edu.asu.emit.qyan.alg.model.VariableGraph;
import edu.asu.emit.qyan.test.KShortestPathPstkm;
import edu.asu.emit.qyan.test.ShortestPathAlgTest;
import edu.asu.emit.qyan.test.YenTopKShortestPathsAlgTest;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LinearProgram;


public class Main 
{
    static int MAX_PATHS = Integer.MAX_VALUE;
    static PSTKMGraphGenerator genGraph = new PSTKMGraphGenerator();
    static AMPLSaver saver = new AMPLSaver();
    static Random randomizer = new Random();
    static NodesTranslator trans;
	
    static Map<String,Node> aps;
    static Node end;
    static ArrayList<KeyPath> paths = new ArrayList<KeyPath>();

    static Graph graph;
    static Map<String,Integer> demand_val = new Hashtable<String,Integer>();
    
    
    public static void genAmplFile(String file_path)
    {
        
	paths.clear();
	
    	int p=0;
    	for(Node ap: aps.values())
    	{
    	    YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(
    			graph, graph.get_vertex(ap.number), graph.get_vertex(end.number));
    	    
    	    Pair<String,String> demand = new Pair<String,String>(ap.id,end.id);
    	    
    	    int i=0;
    	    while(yenAlg.has_next() && i<MAX_PATHS)
    	    {
    		KeyPath temp =new KeyPath("P"+p,yenAlg.next(),demand);
    		paths.add(temp);
    		System.out.println("Path "+i+" : "+temp.getNodesKeyRepresentation(trans));
    		System.out.println("Path "+i+" : "+temp.getEdgesKeyRepresentation(trans));
    		i++;
    		p++;
    	    }
    	    saver.demands.put(demand,demand_val.get(ap.id));
    	}
    	
    	
    	saver.paths = paths;
    	saver.nodes = genGraph.nodes;
    	saver.edges = genGraph.edges;
    	saver.trans = trans;
    	
    	saver.saveToFile(file_path);
    	
    }
    public static void main(String[] args) 
    {
	
/*	LinearProgram lp = new LinearProgram(new double[]{5.0,10.0});
	lp.s
	lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{3.0,1.0}, 8.0, "c1"));
	lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{0.0,4.0}, 4.0, "c2"));
	lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{2.0,0.0}, 2.0, "c3"));
	lp.setMinProblem(true);
	LinearProgramSolver solver  = SolverFactory.newDefault();
	double[] sol = solver.solve(lp);*/
	
	/*LPWizard lpw = new LPWizard();
	lpw.plus("x1",5.0).plus("x2",10.0);
	lpw.addConstraint("c1",8,"<=").plus("x1",3.0).plus("x2",1.0);
	lpw.addConstraint("c2",4,"<=").plus("x2",4.0);
	lpw.addConstraint("c3", 2, ">=").plus("x1",2.0); */
	
	String [] files = {
		"ampl/obrona1",
		"ampl/obrona2"
		/*"ampl/genk1AP4C3L2e15",
		"ampl/genk3AP4C3L2e15"
		,"ampl/genk5AP4C3L2e15",
		"ampl/genk8AP4C3L2e15",
		"ampl/genk10AP4C3L2e15",
		"ampl/genk12AP4C3L2e15",
		"ampl/genk15AP4C3L2e15"*/
		};
	
	int [] max_paths = {1,3};
	int size =2;
	
	//sciezka ap_num c_num l_num e_num e_cost e_random node_cost node_random
	genGraph.generateGraph(files[0], 2, 2, 2, 5, 3, 1,10,5); MAX_PATHS = 3;
	//genGraph.generateGraph(path, 3, 3, 3, 15, 3, 0,10,0); MAX_PATHS = 5;
	//genGraph.generateGraph(path, 3, 3, 3, 15, 3, 0,10,0); MAX_PATHS = 10;

	trans = genGraph.getTranslator();
    	
    	aps = genGraph.getAps();
    	end = genGraph.getEnd();
    
    	graph = new VariableGraph(files[0] + "_numbers");
	
    	for(Node ap: aps.values())
    	{
    	    demand_val.put(ap.id, randomizer.nextInt(10));
    	}
	for(int i=0;i<size;i++)
	{
	    MAX_PATHS=max_paths[i];
	    genAmplFile(files[i]);
	}
	
    }

}
