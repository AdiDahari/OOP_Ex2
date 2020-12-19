import Server.Game_Server_Ex2;
import api.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameClient.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * This class is the main class of the Pokemon Game.
 * each game is implemented as a thread including a GUI and run method.
 * the initialization of the game is supporting 2 options:
 * 1. log in through a graphic login page with scenario number (0-23) and id number.
 * 2. log in through command prompt by the command: java -jar Ex2.jar [*id number*] [*scenario number*]
 * as Agents' speed is changing according to their value, for preventing useless moves the sleep time of the game's thread is changing through time.
 * class structure:
 * 1. paths = HashMap containing each agent's current path as a list of node_data, for optimizing the agents' collaboration.
 * 2. dests = HashMap containing each agent's current destination pokemon, for preventing 2 agents going after the same pokemon together.
 * 3.a. _id = i.d number for logging in to ther server. b. _scNum = the scenario of the current run of the game [0 - 23]
 * 4. _gui = GUI for showing the game on screen as it runs.
 * 5. _ar = Arena to maintain the game elements as the game runs.
 * class methods:
 * 1. run = the thread maintenance for the game, controls the sleep time and the communication with the provided game server.
 * 2. moveAgents = handling the Agents movements as the game runs.
 * 3. nextNode = the main decision making algorithm for each agent. considers the current pokemons and agents locations and each agent's path.
 *               each agent next node is selected by this method according to all relevant parameters.
 * init = initializing the game's arena and by using a json parsing functions. builds the graph according to the server information for each scenario by using the classes implemented in the api package.
 * Login (class) = the implementation of the GUI for the login page of the game. using JTextField and a graphic button with ActionListener - implemented using lambda expressions.
 */
public class Ex2 implements Runnable {
    private static HashMap<Integer, List<node_data>> paths;
    private static HashMap<Integer, CL_Pokemon> dests;
    private static int _id;
    private static int _scNum;
    private static GUI _gui;
    private static Arena _ar;

    /**
     * The main of the game, handles the login information and initializing the GUI.
     * if no information provided from cmd it calls the login page and gets the needed info from it.
     * @param a = String array for the login information. the way to login with cmd is: java -jar Ex2.jar [*id*] [*scenario*]
     */
    public static void main(String[] a) {
        if(a.length != 2) {
            _gui = new GUI("Login Page", 350,300);
            _gui.setResizable(false);
            _gui.add(new Login());
            _gui.setVisible(true);

        }
        else {
            _id = Integer.parseInt(a[0]);
            _scNum = Integer.parseInt(a[1]);
            dests = new HashMap<>();
            paths = new HashMap<>();
            Thread client = new Thread(new Ex2());
            client.start();
        }
    }

    /**
     * this is the thread's control element.
     * decides when the thread sleeps and maintaining connection with the server.
     */
    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(_scNum); // you have [0,23] games

