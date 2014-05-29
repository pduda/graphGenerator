package pl.waw.pduda.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class AbstractGraphGenerator
{
    protected int vertexNum =0;
    
    protected void saveToFile(String file_name,String value)
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append(value);
	sb.append("\n\n");
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
    
    protected abstract String getKeyRepresentation();
    protected abstract String getNumberRepresentation();
    
    public abstract void generateGraph(String file_name,int aps_num,int cabinets_num,int centrals_num,int edges_num,int basic_routing_cost,int max_random_routing_cost,int basic_install_cost,int max_install_random_cost);

}
