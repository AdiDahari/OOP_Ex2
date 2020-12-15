package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Ex2 implements Runnable {
    private static HashMap<Integer, CL_Pokemon> dests;
    private static MyFrame _win;
    private static Arena _ar;
    public static void main(String[] a) {
        dests = new HashMap<>();
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 0;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        dw_graph_algorithms ga = new DWGraph_Algo();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Deserializer());
        Gson gson = builder.create();
        DWGraph_DS graph = gson.fromJson(game.getGraph(), DWGraph_DS.class);
        directed_weighted_graph gg = graph;
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - Pokemon Game"+game.timeToEnd()/1000 + "s");
        int time2end = (int)game.timeToEnd();
        int ind=0;
        long dt= 150;

        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%1==0) {
                    _win.repaint();
                    _win.setTitle("Ex2 - Pokemon Game - Time to end: "+game.timeToEnd()/1000 + "s");
                }
                if((double)time2end/(double) game.timeToEnd()< 0.3) dt = 50;
                else if((double)time2end/(double) game.timeToEnd()< 0.6) dt = 100;

                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }
    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if(dest==-1) {
                dest = nextNode(gg, src, id);
                if(dest == -1) continue;
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
            }
        }
    }

    private static int nextNode(directed_weighted_graph g, int src, int agKey) {
        dw_graph_algorithms ga = new DWGraph_Algo(g);
        int ans = -1;
        double minDist = Double.POSITIVE_INFINITY;
        int minDest = -1;
        int minSrc = -1;
        CL_Pokemon pokeDest = null;
        List<CL_Pokemon> p = _ar.getPokemons();
        List<CL_Agent> a = _ar.getAgents();
        for(CL_Pokemon poke : p){
            _ar.updateEdge(poke, g);

        }
        for(CL_Pokemon poke : p){
            for(CL_Agent agent : a){
                if(agent.getID() != agKey && poke == agent.get_curr_fruit()) continue;
            }
            double val = poke.getValue();
            if(val < 1) continue;
            if(dests.values().contains(poke)) continue;
            else if(ga.shortestPathDist(src, poke.get_edge().getDest()) < minDist) {
                minDist = ga.shortestPathDist(src, poke.get_edge().getDest());
                minDest = poke.get_edge().getDest();
                minSrc = poke.get_edge().getSrc();
                pokeDest = poke;
            }
        }
        if(pokeDest != null) dests.put(agKey, pokeDest);
        List<node_data> path = ga.shortestPath(src, minDest);
        for(CL_Agent agent : a){
            if(agent.getID() == agKey) agent.set_curr_fruit(pokeDest);
            if(agent.getID() != agKey && isOnPath(pokeDest, path, g)) return -1;
            if(path.size()>1 && agent.getID() != agKey && agent.getNextNode() == path.get(1).getKey())return -1;
            if(path.size()>1 && agent.getID() != agKey && agent.getSrcNode() == path.get(1).getKey())return -1;
        }
        if(path.size() > 1) return path.get(1).getKey();
        else return minSrc;
    }
    private void init(game_service game) {
        String fs = game.getPokemons();
        String g = game.getGraph();
        String pks = game.getPokemons();
        dw_graph_algorithms ga = new DWGraph_Algo();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Deserializer());
        Gson gson = builder.create();
        DWGraph_DS graph = gson.fromJson(g, DWGraph_DS.class);
        directed_weighted_graph gg = graph;
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);


        _win.setVisible(true);
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            cl_fs.sort(new PokeComp());
            for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }
    private static boolean isOnPath(CL_Pokemon p, List<node_data> path, directed_weighted_graph g){
        for(int i = 0; i < path.size()-2; i++){
            if(p.get_edge() == g.getEdge(path.get(i).getKey(), path.get(i+1).getKey())) return true;
        }
        return false;
    }
}
