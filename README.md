![Title Logo](https://www.pikpng.com/pngl/m/31-319697_pokemon-logo-png-transparent-pokemon-logo-png-images.png)

# Pokemon Game

**Authors:** Tommy Goroh and Adi Dahari.



### Description
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


