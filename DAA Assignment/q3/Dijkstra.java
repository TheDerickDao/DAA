import java.util.*;

public class Dijkstra
{
    private static final Graph.Edge[] GRAPH = 
	{ 
        new Graph.Edge("A", "B", 6), 
        new Graph.Edge("A", "C", 7), 
        new Graph.Edge("A", "D", 2),
		new Graph.Edge("A", "F", 8),		
        new Graph.Edge("B", "D", 8), 
        new Graph.Edge("B", "E", 9),
        new Graph.Edge("B", "A", 6),
        new Graph.Edge("C", "A", 7),
        new Graph.Edge("C", "F", 5),
        new Graph.Edge("D", "A", 2),
        new Graph.Edge("D", "B", 8),
		new Graph.Edge("D", "E", 7),
		new Graph.Edge("D", "F", 4),
		new Graph.Edge("D", "G", 3),
        new Graph.Edge("E", "B", 9),
		new Graph.Edge("E", "D", 7),
		new Graph.Edge("E", "G", 6),
        new Graph.Edge("F", "A", 8),
		new Graph.Edge("F", "C", 5),
		new Graph.Edge("F", "D", 4),
		new Graph.Edge("F", "G", 11),
        new Graph.Edge("G", "D", 3),
		new Graph.Edge("G", "E", 6),
		new Graph.Edge("G", "F", 11)
    };

    private static final String START = "F";
    private static final String END = "E";

    public static void main(String[] args)
	{
        Graph g = new Graph(GRAPH);
		System.out.println("\n====== EXECUTING DIJKSTRA ======\n");
        g.dijkstra(START);
		System.out.println("\n====== EXECUTING RESPATH ======\n");
        g.resPath(END);
		System.out.println("\n====== EXECUTING REVEDGES ======\n");
        g.revEdges(END);
		System.out.println("\n====== EXECUTING POTENTIALS ======\n");
        g.potentials();
		System.out.println("\n====== EXECUTING DIJKSTRA AGAIN ======\n");
        g.dijkstra(START);
		System.out.println("\n====== EXECUTING RESPATH AGAIN ======\n");
        g.resPath(END);

		System.out.println("------------------------------------");
		System.out.println("2DP/C SOLUTIONS:\n");
        g.printPaths(START, END);
		System.out.println("------------------------------------");
    }
}

class Graph
{
    private final Map<String, Vertex> graph;

    public static class Edge implements Comparable<Edge>
	{
        public final String v1, v2;
        public final int dist;

        public Edge(String v1, String v2, int dist)
		{
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }

        public int compareTo(Edge other)
		{
            if (v1.equals(other.v1))
			{
				return v2.compareTo(other.v2);
			}
            return v1.compareTo(other.v1);
        }
    }

    private TreeSet<Edge> answer = new TreeSet<Edge>();

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    public static class Vertex implements Comparable<Vertex>
	{
        public final String name;
        public int potential = 0;
        public int dist = Integer.MAX_VALUE;
        public Vertex previous = null;
        public final Map<Vertex, Integer> neighbours = new HashMap<Vertex, Integer>();

        public Vertex(String name)
		{
            this.name = name;
        }

        public int compareTo(Vertex other)
		{
            if (dist == other.dist)
			{
				return name.compareTo(other.name);
			}
            return Integer.compare(dist, other.dist);
        }
    }