        game.login(_id);
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
        _gui.setTitle("Ex2 - Pokemon Game"+game.timeToEnd()/1000 + "s");
        int time2end = (int)game.timeToEnd();
        int ind=0;
        int dt = 250;
        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%1==0) {
                    _gui.repaint();
                    _gui.setTitle("Ex2 - Pokemon Game - Time to end: "+game.timeToEnd()/1000 + "s" );
                }
                if(dt == 50) dt = 55;
                else if(dt == 55) dt = 65;
                else if(dt == 65) dt = 71;
                else if(dt == 71) dt = 76;
                else if((double) game.timeToEnd()/(double)time2end< 0.2) dt = 50;
                else if((double) game.timeToEnd()/(double)time2end< 0.3) dt = 55;
                else if((double) game.timeToEnd()/(double)time2end< 0.4) dt = 60;
                else if((double) game.timeToEnd()/(double)time2end< 0.5) dt = 90;
                else if((double) game.timeToEnd()/(double)time2end< 0.6) dt = 140;
                else if((double) game.timeToEnd()/(double)time2end< 0.7) dt = 150;
                else if((double) game.timeToEnd()/(double)time2end< 0.8) dt = 160;
                else if((double) game.timeToEnd()/(double)time2end< 0.9) dt = 200;

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
     * this is the agents controller, handles the movement of the agents through the graph.
     * using nextNode method for getting the next movement for each agent.
     * @param game = the game service for communication with the server
     * @param gg = the graph of the current scenario
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
            if(dest==-1 && !ag.isMoving()) {
                dest = nextNode(gg, src, id);
                if(dest == -1) continue;
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
            }
        }
    }

    /**
     * This is the main method of the game's idea.
     * it chooses for each agent the right node to go to, according to the position of all agents and pokemons.
     * as it gets the agent's key, the graph and the current node of the agents, it uses the methods maid in api class.
     * the given graph is initialized in graph algorithms object for performing checks according to each path for the agent
     * the main idea of this method is to point the agent to the best movement possible for achieving best value.
     * for each pokemon on current position if the game, it computes the closest agent to it and adressing the agent to the closest pokemon.
     * @param g = the graph of the current scenario.
     * @param src = start node's key.
     * @param agKey = agent's key (or ID).
     * @return int = the best next node for the agent to go to.
     */
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
            boolean flag = false;
            for(CL_Agent agent : a){
                if(agent.getID() != agKey && paths.containsKey(agent.getID())){
                    if(paths != null && paths.containsKey(agent.getID()) && paths.get(agent.getID()).contains(g.getNode(poke.get_edge().getDest()))) flag = true;
                }
            }
            if(flag) continue;
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
        if(path != null && path.size() > 1){
            paths.put(agKey, path);
            return path.get(1).getKey();
        }
        else return minSrc;
    }

    /**
     * The initialization of the game.
     * as it gets all information needed as JSON strings,
     * it sets a new arena using this information.
     * for the most efficient initialization, the pokemons are sorted by their values and the agents are placed according to the best pokemons (relatively to their value).
     * @param game = game service of the current scenario selected.
     */
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
        _gui = new GUI("test Ex2");
        _gui.setSize(1000, 700);
        _gui.update(_ar);

        _gui.show();
//        _win.setVisible(true);
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
                int nn = c.getSrcNode();
                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }




    /**
     * JPanel extension for a login interface
     * using JTextField and lambda expression for providing the main class the needed information (scenario number and ID).
     * start button invoking the lambda expression and closing the current gui and then opens the game itself in a new gui.
     */
    public static class Login extends JPanel {
        private ImageIcon bg = new ImageIcon("gui/background.jpg");

        /**
         * paintComponenets is extended to have a nice background for the login page.
         * @param g = Graphics
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image background  = bg.getImage();
            g.drawImage(background,0,0, getWidth(),getHeight(),null);

        }

        /**
         * this is the implementation of the login page, using the GUI class created for graphic interface.
         *
         */
        public Login(){

            super();
            setSize(500,500);
            setLayout(null);
            JLabel scNum = new JLabel("Scenario");
            scNum.setBounds(50,100,60,25);
            scNum.setFont(new Font("Arial", Font.PLAIN, 15));
            scNum.setForeground(Color.black);
            scNum.setBackground(Color.white);
            scNum.setOpaque(true);
            add(scNum);
            JTextField scIn = new JTextField();
            scIn.setFont(new Font("Arial", Font.PLAIN, 10));
            scIn.setBounds(150, 103,150,20);
            add(scIn);
            JLabel idNum = new JLabel("I.D.");
            idNum.setBounds(50,150,60,25);
            idNum.setForeground(Color.black);
            idNum.setBackground(Color.white);
            idNum.setOpaque(true);
            add(idNum);
            JTextField idIn = new JTextField();
            idIn.setFont(new Font("Arial", Font.PLAIN, 10));
            idIn.setBounds(150, 153,150,20);
            add(idIn);
            JButton start = new JButton("START");
            start.setFont(new Font("Arial", Font.ITALIC, 18));
            start.setBounds(120,190,100,20);
            start.setForeground(Color.white);
            start.setBackground(Color.green.darker().darker());
            start.setBorder(BorderFactory.createLineBorder(Color.black,2));
            add(start);
            start.addActionListener(e ->{    //lambda expression for performing the initialization of the game according rhe provided id and scenario number.
                String id = idIn.getText();
                String sc = scIn.getText();
                String args[] = {id,sc};
                main(args);
            });
            start.addActionListener(e -> _gui.setVisible(false));  //closing the login page frame as it no longer necessary.
        }
    }

}
