package scheduler;

import java.io.FileWriter;
import java.util.Formatter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		RC rc = null;
		if (args.length>1){
			System.out.println("Reading resource constraints from "+args[1]+"\n");
			rc = new RC();
			rc.parse(args[1]);
		}
		
		Dot_reader dr = new Dot_reader(true); ///
		if (args.length < 1) {
			System.err.printf("Usage: scheduler dotfile%n");
			System.exit(-1);
		}else {
			System.out.println("Scheduling "+args[0]);
			System.out.println();
		}
		
		Graph g = dr.parse(args[0]);
		System.out.printf("%s%n", g.diagnose());
		
		Scheduler s = new ASAP();
		
		Unfolding a = new Unfolding();
		g = a.Unfolding(g, 3);
		
		//Schedule sched = s.schedule(g);
		//sched.draw("schedules/ALAP_" + args[0].substring(args[0].lastIndexOf("/")+1));
		Graph_draw(g, "schedules/test.dot");
	}
	
	static public void Graph_draw (Graph g, String dotFileName) {
		try {
			
			int x = 0;
			int y = 0;
			int nodeHeight = 1;
			int nodeWidth = 1;
			BufferedWriter dotFile = new BufferedWriter(new FileWriter(dotFileName));
			dotFile.write("//do not use DOT to generate pdf use NEATO or FDP\n");
			dotFile.write("digraph{\n");
			dotFile.write("layout=\"neato\";\n");
			dotFile.write("splines=\"ortho\";\n");
			for(Node n : g) {
				dotFile.write(n.toString() + "[shape=\"ellipse\", style=\"filled\", color=\"#004E8ABF\", pos=\"" + x + "," + y + "!\", height=\"" + nodeHeight + "\", width=\"" + nodeWidth + "\"];\n");
				System.out.printf("-----------------------------------\n");
				System.out.printf("Current node: " + n.toString() + "\n");
				for(Node suc : n.allSuccessors().keySet()){
					System.out.printf("Successors node: " + suc.toString()+ "\n");
					if(n.allSuccessors().get(suc) != 0) {
						dotFile.write(n.toString() + " -> " + suc + "[constraint=false,color=blue,label=\"1\"]" + ";\n");
					}
					else {
						dotFile.write(n.toString() + " -> " + suc + ";\n");
					}
				}
				x+= 5;
				if(x > 25) {
					x = 0;
					y+=3;
				}
			}
			dotFile.write("}");	
			dotFile.flush();
			dotFile.close();
		} catch (IOException e) {
		System.out.println(e.getMessage());
	}
		
	
	}
}
