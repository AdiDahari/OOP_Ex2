import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DWGraph_DSTest {
    public static directed_weighted_graph dwgraphCreator(int v_size, int e_size, double w){
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
    void getNode() {
        directed_weighted_graph g1 = dwgraphCreator(1,0,1);
        assertEquals(0, g1.getNode(0).getKey());
        g1.addNode(new NodeData(100));
        assertEquals(100, g1.getNode(100).getKey());
        assertEquals(2, g1.nodeSize());
    }

    @Test
    void getEdge() {
        directed_weighted_graph g2 = dwgraphCreator(10,9,1);
        assertEquals(1, g2.getEdge(0,1).getWeight());
        assertEquals(null, g2.getEdge(1,0));
        g2.connect(0,9,2000);
        assertEquals(2000,g2.getEdge(0,9).getWeight());
        assertEquals(null, g2.getEdge(9,0));

    }

    @Test
    void addNode() {
        directed_weighted_graph g3 = new DWGraph_DS();
        assertEquals(0, g3.nodeSize());
        g3.addNode(new NodeData());
        assertEquals(1, g3.nodeSize());
    }

    @Test
    void connect() {
        directed_weighted_graph g4 = dwgraphCreator(5,5,1);
        assertEquals(4, g4.edgeSize());
        g4.connect(4,0,1);
        assertEquals(5, g4.edgeSize());
        g4.connect(4,0,5);
        assertEquals(5, g4.edgeSize());
    }

    @Test
    void getV() {
        directed_weighted_graph g5 = new DWGraph_DS();
        assertEquals(0, g5.getV().size());
        g5 = dwgraphCreator(1000, 1000, 1);
        assertEquals(1000, g5.getV().size());
        assertEquals(1000, g5.nodeSize());
        g5.removeNode(500);
        assertEquals(999, g5.getV().size());
        assertEquals(999, g5.nodeSize());

    }

    @Test
    void getE() {
        directed_weighted_graph g6 = dwgraphCreator(1000,0,1);
        for(int i = 1; i < 999; i++){
            g6.connect(0,i,1);
        }
        assertEquals(998, g6.getE(0).size());
        for(int i = 1; i < 999; i++) {
            assertEquals(0, g6.getE(i).size());
        }


    }

    @Test
    void removeNode() {
        directed_weighted_graph g7 = new DWGraph_DS();
        g7.removeNode(10000);
        g7 = dwgraphCreator(1000,999,200);
        g7.removeNode(100000);
        int size = g7.nodeSize();
        for(int i = 0; i < size; i++){
            g7.removeNode(i);
        }
        assertEquals(0, g7.nodeSize());
        assertEquals(0, g7.getV().size());
        assertEquals(0, g7.edgeSize());
    }

    @Test
    void removeEdge() {
        directed_weighted_graph g8 = dwgraphCreator(100,10000000,1);
        assertEquals(99, g8.edgeSize());
        g8.removeEdge(98,99);
        g8.removeEdge(0,0);
        g8.removeEdge(0, 10000);
        assertEquals(98, g8.edgeSize());
        assertEquals(0, g8.getE(98).size());
    }

    @Test
    void nodeSize() {
        directed_weighted_graph g9 = new DWGraph_DS();
        assertEquals(0, g9.nodeSize());
        g9 = dwgraphCreator(1000000,1000000, 1);
        assertEquals(1000000, g9.nodeSize());

    }

    @Test
    void edgeSize() {
        directed_weighted_graph g10 = new DWGraph_DS();
        assertEquals(0, g10.edgeSize());
        g10 = dwgraphCreator(1000000,1000000, 1);
        assertEquals(999999, g10.edgeSize());
    }

    @Test
    void getMC() {
        directed_weighted_graph g11 = new DWGraph_DS();
        assertEquals(0, g11.getMC());
        for(int i = 0; i < 1000; i++){
            g11.addNode(new NodeData(i));
        }
        assertEquals(1000, g11.getMC());
        g11.removeNode(500);
        assertEquals(1001, g11.getMC());
        g11.removeNode(500);
        assertEquals(1001, g11.getMC());
    }
}
