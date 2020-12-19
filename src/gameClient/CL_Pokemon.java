package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

/**
 * This class represents a Pokemon for the game.
 * each Pokemon contains the following elements:
 * 1. _edge = edge_data - which the pokemon lies on.
 * 2. _value = double - the worth of the pokemon if captured.
 * 3. _type = int - 2 types of pokemons different by the position on edge -
 * 					if type < 1 the pokemon lies ont the edge from the higher keyed node, else reverted.
 * 4. _pos = Point3D - the location of the pokemon on a 3 dimensional grid (x,y,z).
 */
public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;

	/**
	 * This is a full constructor for a new pokemon.
	 * @param p = Point3D - location for the new pokemon.
	 * @param t = int - type for the new pokemon.
	 * @param v = double - value of the new pokemon.
	 * @param e = edge_data - edge for the new pokemon to lay on.
	 */
	public CL_Pokemon(Point3D p, int t, double v, edge_data e) {
		_type = t;
		_value = v;
		set_edge(e);
		_pos = p;
	}

	/**
	 * this is a toString method for the pokemon.
	 * @return String representing the pokemon.
	 */
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}

	/**
	 * This returns the current edge which the pokemon lies on.
	 * @return edge_data - pokemon's edge.
	 */
	public edge_data get_edge() {
		return _edge;
	}

	/**
	 * This returns the pokemon's edge destination node's key, according to it's type.
	 * @return int - node's key.
	 */
	public int getDestNode(){
		if(_type > 1){
			return Math.max(_edge.getDest(), _edge.getSrc());
		}
		return Math.min(_edge.getDest(), _edge.getSrc());
	}

	/**
	 * This returns the pokemon's edge source node's key, according to it's type.
	 * @return int - node's key.
	 */
	public int getSrcNode(){
		if(_type < 1){
			return Math.max(_edge.getDest(), _edge.getSrc());
		}
		return Math.min(_edge.getDest(), _edge.getSrc());
	}

	/**
	 * This method sets the new edge to the pokemon.
	 * @param _edge = edge_data to set for the pokemon.
	 */
	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	/**
	 * This method returns the location of the current pokemon in a Point3D format(x,y,z).
	 * @return Point3D - location of the pokemon.
	 */
	public Point3D getLocation() {
		return _pos;
	}

	/**
	 * This method returns the type of the current pokemon (1 / -1)
	 * @return int - type of the pokemon.
	 */
	public int getType() {return _type;}

	/**
	 * This method returns the value associated with the current pokemon.
	 * @return double - value of the pokemon.
	 */
	public double getValue() {return _value;}

}
