package api;

import java.util.Objects;

/**
 * This class represents a node in a graph, each node contains:
 * 1. id = key value, no 2 nodes can have same key value in graph.
 * 2. _info = a String of information each node can store.
 * 3. _tag = Integer for tag marking.
 * 4. _weight = each node contains a weight value.
 * 5. pos = geo_location for marking relevant geographic location of each node.
 */
public class NodeData implements node_data {

    private final int id;
    private static int _count = 0;
    private String _info;
    private int _tag;
    private double _weight;
    private geo_location pos;

    /**
     * This is an empty constructor for the class.
     * creates a node using the _count static int - to avoid creating 2 nodes with the same key.
     */
    public NodeData(){
        id = _count++;
        _info = "";
        _tag = 0;
        _weight = Double.POSITIVE_INFINITY;
        pos = new GeoLocation();

    }

    /**
     * This is a key constructor for the class.
     * creates a new node by a given key.
     * @param key = new node's key.
     */
    public NodeData(int key){
        id = key;
        _info = "";
        _tag = 0;
        _weight = Double.POSITIVE_INFINITY;
        pos = new GeoLocation();
    }

    /**
     * This is a key and geo_location constructor for the class.
     * creates a new node by a given key.
     * @param key = new node's key.
     * @param loc = geo_location for the new node.
     */
    public NodeData(int key, geo_location loc){
        id = key;
        _info = "";
        _tag = 0;
        _weight = Double.POSITIVE_INFINITY;
        pos = loc;
    }

    /**
     * This is a full constructor for the class.
     * creates a new node using all the relevant parameters given.
     * @param key = int - new node's key.
     * @param info = String - info for the new node.
     * @param tag = double - tag value.
     * @param weight = double - weight value.
     * @param gloc = geo_location - location element for the new node.
     */
    public NodeData(int key, String info, int tag, double weight, geo_location gloc){
        id = key;
        _info = info;
        _tag = tag;
        _weight = weight;
        pos = gloc;
    }

    /**
     * This is a copy constructor for the class.
     * all parameters of the given class are copied to the new node created.
     * @param n = node_data to be copied.
     */
    public NodeData(node_data n){
        id = n.getKey();
        _info = n.getInfo();
        _tag = n.getTag();
        _weight = n.getWeight();
        pos = n.getLocation();
    }

    /**
     * This is a simple getter for the node's key.
     * @return int - key.
     */
    @Override
    public int getKey() {
        return id;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return pos;
    }

    /**
     * Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        pos = p;
    }

    /**
     * Returns the weight associated with this node.
     * @return
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    @Override
    public String getInfo() {
        return _info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        _info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return _tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag = t;
    }

    /**
     * Auto-generated equals function for the class, with a few changes to match the class important components.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeData)) return false;
        NodeData nodeData = (NodeData) o;
        return id == nodeData.id &&
                pos.x() == nodeData.pos.x()&&
                pos.y() == nodeData.pos.y()&&
                pos.z() == nodeData.pos.z();
    }

    /**
     * Auto-generated hashCode function for the class, with a few changes to match the class important components.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, pos);
    }

    /**
     * Auto-generated toString function for this class.
     * @return
     */
    @Override
    public String toString() {
        return "NodeData{" +
                "id=" + id +
                ", _info='" + _info + '\'' +
                ", _tag=" + _tag +
                ", _weight=" + _weight +
                ", pos=" + pos +
                '}';
    }
}