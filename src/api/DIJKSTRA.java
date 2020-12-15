package api;

import java.util.*;

/**
 * this class is an additional class for Dijkstra Algorithm methods used in DWGraph_Algo,
 * and a BFS-based connection check genuinely made for ex0.
 * also implemented a Comparator for comparing each 2 node_infos by their tag value - package friendly.
 * 2 algorithms:
 *  dijkstraPath.
 *  bfsConnection.
 * @author Adi Dahari.
 */
public class DIJKSTRA {
    //===============Methods===============//

    /**
     * Method based on Dijkstra Algorithm for finding the lowest-weighted directed path between every 2 nodes in the graph.
     * as the basic conditions checked in the main Algorithms class - DWGraph_Algo,
     * this method gets an existing graph and 2 nodes keys, both in graph.
     * start nodes initialized by a Tag value 0.0 as its distance from himself is 0.
     * start node's parent initialized as null for breaking the reverse List creation of the path.
     * Data structure used:
     * 1. visited = Boolean HashMap for marking each visited node by it's key.
     * 2. parents = node_info HashMap for saving each node's parent in the lowest-weighted path.
     * 3. pq = PriorityQueue implemented by the TagComparator made before, for the most efficient complexity.
     * 4. dist = HashMap for saving each node's path weight from start node.
     * 5. path = LinkedList containing the ordered path from start node to end node.
     *    this is the returned List<node_info>.
     * @Complexity O(V+E*logV) - as V stands for number of nodes, and E for number of edges.
     * @param g = directed_weighted_graph to apply this method on.
     * @param start = start node's Key value.
     * @param end = end node's Key value.
     * @return List<node_info> - the ordered path of node_info from start to end node.
     */
    public static  List<node_data> dijkstraPath(directed_weighted_graph g, int start, int end) {
        HashMap<Integer, Boolean> visited = new HashMap<>();
        HashMap<Integer, node_data> parents = new HashMap<>();
        HashMap<Integer, Double> dist = new HashMap<>();
        class WComparator implements Comparator<node_data>{

            @Override
            public int compare(node_data o1, node_data o2) {
                return Double.compare(dist.get(o1.getKey()), dist.get(o2.getKey()));
            }
        }
        PriorityQueue<node_data> pq = new PriorityQueue<node_data>(20, new WComparator());
        node_data nStart = g.getNode(start);
        visited.put(start, true);
        dist.put(nStart.getKey(), 0.0);
        pq.add(nStart);
        parents.put(start, null);
        while (!pq.isEmpty()) {
            node_data n = pq.poll();
            Collection<edge_data> adjList = g.getE(n.getKey());
            for (edge_data e : adjList) {
                double currWeight = dist.get(n.getKey()) + e.getWeight();
                if(!visited.containsKey(e.getDest())){
                    dist.put(e.getDest(), currWeight);
                    pq.add(g.getNode(e.getDest()));
                    visited.put(e.getDest(), true);
                    parents.put(e.getDest(), n);
                }
                if (currWeight < dist.get(e.getDest())) {

                    dist.replace(e.getDest(), currWeight);
                    parents.replace(e.getDest(), n);
                    if(!pq.contains(g.getNode(e.getDest()))) pq.add(g.getNode(e.getDest()));
                }
            }
        }

        if(!visited.containsKey(end)) return null;
        LinkedList<node_data> path = new LinkedList<>();
        node_data dest = g.getNode(end);
        if(dist.get(end) == null) return null;
        while(dest != null){

            path.addFirst(dest);
            dest = parents.get(dest.getKey());
        }
        return path;
    }

    /**
     * using same idea of the previous algorithm' only this time returns the weight of the path.
     * @Complexity O(V+E*logV) - as V stands for number of nodes, and E for number of edges.
     * @param g = directed_weighted_graph to apply this method on.
     * @param start = start node's Key value.
     * @param end = end node's Key value.
     * @return double = path's weight.
     */
    public static double dijkstraDist(directed_weighted_graph g, int start, int end){
        HashMap<Integer, Boolean> visited = new HashMap<>();
        HashMap<Integer, node_data> parents = new HashMap<>();
        HashMap<Integer, Double> dist = new HashMap<>();
        class WComparator implements Comparator<node_data>{

            @Override
            public int compare(node_data o1, node_data o2) {
                return Double.compare(dist.get(o1.getKey()), dist.get(o2.getKey()));
            }
        }
        PriorityQueue<node_data> pq = new PriorityQueue<node_data>(20, new WComparator());
        node_data nStart = g.getNode(start);
        visited.put(start, true);
        dist.put(nStart.getKey(), 0.0);
        pq.add(nStart);
        parents.put(start, null);
        while (!pq.isEmpty()) {
            node_data n = pq.poll();
            Collection<edge_data> adjList = g.getE(n.getKey());
            for (edge_data e : adjList) {
                double currWeight = dist.get(n.getKey()) + e.getWeight();
                if(!visited.containsKey(e.getDest())){
                    dist.put(e.getDest(), currWeight);
                    pq.add(g.getNode(e.getDest()));
                    visited.put(e.getDest(), true);
                    parents.put(e.getDest(), n);
                }
                if (currWeight < dist.get(e.getDest())) {

                    dist.replace(e.getDest(), currWeight);
                    parents.replace(e.getDest(), n);
                    if(!pq.contains(g.getNode(e.getDest()))) pq.add(g.getNode(e.getDest()));
                }
            }
        }
        if(!dist.containsKey(end)) return -1;
        return dist.get(end);
    }

    /**
     * a BFS'd based method for checking whether a given graph is connected or not,
     * using a LinkedList - as Queue and a boolean map for marking visited nodes.
     * using a very similar idea to the bfsPath execution, without an end node.
     * for further info about the idea look at ex0.
     * @Complexity O(V+E) - V = number of nodes in graph, E = number of edges in graph.
     * @param g - graph_algorithms implemented by Graph_Algo
     * @param start - a start node for the BFS method
     * @return if graph is connected - true. else - false
     */
    public static boolean bfsConnection(directed_weighted_graph g, node_data start) {
        LinkedList<node_data> q = new LinkedList<>();
        HashMap<Integer, Boolean> visited = new HashMap<>();
        q.add(start);
        visited.put(start.getKey(), true);
        while (!q.isEmpty()) {
            node_data n = q.poll();
            for (edge_data edge : g.getE(n.getKey())) {
                if (visited.containsKey(edge.getDest()) && !visited.get(edge.getDest())) {
                    q.add(g.getNode(edge.getDest()));
                    visited.put(edge.getDest(), true);
                } else if (visited.get(edge.getDest()) == null || !visited.get(edge.getDest())) {
                    visited.put(edge.getDest(), true);
                    q.add(g.getNode(edge.getDest()));
                }
            }
        }
        return visited.size() == g.nodeSize();  //checking if the visited map size equals to number of nodes in graph
        //if is - the whole graph is connected
    }
}