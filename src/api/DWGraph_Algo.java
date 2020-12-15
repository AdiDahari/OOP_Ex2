package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements dw_graph_algorithms including:
 *  * 0. clone(); (copy)
 *  * 1. init(graph);
 *  * 2. isConnected(); // strongly (all ordered pais connected)
 *  * 3. double shortestPathDist(int src, int dest);
 *  * 4. List<node_data> shortestPath(int src, int dest);
 *  * 5. Save(file); // JSON file
 *  * 6. Load(file); // JSON file
 */
public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph _g;

    //[Constructors]
    public DWGraph_Algo(){
        _g = new DWGraph_DS();
    }
    public DWGraph_Algo(directed_weighted_graph g){
        _g = g;
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        _g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return _g;
    }

    /**
     * Compute a deep copy of this weighted graph,
     * using copy constructors made in each relevant implementation.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(_g);
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node, by checking a node's connection to each of the nodes in graph, reversing all edges
     * (dest<-->src), and checking connection of the same node again.
     * @return
     */
    @Override
    public boolean isConnected() {
        if(_g.nodeSize() == 1 || _g.nodeSize() == 0) return true;
        node_data n = _g.getV().iterator().next();
        boolean flag = DIJKSTRA.bfsConnection(_g, n);
        if(flag){
            directed_weighted_graph newg = new DWGraph_DS();
            for(node_data node : _g.getV()){
                newg.addNode(node);
            }
            for(node_data newnode : newg.getV()){
                for(edge_data e : _g.getE(newnode.getKey())){
                    newg.connect(e.getDest(),e.getSrc(),e.getWeight());
                }
            }
            return DIJKSTRA.bfsConnection(newg, n);
        }
        return false;
//        for(node_data n :_g.getV()){
//            boolean flag = DIJKSTRA.bfsConnection(_g, n);
//            if(!flag) return false;
//        }
//        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(src == dest) return 0.0;
        if(_g.nodeSize() == 0 || _g.getNode(src) == null || _g.getNode(dest) == null) return -1;
        return DIJKSTRA.dijkstraDist(_g, src, dest);
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest. if no such path - returns null.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(_g.nodeSize() == 0 || _g.getNode(src) == null || _g.getNode(dest) == null) return null;
        if(src == dest){
            List<node_data> ans = new LinkedList<>();
            ans.add(_g.getNode(src));
            return ans;
        }
        return DIJKSTRA.dijkstraPath(_g, src, dest);
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) throws JSONException {
        JSONArray Nodes = new JSONArray();
        JSONArray Edges = new JSONArray();
        for(node_data n : getGraph().getV()){
            JSONObject node = new JSONObject();
            String pos = n.getLocation().x()+","+n.getLocation().y()+","+n.getLocation().z();
            int key = n.getKey();
            node.put("pos", pos);
            node.put("id", key);
            Nodes.put(node);
            for(edge_data e : getGraph().getE(key)){
                JSONObject edge = new JSONObject();
                int src = e.getSrc();
                int dest = e.getDest();
                double w = e.getWeight();
                edge.put("src", src);
                edge.put("w", w);
                edge.put("dest", dest);
                Edges.put(edge);
            }
        }
        JSONObject graph = new JSONObject();
        graph.put("Edges", Edges);
        graph.put("Nodes", Nodes);
        try{
            File dirCheck = new File(file);
            if (dirCheck.getParent() != null && !(new File(dirCheck.getParent()).exists())) {
                System.out.println("Creating missing parent folders...");
                File dirs = new File(dirCheck.getParent());
                if (!dirs.mkdirs()) {
                    System.err.println("Could not create parent folders.");
                    return false;
                }
                dirCheck.delete();
            }
            System.out.println("Saving Directed Weighted Graph to a file...");
            FileWriter fw = new FileWriter(file);
            fw.write(graph.toString());
            fw.flush();
            fw.close();
            System.out.println("File successfully saved!");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try
        {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Deserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            DWGraph_DS graph = gson.fromJson(reader, DWGraph_DS.class);
            init(graph);
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //equals and hashCode methods automatically created with a few minor fixes.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DWGraph_Algo)) return false;
        DWGraph_Algo that = (DWGraph_Algo) o;
        return getGraph().equals(((DWGraph_Algo) o).getGraph());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_g);
    }
}
