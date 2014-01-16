package trip;
/** the Location class.
* @author Ian Fox*/
public class Road implements graph.Weightable {
    /** the location.*/
    private String _location;
    /** the location _from.*/
    private Location _from;
    /** the location _to.*/
    private Location _to;
    /** the length of the road.*/
    private double _dist;
    /** the direction of the road.*/
    private String _direct;
    /**creates a new LOCATION.*/
    public void setRoad(String location) {
        _location = location;
    }
    /** sets the X coordinates of the location.*/
    public void setL0(Location x) {
        _from = x;
    }
    /** set the Y coordinates of the location.*/
    public void setL1(Location y) {
        _to = y;
    }
    /** sets the distance of this road to D.*/
    public void setDistance(Double d) {
        _dist = d;
    }

    /** sets the direction of this road to DIRECT.*/
    public void setDirection(String direct) {
        _direct = direct;
    }
    /** return the LOCATION where this comes from.*/
    public Location getFrom() {
        return _from;
    }
    /** returns the LOCATION this goes to.*/
    public Location getTo() {
        return _to;
    }
    /** returns the STRING direction of this edge.*/
    public String getDirect() {
        return _direct;
    }
    /** return the DOUBLE length of this road.*/
    public Double getDist() {
        return _dist;
    }

    @Override
    public void setWeight(double w) {
        _dist = w;
    }
    @Override
    public double weight() {
        return _dist;
    }
    /** @return string .*/
    public String getName() {
        return _location;
    }
}
