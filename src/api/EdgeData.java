package api;

import java.util.Objects;

/**
 * This interface represents the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 *
 */
public class EdgeData implements edge_data {
    private int src, dest;
    private double w;
    private String _info;
    private int _tag;

    /**
     * This is a constructor that creates a new edge using a given source and destination node's keys and a weight value.
     * @param nsrc = int - src node's key.
     * @param ndest = int - dest node's key.
     * @param nw = double - new edge's weight.
     */
    public EdgeData(int nsrc, int ndest, double nw){
        src = nsrc;
        dest = ndest;
        w = nw;
        _tag = 0;
        _info = "";
    }

    /**
     * This is a full constructor for this class.
     * @param nsrc = int - src node's key.
     * @param ndest = int - dest node's key.
     * @param nw = double - new edge's weight.
     * @param ntag = int - tag value for the new edge.
     * @param ninfo = String - info for the new edge.
     */
    public EdgeData(int nsrc, int ndest, double nw, int ntag, String ninfo){
        src = nsrc;
        dest = ndest;
        w = nw;
        _tag = ntag;
        _info = ninfo;
    }

    /**
     * This is a copy constructor for the class.
     * each parameter of the given edge is being copied to the ne edge created.
     * @param e = edge_data - edge  to be copied to the new one.
     */
    public EdgeData(edge_data e){
        src = e.getSrc();
        dest = e.getDest();
        _info = e.getInfo();
        w = e.getWeight();
        _tag = e.getTag();
    }

    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return src;
    }

    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return dest;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return w;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return _info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
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
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag = t;
    }

    /**
     * Auto-generated equals method with a few changes to match the important elements of the edge class.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeData)) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src &&
                dest == edgeData.dest;
    }

    /**
     * Auto-generated hashCode method with a few changes to match the important elements of the edge class.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(src, dest, w);
    }

    /**
     * Auto-generated toString method for this class.
     * @return
     */
    @Override
    public String toString() {
        return "EdgeData{" +
                "_src=" + src +
                ", _dest=" + dest +
                ", _weight=" + w +
                ", _info='" + _info + '\'' +
                ", _tag=" + _tag +
                '}';
    }
}