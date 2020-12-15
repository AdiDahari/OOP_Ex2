package api;

import java.util.Objects;

public class EdgeData implements edge_data {
    private int src, dest;
    private double w;
    private String _info;
    private int _tag;

    public EdgeData(int nsrc, int ndest, double nw){
        src = nsrc;
        dest = ndest;
        w = nw;
        _tag = 0;
        _info = "";
    }
    public EdgeData(int nsrc, int ndest, double nw, int ntag, String ninfo){
        src = nsrc;
        dest = ndest;
        w = nw;
        _tag = ntag;
        _info = ninfo;
    }
    public EdgeData(edge_data e){
        src = e.getSrc();
        dest = e.getDest();
        _info = e.getInfo();
        w = e.getWeight();
        _tag = e.getTag();
    }
    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return w;
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
        if (!(o instanceof EdgeData)) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src &&
                dest == edgeData.dest &&
                w == edgeData.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, w);
    }

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