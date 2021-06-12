package scheduler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/***
 * Unfolding for a graph
 * 
 *	@param sg: input graph
 *  @param iteration: number of iteration the unfolding should do  
 *  @return graph after unfolding
 *
 */
public class Unfolding {
	public Graph Unfolding(final Graph sg, int iteration)
	{
		Graph g = sg.clone();	//clone the graph so that we can do modification

		//for(int i = 1; i<=iteration; i++)	//put it into  the next small loop
		//{
		Iterator<Node> graph_iterator = sg.iterator();
		for(int m = 0; m<sg.size(); m++)
		{
			Node ng = graph_iterator.next();	//node from original graph
				
			//create new node corresponding to node ng (name id + Iteration number, resource type)"  e.g. n1 --> n1_2
			for(int i = 2; i<=iteration; i++)
			{
				Node node_new = new Node(ng.toString()+"_"+Integer.toString(i), ng.getRT());
				g.add(node_new);
			}
				
			if(!ng.allSuccessors().isEmpty())	//not the last node
			{
				HashMap<Node, Integer> map_successor = ng.allSuccessors();
				Iterator<Node> iterator = map_successor.keySet().iterator();
				while(iterator.hasNext()==true)
				{
					Node successor = iterator.next();
					int distance = ng.getSuccWeight(successor);	//get label of edges

					//create new node corresponding to successor (name id + Iteration number, resource type)"  e.g. n1 --> n1_2
					for(int i = 2; i<=iteration; i++)
					{
						Node successor_new = new Node(successor.toString()+"_"+Integer.toString(i), successor.getRT());
						g.add(successor_new);
					}
							

					if(distance==0)
					{
						for(int i = 2; i<=iteration; i++)
						{
							String successor_id = successor.toString()+"_"+Integer.toString(i);	
							String node_id = ng.toString()+"_"+Integer.toString(i);
								
							g.rlink(node_search(node_id,g),node_search(successor_id,g) , distance);
						}
					}
							
					else
					{
						g.unlink(ng, successor);
						for(int i=1; i<=iteration; i++)
						{
							String successor_id = successor.toString()+"_"+Integer.toString(i+1);	
							String node_id = ng.toString()+"_"+Integer.toString(i);
							if(i==1)
								g.rlink(ng, node_search(successor_id,g), distance-1);
							else if(i==iteration)
								g.rlink(node_search(node_id,g), successor, distance); //original weight
							else
							{
								g.rlink(node_search(node_id,g), node_search(successor_id,g), distance-1); //reduced weight
							}
						}	
					}		
				}
			}	
		}		
		return g;
	}
	
	
	/***
	 * Find a node in the graph
	 * 
	 *	@param id: id of a node
	 *  @param g: input graph  
	 *  @return node according the id
	 *
	 */
	public Node node_search(String id, Graph g)
	{
		Node node = null;
		for(Node a : g)	
		{
			if(a.toString().contentEquals(id))
				node = a;
		}
		return node;
	}
}