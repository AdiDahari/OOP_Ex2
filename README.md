![Title Logo](https://www.pikpng.com/pngl/m/31-319697_pokemon-logo-png-transparent-pokemon-logo-png-images.png)

# Pokemon Game

**Authors:** Tommy Goroh and Adi Dahari.



## Description
This project is a Pokemon themed capture game, based on directed weighted graphs. 
The main propose of the game is to capture as many Pokemons as you can before the time runs out.
Each scenario of this game has a different amount of agents simultaneously going after the Pokemons on the graph.
Each Pokemon contains a value and located on an edge of the graph.
An agent that catches a Pokemon is being awarded by it's value.
By the end of each scenario of the game, the sum of all agents' values is the final result.

This project is a product of 2 layers:
1. Directed weighted graph implementation including:
	**A.** Node implementation 
	(interface: node_data.java, implementation: NodeData.java).
	**B.** Edge implementation 
	(interface: edge_data.java, implementation: EdgeData.java).
	**C.** Graph implementation using nodes and edges 
	(interface: directed_weighted_graph, implementation: DWGraph_DS.java).
	**D.** Relevant algorithms (such as finding the weight of a path between 2 nodes in graph)
	(interface: dw_graph_algorithms, implementation: DWGraph_Algo.java)
2. Game components implementation including:
	**A.** Game Manager which communicates with the server and transfers the relevant information during game run, as well as handling the thread of the game on the run.
	**B.** Arena which contains all of the game's elements such as Agents on mapand their parameters, Pokemons on map and their parameters.
	**C.** GUI which converts the game proccess to a visual output on screen. updates constantly.
	**D.** CL_Agent = the agents of the game, which collects value by capturing pokemons.
	**E.** CL_Pokemon = the ones that being captured by the agents, lies on edges of the graph.
	(CL_Pokemons compare defined in an outer class called PokeComp, Pokemons are being compared by their values.

## Structure
### api
#### Directed Weighted Graph
(DWGraph_DS.java - implements directed_weighted_graph.java)
##### Structure:
* Nodes = HashMap that contains all of the graph's nodes, mapped by their keys.
* Edges = HashMap with inner HashMaps mapped by node keys. each inner map for each node contains all of the edge_datas that contain the node as their source.
* edgeCount = counts the edges in graph. changes when a relevant method is called (adding an edge or deleting one).
* modeCount = counts the changes made in graph since initialized.
##### Constructors:
* Empty Constructor = creates a new empty graph by creating the main HashMaps.
* Nodes Constructor = creates a new graph using a given HashMap of nodes.
* Full Constructor = creates a graph by all relevant components given
(Nodes map, Edges map, edgeCount, modeCount)
* Copy Constructor = copies a given directed_weighted_graph to a new one, uses deep-copy methods maid in each relevant class.
##### Methods:
* getNode(int key) = returns the node_data of this graph, null if no such node in graph.
* getEdge(int src, int dest) = returns the edge_data by src node's key and dest node's key. null if no such edge.
* addNode(node_data n) = adds the given node to the graph.
* connect(int src, int dest, double w) = connects the src node to the dest node  (given by their keys) by a new edge with the given weight. does so only if both exists in graph.
* getV() = returns a shallow copy of all node_datas in graph as a Collection.
* getE(int node_id) = returns a shallow copy of all edges which has the node with the given key as their source node as a Collection.
* removeNode(int key) = if node (given by it's key) exists, removes all the edges associated with it in graph and deletes the node.
* removeEdge(int src, int dest) = if exists, removes the edge_data given by it's src and dest node keys.
* nodeSize() = returns the number of nodes in graph.
* edgeSize() = returns the number of edges in graph.
* getMC() = returns the modeCount of the graph.
* equals(Object o) = checks whether the given Object equals to this graph or not.
* toString() = Auto-generated toString method.

#### Algorithms
(DWGraph_Algo.java - implements dw_graph_algorithms.java)
##### Structure:
The algorithms class has only one element: a directed_weighted_graph.
##### Constructors:
* Empty Constructor = creates a new DWGraph_DS object within the new graph algo created.
* Graph Constructor = initializes a given graph as this graph.

##### Methods:
* init(directed_weighted_graph g) = initializes a given graph as this graph for running algorithms on it.
* getGraph() = returns the underlying graph.
* copy() = generates and returns a deep copy of the underlying graph using a deep copy methods made in each relevant class.
* isConnected() = checks whether the current graph is connected or not, using a BFS based algorithm made in DIJKSTRA class - once on the graph itself, second on a reversed copy of the graph (each edge src and dests are being switched).
* shortestPathDist(int src, int dest) = if such path exists, returns the lowest weighted path between the src and dest nodes given by their keys. using the method dijkstraDist of the DIJKSTRA class.
* shortestPath(int src, int dest) = if such path exists, returns an ordered list of nodes on the path between src and dest nodes given by their keys, using the method dijkstraPath of the DIJKSTRA class.
* save(String file) = saves the underlying graph as a JSON object to the given path on the computer.
returns true if saved successfully.
* load(String file) = loads a graph to the underlying graph given a JSON String.
if failed to load returns false and keeps the current underlying graph.
* equals(Object o) = Auto-generated and checks the equality between the underlying graphs of both DWGraph_Algo Objects.

### gameClient
#### Arena
This class contains and manages all of the current game elements.
##### Structure:
* srcs = List of all source node keys computed by Pokemons locations
* _gg = directed_weighted_graph of the current scenario.
* _agents = List of all agents of the current scenario.
* _pokemons = List of all Pokemons of the current scenario.
* _info = List of the scenario information Strings.

##### Constructors:
* Empty Constructor = initializes a new Arena.
* Full Constructor = initializes a new Arena with a given graph, lists of agents and pokemons.

##### Methods:
* setPokemons(List<CL_Pokemon> f) = sets the Pokemon list of the Arena to the given one.
* setAgents(List<CL_Agent> f) = sets the Agent list of the Arena to the given one.
* setGraph(directed_weighted_graph g) = sets the graph of this Arena to the given one.
* getAgents() = returns the current list of agents.
* getPokemons() = returns the current list of pokemons.
* getGraph() = returns the current graph of the arena.
* get_info() = returns the current list of  info Strings.
* set_info(List<String> _info) = sets the info List of ths arena to the given one.
* getAgents(String aa, directed_weighted_graph gg) = get a List of agents from a JSON String and updates them on the given graph.
* json2Pokemons(String fs) = returns a List of Pokemons parsed from a JSON String.
* updateEdge(CL_Pokemon fr, directed_weighted_graph g) = updates the Pokemons edge by their location.
* isOnEdge = methods that checks by different variations of edge elements given if a geo_location can be reffered as "On theEdge".
* GraphRange(directed_weighted_graph g) = converts the given graph to a 2D Range2D object, for the GUI.
* w2f(directed_weighted_graph g, Range2D frame) = converts a Range2D and a given graph to a Range2Range object.

#### GUI:
The Graphical User Interface implementation has 2 layers:
 ##### JFrame extension:
 An extension of the JFrame class to match the needed graph visualization.
 as the frame is the basic layer of the visual presentation, all changes made on graph is shown upod the graph itself.
 each node is shown as a dot on screen with it's key above.
 each edge is shown as a straight line between 2 nodes.
 at the game title bar there is a timer (seconds) that calculates the time until the scenario ends.
 the frame is implemented in a way that it can be resized and adjust to match the size in any formation.
 
 all relevant elements are being drawn according to their location and it's conversion to a 2-Dimentional frame.
  ##### JPanel extension:
An extension to the JPanel class to match the Agents-Pokemons themed game.
each Agent of the game is drawn as a pokeball.
pokemons are drawn according to their type as articuno or multres ( 2 legendary pokemons )
each agent's value is drawn by their id (0->1->2) order ont he upper-left side of the screen in a synchronized way to the game progress, as well as the sum of all of the values together.
## Ex2.java
This class is the main class of the whole game implementation, as it controls the Thread which runs the game, 
as well as controlling each movement made by an agent.
The Thread sleep time is changing through time according to the time left for each scenario.
The frame is updated constantly in order to present a nice and smooth visualization of the game on screen.
as the Login page is critical to the main String parameters, the JFrame extension of the login page is implemented as an inner class inside this one.
### run
This is the Thread control center, as it changes the sleep time according to the time left, calls the moveAgents method which controls the agents movement on graph,
refreshes the frame and panel of the GUI.
after a scenario ends it prints the result by game's info, number of moves and total value collected.
### moveAgents
This method handles the movement of the agents through the graph, by using the nextNode method.
does so by communicating with the Arena of the current scenario and the game server.
the method communicates with the game sercive by calling the chooseNextEdge function of the service, if and only if the destination node's key is not -1.
### nextNode
This is the main method which affects the value collection.
each entry to this method provides a source key, a graph and a agent's id.
by saving each agent's destination pokemon and the path to it, this method gets the agents to move as efficiently as possible without going after the same pokemon or the ones that will be collected on each path currently taken.
as each path of any agent starts with the current node which the agent is on, the method returns, after computing the right path for each agent, the next key of the next node of the path.
as a default, and without any conflicts, this method computes the most valued pokemon edge destiantion's key as the main target of the current agent.
if no move should be made the method returns -1.
### init
this method initializes all needed paramters of the scenario, as it called in the beginning of each scenario.
it does so by communicating with the server and getting all relevant information for the initialization of the scenario, such as the scenario's graph, amount of pokemons and agents.
in this method the arena is being initialized according to the scenario elements, received as JSON strings and parsed to the relevant format. 

### DIJKSTRA(Additional class):
This is an additionally made class containing 2 algorithm based on BFS and Dijkstra Algorithm.
### Method implemented:
#### dijkstraPath:
A method based on Dijkstra Algorithm.
this method going through every path possible and initializing every node in path by a HashMap<Integer, double> the weight of the path to it.
the implementation makes it possible for a node's map value to be updated if found a lower-weighted path to it, in that way it is promised that each node's map value will
contain the lowest-weighted path possible to it from the start node.
returns an ordered List of nodes which is the path on the graph form the source node to the destination node.
if no such path returns null.
each node distance from source is stored as a double and keyed by the node's key.


#### dijkstraDist:
Same algorithm as the dijkstraPath, only this time returns the weight (double) of the path between the 2 nodes.

#### bfsConnection:
this BFS-based algorithm uses boolean HashMap for marking visited nodes. it uses an Queue, inserted by the visited nodes and used to get all of node's neighbors. this method checks every possible path from a start node, by that assuring no node which have a connection to the start node isn't being checked. in the end of this method it checks the size of the boolean map against the graph's nodeSize, if the size is equal - all nodes are connected. else - not all nodes are connected and the boolean value "false" is returned. ( imported from ex0 and changed lightly to work with the new implementation of weighted_graph).


