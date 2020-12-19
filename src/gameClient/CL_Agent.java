package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

/**
 * This class implements an agent - a "Pokemon Trainer" that collects pokemon and receives their values.
 */
public class CL_Agent {
	public static final double EPS = 0.0001;
	private static int _count = 0;
	private static int _seed = 3331;
	private int _id;
	private geo_location _pos;
	private double _speed;
	private edge_data _curr_edge;
	private node_data _curr_node;
	private directed_weighted_graph _gg;
	private CL_Pokemon _curr_fruit;
	private long _sg_dt;
	private double _value;

	/**
	 * This is a graph and node Constructor.
	 * gets a graph and an int representing a node's key, and puts the agent ont the node in graph.
	 * @param g = the graph which the node is on.
	 * @param start_node = node's key.
	 */
	public CL_Agent(directed_weighted_graph g, int start_node) {
		_gg = g;
		setMoney(0);
		this._curr_node = _gg.getNode(start_node);
		_pos = _curr_node.getLocation();
		_id = -1;
		setSpeed(0);
	}

	/**
	 * This method updates an agents from a Json string.
	 * no changes made in this method.
	 * @param json
	 */
	public void update(String json) {
		JSONObject line;
		try {
			// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {_id = id;}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				double value = ttt.getDouble("value");
				this._pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This getter is for the current node which the agent is on.
	 * @return int = key of the current node.
	 */
	public int getSrcNode() {return this._curr_node.getKey();}

	/**
	 * This method converts an agent to a Json String.
	 * no changes made in this method.
	 * @return String = a Json format of the agent.
	 */
	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this._id+","
				+ "\"value\":"+this._value+","
				+ "\"src\":"+this._curr_node.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.getSpeed()+","
				+ "\"pos\":\""+_pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;
	}

	/**
	 * This setter sets the agent's current value, according to the pokemons he caught.
	 * @param v = value to be set.
	 */
	private void setMoney(double v) {_value = v;}

	/**
	 * This setter sets the next node of the agent, given by it's key,
	 * as well as setting the edge which connect the source node and the destination node.
	 * @param dest = destination node's key.
	 * @return true if such edge exists and the next node has been set properly
	 */
	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this._curr_node.getKey();
		this._curr_edge = _gg.getEdge(src, dest);
		if(_curr_edge!=null) {
			ans=true;
		}
		else {_curr_edge = null;}
		return ans;
	}

	/**
	 * This setter sets the current node of the agent.
	 * @param src == current node's key.
	 */
	public void setCurrNode(int src) {
		this._curr_node = _gg.getNode(src);
	}

	/**
	 * This method checks whether the agent is on the move or not,
	 * does so by checking if the current agent's edge is set. if so - the agent is moving.
	 * @return true if agent is moving, means by that, has a current edge.
	 */
	public boolean isMoving() {
		return this._curr_edge!=null;
	}

	/**
	 * This toString method uses the Json format of the agent.
	 * calls the toJSON method.
	 * @return String = representing the agent.
	 */
	public String toString() {
		return toJSON();
	}

	/**
	 * This method returns the current agent's key/ID.
	 * @return int = agent's key/ID
	 */
	public int getID() {
		return this._id;
	}

	/**
	 * This method returns the current location of the agent in geo_location format.
	 * @return geo_location = current agent's location.
	 */
	public geo_location getLocation() {
		return _pos;
	}

	/**
	 * This method returns the current agent's value.
	 * @return double = agent's value.
	 */
	public double getValue() {
		return this._value;
	}

	/**
	 * This method returns the next node of the agent,
	 * means - the destination node's key of the agent's edge.
	 * @return dest node's key if moving, else -1.
	 */
	public int getNextNode() {
		int ans = -2;
		if(this._curr_edge==null) {
			ans = -1;}
		else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	/**
	 * This method returns the current agent's speed.
	 * @return double = speed of agent.
	 */
	public double getSpeed() {
		return this._speed;
	}

	/**
	 * This method sets the agents current speed.
	 * @param v = new speed for agent.
	 */
	public void setSpeed(double v) {
		this._speed = v;
	}

}
