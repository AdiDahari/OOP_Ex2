package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
	private int _ind;
	private Arena _ar;
	private static MyPanel _panel;
	private Range2Range _w2f;

	MyFrame(String a) {
		super(a);
		int _ind = 0;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public MyFrame(String a, int w, int h) {
		super(a);
		this.setSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();

	}

	protected void updateFrame() {
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
	}
	public void paint(Graphics g) {
		if(_ar != null) {
			_panel = new MyPanel(this);
			add(_panel);
			revalidate();
		}
	}
//	@Override
//	public void paintComponents(Graphics g){
//		int w = getWidth(), h = getHeight();
//		g.clearRect(0,0,w,h);
//		if(_ar != null) {
//			updateFrame();
//			drawGraph(g);
//			_panel.paintComponent(g);
//			revalidate();
//		}
//
//	}
	@Override
	public void paintComponents(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		updateFrame();
		drawGraph(g);
		_panel.drawPokemons(g);
		_panel.drawAgents(g);
		drawInfo(g);
		revalidate();
	}
	protected void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}
		
	}
	protected void drawGraph(Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.blue);
			drawNode(n,5,g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while(itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
	}

	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this._w2f.world2frame(pos);
		g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}
	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
	//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}
	public Arena getAr(){
		return _ar;
	}
	public Range2Range getW2f(){
		return this._w2f;
	}
}
