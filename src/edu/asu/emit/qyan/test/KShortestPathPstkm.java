package edu.asu.emit.qyan.test;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.VariableGraph;
import org.junit.Test;

public class KShortestPathPstkm extends YenTopKShortestPathsAlgTest
{
	static Graph graph = new VariableGraph("data/nasze_prosty_routing");
	static int MAX_PATHS = Integer.MAX_VALUE;
	static int START = 0;
	static int END = 8;
	
	@Test
	public void testYenShortestPathsAlg2()
	{
		System.out.println("Obtain all paths in increasing order! - updated!");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(
				graph, graph.get_vertex(START), graph.get_vertex(END));
		int i=0;
		while(yenAlg.has_next() && i<MAX_PATHS)
		{
			System.out.println("Path "+i+++" : "+yenAlg.next());
		}
		
		System.out.println("Result # :"+i);
		System.out.println("Candidate # :"+yenAlg.get_cadidate_size());
		System.out.println("All generated : "+yenAlg.get_generated_path_size());
	}
}
