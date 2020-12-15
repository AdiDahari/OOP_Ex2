package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
/**
 * This class represents a directed weighted graph, implementing directed_weighted_graph interface.
 * each graph contains a collection of nodes and directed weighted edges.
 * all nodes are of the type node_data - an interface implemented in NodeData class.
 * all edges are of the type edge_data - an interface implemented in EdgeData.
 * each graph implemented by:
 * 1. nodes = a HashMap of all nodes by their keys.
 * 2. edges = a. main HashMap is for each node in graph, contains all edges by node's key.
 *            b. nested HashMap is for all of the edges by the dest node's key.
 * 3. edgeCount = for easy reference to the amount of edges in graph.
 * 4. modeCount = counting the changes made in graph since initialized.
 */
public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, node_data> Nodes;
    private HashMap<Integer, HashMap<Integer,edge_data>> Edges;
    private int edgeCount = 0;
    private int modeCount = 0;

    //[Empty Constructor]
    public DWGraph_DS(){
        Nodes = new HashMap<>();
        Edges = new HashMap<>();
    }

    //[Constructor by HashMap of node_data]
    public DWGraph_DS(HashMap<Integer, node_data> map){
        Nodes = map;
        Edges = new HashMap<>();
        for(node_data n : map.values()){
            Edges.put(n.getKey(), new HashMap<>());
        }
    }

    //[Full Consturctor](mainly for loading graphs from files)
    public DWGraph_DS(HashMap<Integer, node_data> nodes, HashMap<Integer, HashMap<Integer, edge_data>> edges, int eCount, int mCount){
        Nodes = nodes;
        Edges = edges;
        edgeCount = eCount;
        modeCount = mCount;
    }

    //[Copy Constructor]
    public DWGraph_DS(directed_weighted_graph g){
        Nodes = new HashMap<>();
        Edges = new HashMap<>();
        for(node_data n : g.getV()){
            Nodes.put(n.getKey(), n);
            Edges.put(n.getKey(), new HashMap<>());
            for(edge_data e : g.getE(n.getKey())){
                Edges.get(n.getKey()).put(e.getDest(), new EdgeData(e));
            }
        }
        edgeCount = g.edgeSize();

    }

    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return Nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return
     * @Complexity O(1), an independent number of actions.
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(Edges.containsKey(src) && Edges.get(src).containsKey(dest)){
            return Edges.get(src).get(dest);
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * @param n
     * @Complexity O(1), an independent number of actions.
     */
    @Override
    public void addNode(node_data n) {
        if(n != null) {
            Nodes.put(n.getKey(), n);
            Edges.put(n.getKey(), new HashMap<>());
            modeCount++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     * @Complexity O(1), an independent number of actions.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(!Nodes.containsKey(src) || !Nodes.containsKey(dest) || w < 0) return;
        edge_data newEdge = new EdgeData(src, dest, w);
        if(!Edges.get(src).containsKey(dest)) edgeCount++;
        Edges.get(src).put(dest, newEdge);
        modeCount++;

    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     * @Complexity O(1) - shallow copy return
     */
    @Override
    public Collection<node_data> getV() {
        return Nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @return Collection<edge_data>
     * @Complexity O(k), k = degree of the node.
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        return Edges.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key
     * @Complexity O(1) - shallow copy return
     */
    @Override
    public node_data removeNode(int key) {
        node_data rNode = Nodes.get(key);
        if(rNode == null) return rNode;
        for(node_data curr : getV()){
            removeEdge(curr.getKey(), key);
        }
        edgeCount = edgeCount - getE(key).size();
        modeCount++;
        Nodes.remove(key);
        Edges.remove(key);
        return rNode;
    }

    /**
     * Deletes the edge from the graph,
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     * @Complexity O(1), an independent number of actions.
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(Nodes.get(src) == null || Nodes.get(dest) == null || !Edges.get(src).containsKey(dest)) return null;
        edge_data rEdge = Edges.get(src).get(dest);
        Edges.get(src).remove(dest);
        edgeCount--;
        modeCount++;
        return rEdge;
    }

    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     * @Complexity O(1), a single action.
     */
    @Override
    public int nodeSize() {
        return Nodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * @return
     * @Complexity O(1), a single action.
     */
    @Override
    public int edgeSize() {
        return edgeCount;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return modeCount;
    }

    //equals, hashCode and toString methods automatically created with a few minor fixes.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DWGraph_DS)) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        if(nodeSize() != that.nodeSize()) return false;
        else {
            for (node_data n : Nodes.values()) {
                if(!n.equals(that.getNode(n.getKey()))) return false;
                for(edge_data e : getE(n.getKey())){
                    if(!e.equals(that.getEdge(e.getSrc(),e.getDest()))) return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Nodes, Edges);
    }

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "Nodes=" + Nodes +
                ", Edges=" + Edges +
                ", edgeCount=" + edgeCount +
                ", modeCount=" + modeCount +
                '}';
    }
}
