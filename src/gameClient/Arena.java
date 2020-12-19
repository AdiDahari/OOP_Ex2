package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implementation is for handling all of the game's parameters.
 * the arena contains the following elements:
 * 1. _gg = the directed_weighted_graph associated with the current scenario selected.
 * 2. _agents = List<CL_Agent> represents all the agents in game.
 * 3. _pokemons = List<CL_Pokemon> represents all the pokemons in game.
 * 4. _info = List<String> contains info of the current state of game.
 * 5. srcs = List<Integer> contains all pokemon's src node keys.
 */
public class Arena {
	private static List<Integer> srcs;
	public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private List<CL_Pokemon> _pokemons;
	private List<String> _info;


	/**
	 * This is an empty Constructor for the class.
	 * initializes the _info and srcs Lists.
	 */
	public Arena() {
		_info = new ArrayList<String>();
		srcs = new ArrayList<>();
	}

	/**
	 * This is a Constructor with 3 elements for a more specific construction of the new arena.
	 * the graph, agents and pokemons are being initialized in this constructor.
	 * @param g = directed_weighted_graph to be initialized in this arena.
	 * @param r = List<CL_Agent> to be initialized to this arena.
	 * @param p = List<CL_Pokemon> to be initialized to this arena.
	 */
	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		_gg = g;
		this.setPokemons(p);
		this.setAgents(r);
	}

	/**
	 * This method sets the pokemons list of this arena.
	 * after it does so, it uses the updateEdge method to associate each pokemon to the correct edge.
	 * @param f = List<CL_Pokemon> to set to this arena.
	 */
	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
		for(CL_Pokemon p : _pokemons){
			updateEdge(p, _gg);
			srcs.add(p.get_edge().getSrc());
		}
	}

	/**
	 * This method sets the agents list of this arena.
	 * @param f = List<CL_Agent> to set to this arena.
	 */
	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}

	/**
	 * This method sets the agents list of this arena.
	 * @param g = directed_weighted_graph to set to this arena.
	 */
	public void setGraph(directed_weighted_graph g) {this._gg =g;}

	/**
	 * This method returns the List of agents contains all agents in the current scenario
	 * @return List<CL_Agent> associated with this arena.
	 */
	public List<CL_Agent> getAgents() {return _agents;}

	/**
	 * This method returns the List of pokemons contains all agents in the current scenario
	 * @return List<CL_Pokemon> associated with this arena.
	 */
	public List<CL_Pokemon> getPokemons() {return _pokemons;}

	/**
	 * This method returns the graph of this arena.
	 * @return directed_weighted_graph = arena's graph.
	 */
	public directed_weighted_graph getGraph() {
		return _gg;
	}

	/**
	 * This method returns the info of the current arena.
	 * @return
	 */
	public List<String> get_info() {
		return _info;
	}

	/**
	 * This method sets the info of the current arena.
	 * @param _info = List<String> contains all info Strings to be set.
	 */
	public void set_info(List<String> _info) {
		this._info = _info;
	}

	////////////////////////////////////////////////////

	/**
	 * This method updates the agents on the graph by a Json String
	 * @param aa = String - json to parse for the update.
	 * @param gg = directed_weighted_graph - the graph to update agents on.
	 * @return List<CL_Agent> = updated List of the game's agents.
	 */
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {

				CL_Agent c = new CL_Agent(gg, 0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	/**
	 * This method returns an ArrayList of updated pokemon using a json string received from server.
	 * @param fs = String - json to be parsed and used to update all pokemons in game.
	 * @return ArrayList<CL_Pokemon> - updated list of all pokemon in game.
	 */
	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}

	/**
	 * This method update the relevant edge to each pokemon it gets, using the edge and pokemon location
	 * @param fr = CL_Pokemon to be updated.
	 * @param g = underlying graph of the current scenario.
	 */
	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {fr.set_edge(e);}
			}
		}
	}

	/**
	 * This method checks whether a given location is on an edge given by it's src and dest node keys.
	 * @param p = geo_location to be checked.
	 * @param src = geo_location represents the edge's source node.
	 * @param dest = geo_location represents the edge's destination node.
	 * @return true if the location is as close as EPS2 to the edge.
	 */
	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}

	/**
	 * This method checks whether a given location is on an edge given by it's src and dest node keys.
	 * calls the same signature method with locations.
	 * @param p = geo_location to be checked.
	 * @param s = int - edge source node's key.
	 * @param d = int - edge destination node's key.
	 * @param g = directed_weighted_graph which this edge belongs to.
	 * @return true if the location is as close as EPS2 to the edge by using first method.
	 */
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}

	/**
	 * This method checks whether a given location is on an edge given by it's src and dest node keys.
	 * calls the same signature method with locations.
	 * @param p = geo_location to be checked.
	 * @param e = edge_data to check with.
	 * @param type = int - represents the type of the edge, for associating with a relevant pokemon.
	 * @param g directed_weighted_graph which this edge belongs to.
	 * @return true if the location is as close as EPS2 to the edge by using first method.
	 */
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}

	/**
	 * this method returns a 2D range of the given graph.
	 * @param g = directed_weighted_graph to be updated.
	 * @return Range2D = Range of the given graph.
	 */
	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}

	/**
	 * This method is important for the graphic interface.
	 * converts the graph dimentions to a 2D ranged graph and by that enabling the GUI to create a visualization of the graph.
	 * @param g = directed_weighted_graph to be formatted.
	 * @param frame = Range2D of the frame.
	 * @return Range2Range - to be used by GUI.
	 */
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

}
