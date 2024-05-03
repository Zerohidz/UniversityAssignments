// import java.util.*;
// import java.io.*;

// public class Cop {
//     public static class Edge implements Comparable<Edge> {
//         private final int v, w;
//         private final double weight;

//         public Edge(int v, int w, double weight) {
//             this.v = v;
//             this.w = w;
//             this.weight = weight;
//         }

//         public int either() {
//             return v;
//         }

//         public int other(int vertex) {
//             if (vertex == v)
//                 return w;
//             else
//                 return v;
//         }

//         public int compareTo(Edge that) {
//             if (this.weight < that.weight)
//                 return -1;
//             else if (this.weight > that.weight)
//                 return +1;
//             else
//                 return 0;
//         }
//     }

//     public static class EdgeWeightedGraph {
//         private final int V;
//         private final LinkedList<Edge>[] adj;

//         public EdgeWeightedGraph(int V) {
//             this.V = V;
//             adj = (LinkedList<Edge>[]) new LinkedList[V];
//             for (int v = 0; v < V; v++)
//                 adj[v] = new LinkedList<Edge>();
//         }

//         public LinkedList<Edge> edges() {
//             LinkedList<Edge> list = new LinkedList<Edge>();
//             for (int v = 0; v < V; v++)
//                 for (Edge e : adj[v])
//                     if (e.other(v) > v)
//                         list.add(e);
//             return list;
//         }

//         public void addEdge(Edge e) {
//             int v = e.either(), w = e.other(v);
//             adj[v].add(e);
//             adj[w].add(e);
//         }

//         public Iterable<Edge> adj(int v) {
//             return adj[v];
//         }
//     }

//     public static class KruskalMST {
//         private Queue<Edge> mst = new LinkedList<Edge>();

//         public KruskalMST(EdgeWeightedGraph G) {
//             PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
//             for (Edge e : G.edges())
//                 pq.add(e);
//             UF uf = new UF(G.V());
//             while (!pq.isEmpty() && mst.size() < G.V() - 1) {
//                 Edge e = pq.delMin();
//                 int v = e.either(), w = e.other(v);
//                 if (!uf.connected(v, w)) {
//                     uf.union(v, w);
//                     mst.enqueue(e);
//                 }
//             }
//         }

//         public Iterable<Edge> edges() {
//             return mst;
//         }
//     }

//     public static void main(String[] args) throws IOException {
//         BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));

//         int numTestCase = Integer.parseInt(reader.readLine());
//         for (int i = 0; i < numTestCase; i++) {
//             String[] input = reader.readLine().split(" ");
//             int numDroneStations = Integer.parseInt(input[0]);
//             int numTotalStations = Integer.parseInt(input[1]);
//             int numGroundPumpStations = numTotalStations - numDroneStations;

//         }
//     }
// }
