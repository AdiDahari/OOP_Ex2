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

public class Ex2 implements Runnable {
    private static HashMap<Integer, List<node_data>> paths;
    private static HashMap<Integer, CL_Pokemon> dests;
    private static int _id;
    private static int _scNum;
    private static GUI _gui;
    private static Arena _ar;
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

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(_scNum); // you have [0,23] games
        //game.login(_id);
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
        int dt = 200;
        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%1==0) {
                    _gui.repaint();
                    _gui.setTitle("Ex2 - Pokemon Game - Time to end: "+game.timeToEnd()/1000 + "s" );
                }
                if(dt == 50) dt = 55;
                else if(dt == 55) dt = 61;
                else if(dt == 61) dt = 71;
                else if(dt == 71) dt = 85;
                else if((double) game.timeToEnd()/(double)time2end< 0.2) dt = 50;
                else if((double) game.timeToEnd()/(double)time2end< 0.4) dt = 60;
                else if((double) game.timeToEnd()/(double)time2end< 0.5) dt = 70;
                else if((double) game.timeToEnd()/(double)time2end< 0.6) dt = 80;
                else if((double) game.timeToEnd()/(double)time2end< 0.7) dt = 100;
                else if((double) game.timeToEnd()/(double)time2end< 0.8) dt = 150;
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
            if(dest==-1 && !ag.isMoving()) {
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
//            if(path.size()>1 && agent.getID() != agKey && agent.getSrcNode() == path.get(1).getKey())return -1;
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

    /**
     * JPanel extension for a login interface
     * using JTextField and lambda expression for providing the main class the needed information (scenario number and ID).
     * start button invoking the lambda expression and closing the current gui and then opens the game itself in a new gui.
     */
    public static class Login extends JPanel {
        private ImageIcon bg = new ImageIcon("gui/background.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image background  = bg.getImage();
            g.drawImage(background,0,0, getWidth(),getHeight(),null);

        }

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
            start.addActionListener(e ->{
                String id = idIn.getText();
                String sc = scIn.getText();
                String args[] = {id,sc};
                main(args);
            });
            start.addActionListener(e ->{ final int scNumIn = Integer.parseInt(scIn.getText());});
            start.addActionListener(e -> _gui.setVisible(false));
        }
    }

}