    public Graph(Edge[] edges)
	{
        graph = new HashMap<String, Vertex>(edges.length);

        for (Edge e : edges)
		{
            if (!graph.containsKey(e.v1))
			{
				graph.put(e.v1, new Vertex(e.v1));
			}
                
            if (!graph.containsKey(e.v2))
			{
				graph.put(e.v2, new Vertex(e.v2));
			}
        }

        for (Edge e : edges)
		{
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist);
        }
    }

    /*	If the graph doesn't contain the node with the specified name, terminate
		For all vertexes v in the graph of values
		If the vertex is the source, set the previous node of this vertex to the source and set its distance to 0
		Otherwise, the vertex isn't connected to by anything and its distance is now INFINITY because we don't care about exploring this node
		Add that vertex back into the tree set.
		Run djikstra again on the list of vertices.
	*/
    public void dijkstra(String startName)
	{
		Vertex source;
		
        if (!graph.containsKey(startName))
		{
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
		
        source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<Vertex>();

        // set-up vertices
        for (Vertex v : graph.values())
		{
			if(v != source)
			{
				System.out.println("VERTEX: " + v.name + " IS NOT THE SOURCE: " + startName);
				System.out.println("CHANGING PREVIOUS OF V TO NULL AND IT'S DISTANCE TO INFINITY\n");
				v.previous = null;
				v.dist = Integer.MAX_VALUE;
			}
			else
			{
				System.out.println("VERTEX: " + v.name + " IS THE SOURCE: " + startName);
				System.out.println("CHANGING PREVIOUS OF V TO SOURCE AND IT'S DISTANCE TO 0\n");
				v.previous = source;
				v.dist = 0;
			}
            q.add(v);
        }

        dijkstra(q);
    }

    /*
		Evaluates the tree set q and picks the vertex with the shortest distance
		If that node is marked as infinity, ignore it and any other remaining vertices because they don't need to be evaluated if the rest are infinity
		Evaluate each distance of the vertex to its neighbours
		Get the neighbour's key, set the alternate distance using the distance of the shortest node, the current neighbour's value, and each of their potentials to be the answer
		If the alternate distance is less than the distance to the neighbour
		Remove the neighbour from the tree set q
		Set the neighbour's distance to the alternate distance
		Set its previous node to the node with the shortest distance and add it to the tree set.
		
		Utilises Dijkstra's Algorithm
	*/
    private void dijkstra(final NavigableSet<Vertex> q)
	{
        Vertex u, v;
		int alternateDist;
		
        while (!q.isEmpty())
		{

            u = q.pollFirst();
            if (u.dist == Integer.MAX_VALUE)
			{
				System.out.println("THIS NODE: " + u.name + ", THE DISTANCE IS INFINITY. IGNORE!");
				break;
			}

            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet())
			{
                v = a.getKey();

                alternateDist = u.dist + a.getValue() + u.potential - v.potential;
                if (alternateDist < v.dist)
				{ 
					System.out.println("CHANGING DISTANCE OF " + v.name + " TO ALTERNATE DISTANCE: " + alternateDist);
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }
	
    /*	Get the end node
		If there is a node connecting to it other than itself
		Get that node, get the neighbour from the hashmap
		Remove v and w as neighbours of each other
		Insert the current evaluated node to the hashmap
		Evaluate the node taken from the hashmap
	*/
    public void revEdges(String endName)
	{
		System.out.println("REVERTING EDGES FROM END NODE: " + endName + "\n");
		Vertex v = null, w = null;
		int weight;
		
        v = graph.get(endName);
        while (v.previous != v && v.previous != null)
		{
            w = v.previous;
            weight = v.neighbours.get(w);
            v.neighbours.remove(w);
            w.neighbours.remove(v);

            v.neighbours.put(w, - weight);

            v = w;
        }
    }

	/* Assigns each vertex with a value that dictates each node's value towards the answer for the shortest path */
	
    public void potentials()
	{
		System.out.println("CHANGING ALL POTENTIAL OF ALL VERTICES WITH ITS DISTANCE!\n");
        for (Vertex v : graph.values())
		{
            v.potential = v.dist;
        }
    }

    /*	restorePath gets the end node
		While the a node connects to the end node and that node isn't itself (loop)
		Get the name of that connecting node and assign it to 'to'
		If the edge that connects these two nodes has a dist of 0 in the answer tree set
		Remove that edge.
		Otherwise
		Add that edge as part of the answer.
		Continue to backtrack.
	*/
    public void resPath(String endName)
	{
		System.out.println("RESTORING PATH FROM: " + endName + "\n");
		String from = "", to = "";
        Vertex v = graph.get(endName);
        while (v.previous != v && v.previous != null)
		{
            from = v.previous.name;
            to = v.name;
            if (answer.contains(new Edge(to, from, 0)))
			{
                answer.remove(new Edge(to, from, 0));
            }
            else
			{
                answer.add(new Edge(from, to, 0));
            }
            v = v.previous;
        }
    }

    public void printOnePath(String startName, String endName)
	{
        Vertex from, to, cur;
		from = graph.get(startName);
		to = graph.get(endName);
        cur = from;
        do
		{
            System.out.printf("%s -> ", cur.name);

            Edge e = answer.ceiling(new Edge(cur.name, "", 0));
            answer.remove(e);

            cur = graph.get(e.v2);
        } while (cur != to);
        System.out.println(to.name);
    }

    public void printPaths(String startName, String endName)
	{
        printOnePath(startName, endName);
        printOnePath(startName, endName);
    }
}