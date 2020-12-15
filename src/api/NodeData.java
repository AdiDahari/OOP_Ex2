package api;

import java.util.Objects;

public class NodeData implements node_data {

    private final int id;
    private static int _count = 0;
    private String _info;
    private int _tag;
    private double _weight;
    private geo_location pos;
    public NodeData(){
        id = _count++;
        _info = "";
        _tag = 0;
        _weight = Double.POSITIVE_INFINITY;
        pos = new GeoLocation();

    }
    public NodeData(int key){
        id = key;
        _info = "";
        _tag = 0;
        _weight = Double.POSITIVE_INFINITY;
        pos = new GeoLocation();
    }
    public NodeData(int key, geo_location loc){
        id = key;
        _info = "";
        _tag = 0;
        _weight = Double.POSITIVE_INFINITY;
        pos = loc;
    }
    public NodeData(int key, String info, int tag, double weight, geo_location gloc){
        id = key;
        _info = info;
        _tag = tag;
        _weight = weight;
        pos = gloc;
    }
    public NodeData(node_data n){
        id = n.getKey();
        _info = n.getInfo();
        _tag = n.getTag();
        _weight = n.getWeight();
        pos = n.getLocation();
    }

    @Override
    public int getKey() {
        return id;
    }

    @Override
    public geo_location getLocation() {
        return pos;
    }

    @Override
    public void setLocation(geo_location p) {
        pos = p;
    }

    @Override
    public double getWeight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    @Override
    public String getInfo() {
        return _info;
    }

    @Override
    public void setInfo(String s) {
        _info = s;
    }

    @Override
    public int getTag() {
        return _tag;
    }

    @Override
    public void setTag(int t) {
        _tag = t;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, pos);
    }

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