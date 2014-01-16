package graph;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class GraphsTesting {

    private class TestWeight implements  Weighter<String> {
        private HashMap<String, Double> _distances = new
                HashMap<String, Double>();
        @Override
        public void setWeight(String x, double v) {
            _distances.put(x, v);
        }
        @Override
        public double weight(String x) {
            return _distances.get(x);
        }
    }
    @Test
    public void testPath() {
        UndirectedGraph<String, String> g =
            new UndirectedGraph<String, String>();
        TestWeight test = new TestWeight();
        Graph<String, String>.Vertex v1 = g.add("1");
        Graph<String, String>.Vertex v2b = g.add("2 preferd");
        Graph<String, String>.Vertex v2 = g.add("2");
        Graph<String, String>.Vertex v3 = g.add("3");
        Graph<String, String>.Vertex v4 = g.add("4");
        Graph<String, String>.Edge eOnetoTwo = g.add(v1, v2, "1->2");
        Graph<String, String>.Edge eOnetoTwob = g.add(v1, v2b, "1->2b");
        test.setWeight((String) eOnetoTwob.getLabel(), 1.1);
        test.setWeight((String) eOnetoTwo.getLabel(), 1);
        Graph<String, String>.Edge eTwotoThree = g.add(v2, v3, "2->3");
        test.setWeight((String) eTwotoThree.getLabel(), .5);
        Graph<String, String>.Edge eTwoAlttoThree = g.add(v2b, v3, "2b -> 3");
        test.setWeight((String) eTwoAlttoThree.getLabel(), 1.1);
        Graph<String, String>.Edge eThreetoFour = g.add(v3, v4, "3->4");
        test.setWeight((String) eThreetoFour.getLabel(), 1);
        ArrayList<Graph<String, String>.Edge> output =
                new ArrayList<Graph<String, String>.Edge>();
        output.add(eOnetoTwo);
        output.add(eTwotoThree);
        output.add(eThreetoFour);
        System.out.println(Graphs.shortestPath(g, v1,
                        v4, Graphs.ZERO_DISTANCER, test, test));
    }
    @Test
    public void testCrazyPath() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        TestWeight test = new TestWeight();
        Graph<String, String>.Vertex a = g.add("a");
        Graph<String, String>.Vertex b = g.add("b");
        Graph<String, String>.Vertex c = g.add("c");
        Graph<String, String>.Vertex d = g.add("d");
        Graph<String, String>.Vertex e = g.add("e");
        Graph<String, String>.Vertex f = g.add("f");
        Graph<String, String>.Vertex fprime = g.add("fprime");
        Graph<String, String>.Vertex h = g.add("h");
        Graph<String, String>.Vertex i = g.add("i");
        Graph<String, String>.Vertex j = g.add("j");
        Graph<String, String>.Vertex k = g.add("k");
        Graph<String, String>.Vertex l = g.add("l");
        Graph<String, String>.Vertex goal = g.add("goal");
        setEdge("a->b", g, SEVEN, a, b, false, test);
        setEdge("a->d", g, TEN, a, d, false, test);
        setEdge("a->c", g, 5, a, c, true, test);
        setEdge("c->f", g, TEN + 5, c, f, false, test);
        setEdge("d->c", g, 2, d, c, false, test);
        setEdge("c->b", g, 1, c, b, true, test);
        setEdge("c->e", g, 4, c, e, false, test);
        setEdge("c->d", g, 2, c, d, false, test);
        setEdge("d->e", g, SEVEN, d, e, false, test);
        setEdge("b->e", g, 2, b, e, true, test);
        setEdge("e->f", g, 4, e, f, true, test);
        setEdge("e->goal", g, TEN * TEN, e, goal, false, test);
        setEdge("f->f'", g, 2, f, fprime, true, test);
        setEdge("f'->h", g, 3, fprime, h, true, test);
        setEdge("h->i", g, 2, h, i, true, test);
        setEdge("h->j", g, 2, h, j, false, test);
        setEdge("j->k", g, TEN + 1, j, k, false, test);
        setEdge("i->k", g, SEVEN, i, k, true, test);
        setEdge("k-l", g, 3, k, l, true, test);
        setEdge("fprime - l", g, TEN + 6, fprime, l, false, test);
        setEdge("l - goal", g, 1, l, goal, true, test);
        assertEquals("the path should be this", _output,
            Graphs.shortestPath(g, a, goal, Graphs.ZERO_DISTANCER, test, test));
    }
    /** the number seven.*/
    private static final int SEVEN = 7;
    /** the number ten.*/
    private static final int TEN = 10;
    /** the arraylist of edges for shortest path in the Crazy test.*/
    private ArrayList<Graph<String, String>.Edge> _output =
            new ArrayList<Graph<String, String>.Edge>();

    /** sets the Edge using:
     * @param NAME the name of the edge
     * @param G the graph
     * @param WEIGHT the weight of the edge
     * @param TO the to vertex
     * @param FROM the from vertex
     * @param ISSHORTEST if this is a shortest path edge
     * @param TEST the weighter
     */
    private void setEdge(String name, Graph<String, String> g,
                        double weight, Graph<String, String>.Vertex to,
                        Graph<String, String>.Vertex from,
                        boolean isShortest,
                        TestWeight test) {
        Graph<String, String>.Edge e = g.add(to, from, name);
        test.setWeight(name, weight);
        if (isShortest) {
            _output.add(e);
        }
    }
}
