package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class is an extension of JFrame, containing an inner JPanel extension.
 * made for a graphic view of the graph and the game process.
 * as it shows a pokemon capture game:
 *  each agent is shown as a "Pokeball".
 *  2 types of pokemons are shown as articuno and multres (2 legendary pokemons).
 *  all relevant image files are stored in the "gui" folder.
 */
public class GUI extends JFrame {
    private Arena _ar;
    private Range2Range _w2f;
    private int n1;
//==========================[Panel]==========================//
    /**
     * This inner class extends JPanel.
     * each relevant parameter of the game is shown on screen.
     * as the game running the panel refreshes with the new elements of the game on screen.
     */
    public class Panel extends JPanel {

        public Panel() {
        }

        /**
         * This handles the presentation of the grades by value of each agents, and the total.
         * located ont the upper-left side of te frame.
         * @param g
         */
        private void drawGrades(Graphics g) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("Current Grades:", 10, 20);
            double total = 0;
            int y = 40;
            for (CL_Agent i : _ar.getAgents()) {
                total += i.getValue();
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                y = 40 + (20 * i.getID());
                g.drawString("Agent " + i.getID() + ":  " + i.getValue(), 20, y);
            }
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Current Total:"+ total, 10, y + 20);



        }

        /**
         * paintComponent override.
         * calls all the elements of the screen to be drawn.
         * @param g
         */
        public void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            g.clearRect(0, 0, w, h);
            if (_ar != null) {
                updateFrame();
                drawGraph(g);
                drawPokemons(g);
                drawAgents(g);
                drawInfo(g);
                drawGrades(g);
            }
            revalidate();
        }

        /**
         * a given method to show info of the game. taken from the MyFrame given as an example.
         * @param g
         */
        private void drawInfo(Graphics g) {
            List<String> str = _ar.get_info();
            String dt = "none";
            for (int i = 0; i < str.size(); i++) {
                g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
            }
        }

        /**
         * This handles the pokemons by their type.
         * each type is either articuno or multres shown on screen.
         * @param g
         */
        private void drawPokemons(Graphics g) {
            ImageIcon articuno = new ImageIcon("gui/articuno.png");
            ImageIcon multres = new ImageIcon("gui/multres.png");
            List<CL_Pokemon> p = _ar.getPokemons();

            if (p != null) {
                Iterator<CL_Pokemon> itr = p.iterator();

                while (itr.hasNext()) {

                    CL_Pokemon poke = itr.next();
                    Point3D c = poke.getLocation();
                    int r = 10;
                    if (c != null) {

                        geo_location loc = _w2f.world2frame(c);

                        if (poke.getType() < 0) {
                            g.drawImage(multres.getImage(), (int) loc.x() - r - 10, (int) loc.y() - r - 10, this);
                        } else
                            g.drawImage(articuno.getImage(), (int) loc.x() - r - 20, (int) loc.y() - r - 20, this);

                    }
                }
            }
        }

        /**
         * This handles the agents on screen.
         * each agent is represented as "PokeBall", as pokeballs stores pokemon.
         * @param g
         */
        private void drawAgents(Graphics g) {
            ImageIcon ball = new ImageIcon("gui/pokeball.png");
            List<CL_Agent> a = _ar.getAgents();
            g.setColor(Color.cyan);
            int i = 0;
            while (a != null && i < a.size()) {
                geo_location loc = a.get(i).getLocation();
                if (loc != null) {

                    geo_location fp = _w2f.world2frame(loc);
                    g.drawImage(ball.getImage(), (int) fp.x() - 20, (int) fp.y() - 20, this);
                }
                i++;
            }

        }

        //==========================[Panel - End]==========================//

    }

    /**
     * This is a String Constructor, extending the super constructor.
     * default close operation set for exiting on closure of the window.
     * @param a
     */
    public GUI(String a) {
        super(a);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * This is a String Constructor with dimension parameters, extending the super constructor.
     * dimensions given are being set for the size of the frame.
     * default close operation set for exiting on closure of the window.
     * @param s = String
     * @param w = Width
     * @param h = Height
     */
    public GUI(String s, int w, int h) {
        super(s);
        setSize(new Dimension(w, h));
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    /**
     * This updates the GUI class with an Arena for accessing the information of the graph, agents and pokemons locations.
     * @param ar = Arena
     */
    public void update(Arena ar) {
        _ar = ar;
        updateFrame();

    }

    /**
     * This paints on the frame if there is an arena initialized.
     * adds the panel to the frame and setting the icon image of the window.
     * revalidation performed for the painting layers to be correct by their hierarchy.
     * @param g
     */
    public void paint(Graphics g) {
        if (_ar != null) {
            Panel p = new Panel();
            add(p);
            setIconImage(new ImageIcon("gui/icon.png").getImage());
            revalidate();
        }
    }

    /**
     * This method updating frame for the correct dimensions according to the graph structure.
     * using the utils provided (util folder) and the w2f of Arena class to match the graph to the frame.
     */
    private void updateFrame() {
        Range rx = new Range(100, this.getWidth() - 40);
        Range ry = new Range(this.getHeight() - 50, 10);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    /**
     * This draws the Arena's graph on frame.
     * each node and edge are drawn using drawNode and drawEdge methods.
     * @param g
     */
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    /**
     * this draws a node in it's relative position on the it's location parameters.
     * @param n = node_data to be drawn.
     * @param r = int - a size of the node.
     * @param g
     */
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = _w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.setColor(Color.green.darker().darker());
        g.drawString("" + n.getKey(), (int)fp.x(), (int)fp.y() - 4*r);
    }

    /**
     * This draws an edge accirding to it's location parameters.
     * each edge lies between 2 different nodes.
     * @param e = edge_data to be drawn.
     * @param g
     */
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = _w2f.world2frame(s);
        geo_location d0 = _w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
    }

}
