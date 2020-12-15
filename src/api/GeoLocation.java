package api;

public class GeoLocation implements geo_location{
    private double _x, _z, _y;
    public GeoLocation(){
        _x = 0;
        _y = 0;
        _z = 0;

    }
    public GeoLocation(double x, double y, double z){
        _x = x;
        _y = y;
        _z = z;
    }
    @Override
    public double x() {
        return _x;
    }

    @Override
    public double y() {
        return _y;
    }

    @Override
    public double z() {
        return _z;
    }

    @Override
    public double distance(geo_location g) {
        double d = ((Math.pow((g.x() - _x), 2))+(Math.pow((g.y() - _y), 2))+(Math.pow((g.z() - _z), 2)));
        return Math.sqrt(d);

    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "_x=" + _x +
                ", _y=" + _y +
                ", _z=" + _z +
                '}';
    }
}
