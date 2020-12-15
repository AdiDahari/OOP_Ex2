import api.*;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DWGraph_AlgoTest {
    public directed_weighted_graph dwgraphCreator(int v_size, int e_size, double w){
        directed_weighted_graph g = new DWGraph_DS();
        for(int i = 0; i < v_size; i++){
            g.addNode(new NodeData(i));
        }
        for(int i = 0; i < e_size; i++){
            g.connect(i, i+1, w);
        }
        return g;
    }
    @Test
    void init() {
        directed_weighted_graph g1 = dwgraphCreator(0,0,0);
        dw_graph_algorithms ga1 = new DWGraph_Algo();
        ga1.init(g1);
        assertEquals(0, ga1.getGraph().nodeSize());
        assertEquals(ga1.getGraph(), g1);
        ga1.init(dwgraphCreator(1000,999,1));
        assertEquals(1000, ga1.getGraph().nodeSize());

    }

    @Test
    void copy() {
        dw_graph_algorithms g2_1 = new DWGraph_Algo(dwgraphCreator(100000,1,200));
        dw_graph_algorithms g2_2 = new DWGraph_Algo(g2_1.copy());
        assertEquals(g2_1,g2_2);
        node_data n = g2_1.getGraph().getV().iterator().next();
        g2_2.getGraph().removeNode(n.getKey());
        assertNotEquals(g2_1,g2_2);
        g2_2.getGraph().addNode(new NodeData(1000000));
        assertNotEquals(g2_1,g2_2);
        g2_2.getGraph().removeNode(1000000);
        g2_2.getGraph().addNode(new NodeData(0));
        g2_2.getGraph().connect(n.getKey(),n.getKey()+1,200);
        assertTrue(g2_1.equals(g2_2));

    }

    @Test
    void isConnected() {
        dw_graph_algorithms g3 = new DWGraph_Algo();
        assertTrue(g3.isConnected());
        g3.getGraph().addNode(new NodeData(0));
        assertTrue(g3.isConnected());
        g3 = new DWGraph_Algo(dwgraphCreator(1000,999,1));
        assertFalse(g3.isConnected());
        g3.getGraph().connect(998,0,1);
        assertFalse(g3.isConnected());
        g3.getGraph().connect(999,0,1);
        assertTrue(g3.isConnected());
    }

    @Test
    void shortestPathDist() {
        dw_graph_algorithms g4 = new DWGraph_Algo();
        g4.init(dwgraphCreator(5,4,1));
        assertEquals(4,g4.shortestPathDist(0,4));
        assertEquals(-1, g4.shortestPathDist(4,0));
        assertEquals(0,g4.shortestPathDist(0,0));
        g4.getGraph().connect(4,0,1);
        assertEquals(4, g4.shortestPathDist(4,3));
        assertEquals(4, g4.shortestPathDist(3,2));
        assertEquals(4, g4.shortestPathDist(2,1));
        assertEquals(4, g4.shortestPathDist(1,0));

    }

    @Test
    void shortestPath() {
        dw_graph_algorithms g5 = new DWGraph_Algo();
        System.out.println(g5.shortestPath(0,0));
        g5.init(dwgraphCreator(10,9,1));
        List<node_data> path = g5.shortestPath(0,9);
        for(node_data n : path){
            System.out.print(n.getKey());
            if(n != path.get(path.size()-1)) System.out.print(" --> ");
        }
    }

    @Test
    void save() throws JSONException {
        dw_graph_algorithms g6 = new DWGraph_Algo();
        g6.init(dwgraphCreator(100,99,1));
        g6.save("C:/Users/Adi Dahari/Desktop/Test/New Folder Test/graph.json");
    }

    @Test
    void load() throws JSONException {
        dw_graph_algorithms g7_1 = new DWGraph_Algo();
        g7_1.init(dwgraphCreator(100,99,1));
        g7_1.save("C:/Users/Adi Dahari/Desktop/Test/graph.json");
        dw_graph_algorithms g7_2 = new DWGraph_Algo();
        g7_2.load("C:/Users/Adi Dahari/Desktop/Test/graph.json");
        assertTrue(g7_1.equals(g7_2));
        System.out.println(g7_1.getGraph());
        System.out.println(g7_2.getGraph());
        g7_1.load("C:/Users/Adi Dahari/Desktop/Ex2/ex2/data/A5");
        System.out.println("");
        System.out.println(g7_1.getGraph());

    }
}
