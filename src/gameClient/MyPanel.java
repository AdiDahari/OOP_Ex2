package gameClient;

import gameClient.util.Point3D;
import api.*;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyPanel extends JPanel{
    private Arena _ar;
    private int _timer;
    private MyFrame _frame;
    private Range2Range _w2f;
    public MyPanel(MyFrame f){
        _frame = f;
    }

    protected void drawGrades(Graphics g){
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(Color.BLACK);
        double total = 0.0;
        g.drawString("Current Values: ",10,20);
        for(CL_Agent a : _ar.getAgents()){
            total += a.getValue();
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Agent " + a.getID() + "= " + a.getValue() + "$", 20, 40 + (30*a.getID()));
        }
        g.drawString("Total Agents Value = " + total, 10,60 + (_ar.getAgents().size()*30));
    }

    protected void drawTime(Graphics g){
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.red);
        g.drawString("Time Left: "+ _timer/1000 + "Seconds", 10, this.getWidth()-20);
    }

    protected void drawPokemons(Graphics g){
        List<CL_Pokemon> pokemons = _ar.getPokemons();
        ImageIcon multres = new ImageIcon("gui/multres.png");
        ImageIcon articuno = new ImageIcon("gui/articuno.png");
        if(pokemons != null){
            for(CL_Pokemon p : pokemons){
                Point3D pLoc = p.getLocation();
                if(pLoc != null){
                    geo_location loc = _w2f.world2frame(pLoc);
                    if(p.getType() > 0){
                        g.drawImage(articuno.getImage(), (int)loc.x()-20,(int)loc.y()-20,this);
                    }
                    else{
                        g.drawImage(multres.getImage(), (int)loc.x()-20,(int)loc.y()-20,this);
                    }
                }
            }
        }
    }
    protected void drawAgents(Graphics g){
        ImageIcon pokeball = new ImageIcon("gui/pokeball.jpg");
        List<CL_Agent> a = _ar.getAgents();
        if(a != null){
            for(CL_Agent agent : a){
                geo_location loc = agent.getLocation();
                if(loc != null){
                    loc = _w2f.world2frame(loc);
                    g.drawImage(pokeball.getImage(), (int)loc.x() -20, (int)loc.y()-20,this);
                }
            }
        }
    }
//    @Override
//    protected void paintComponent(Graphics g) {
//        _w2f = _frame.getW2f();
//        int w = getWidth(), h = getHeight();
//        g.clearRect(0,0,w,h);
//        if(_ar != null) {
//            drawGrades(g);
//            drawTime(g);
//            drawPokemons(g);
//            drawAgents(g);
//        }
//    }
}