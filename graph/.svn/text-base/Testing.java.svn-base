package graph;


import org.junit.Test;
import java.util.ArrayList;
import ucb.junit.textui;
import static org.junit.Assert.*;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package.
 *  @author Ian Fox
 */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        textui.runClasses(graph.GraphsTesting.class);
        System.exit(textui.runClasses(graph.Testing.class));
    }

    @Test
    public void emptyGraph() {
        DirectedGraph<Object, Object> g =
                new DirectedGraph<Object, Object>();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }
    @Test
    public void multipleGraphPointers() {
        DirectedGraph<NoLabel, NoLabel> g =
                new DirectedGraph<NoLabel, NoLabel>();
        g.add(new NoLabel());
        g.add(new NoLabel());
        DirectedGraph<NoLabel, NoLabel> g1 =
                new DirectedGraph<NoLabel, NoLabel>();
        assertEquals("Initial graph has vertices", 2, g.vertexSize());
        assertEquals("Initial graph has vertices", 0, g1.vertexSize());
    }
    @Test
    public void testofOutdegree() {
        DirectedGraph<NoLabel, NoLabel> g =
                new DirectedGraph<NoLabel, NoLabel>();
        Graph<NoLabel, NoLabel>.Vertex v = g.add(new NoLabel());
        Graph<NoLabel, NoLabel>.Vertex vFrom1 = g.add(new NoLabel());
        g.add(v, vFrom1 , new NoLabel());
        g.add(v, vFrom1, new NoLabel());
        Graph<NoLabel, NoLabel>.Vertex vFrom2 = g.add(new NoLabel());
        g.add(v, vFrom2);
        assertEquals("The out degree is 2", 3, g.outDegree(v));
        assertEquals("the in degree is 0 ", 0, g.inDegree(v));
        g.add(vFrom1, v);
        g.add(vFrom1, vFrom1);
        assertEquals("Outdegree is 2 because of self edge", 2,
                g.outDegree(vFrom1));
    }
    @Test
    public void testRemoveVertices() {
        DirectedGraph<NoLabel, NoLabel> g =
                new DirectedGraph<NoLabel, NoLabel>();
        Graph<NoLabel, NoLabel>.Vertex v = g.add(new NoLabel());
        Graph<NoLabel, NoLabel>.Vertex v1 = g.add(new NoLabel());
        Graph<NoLabel, NoLabel>.Vertex v2 = g.add(new NoLabel());
        g.remove(v);
        g.remove(v1);
        g.remove(v2);
        assertEquals("Graph has vertices", 0 , g.vertexSize());
    }
    @Test
    public void testBFS() {
        DirectedGraph<String, NoLabel> g =
                new DirectedGraph<String, NoLabel>();
        ArrayList<Graph<String, NoLabel>.Vertex> vertices =
            new ArrayList<Graph<String, NoLabel>.Vertex>();
        final ArrayList<Graph<String, NoLabel>.Vertex> vertices2 =
            new ArrayList<Graph<String, NoLabel>.Vertex>();
        Graph<String, NoLabel>.Vertex v3 = g.add("3");
        vertices.add(v3);
        Graph<String, NoLabel>.Vertex v0 = g.add("0");
        vertices.add(v0);
        Graph<String, NoLabel>.Vertex v2 = g.add("2");
        vertices.add(v2);
        Graph<String, NoLabel>.Vertex v1 = g.add("1");
        vertices.add(v1);
        g.add(v3, v0);
        g.add(v3, v2);
        g.add(v0, v2);
        g.add(v0, v1);
        g.add(v1, v2);
        Traversal<String, NoLabel> t =
                new Traversal<String, NoLabel>() {
                    @Override
                    public void visit(Graph<String, NoLabel>.Vertex v) {
                        vertices2.add(v);
                    }
                };
        t.breadthFirstTraverse(g, v3);
        assertEquals("both traversals match each other", vertices,
            vertices2);
    }

    @Test
    public void testBFStwo() {
        UndirectedGraph<String, NoLabel> g = new
                UndirectedGraph<String, NoLabel>();
        ArrayList<Graph<String, NoLabel>.Vertex> vertices =
            new ArrayList<Graph<String, NoLabel>.Vertex>();
        final ArrayList<Graph<String, NoLabel>.Vertex> vertices2 =
            new ArrayList<Graph<String, NoLabel>.Vertex>();
        Graph<String, NoLabel>.Vertex a = g.add("a");
        vertices.add(a);
        Graph<String, NoLabel>.Vertex b = g.add("b");
        vertices.add(b);
        Graph<String, NoLabel>.Vertex c = g.add("c");
        vertices.add(c);
        Graph<String, NoLabel>.Vertex d = g.add("d");
        vertices.add(d);
        Graph<String, NoLabel>.Vertex e = g.add("e");
        vertices.add(e);
        Graph<String, NoLabel>.Vertex f = g.add("f");
        vertices.add(f);
        g.add(a, b);
        g.add(a, c);
        g.add(c, e);
        g.add(a, d);
        g.add(d, e);
        g.add(d, f);
        Traversal<String, NoLabel> t =
                new Traversal<String, NoLabel>() {
                    @Override
                    public void visit(Graph<String, NoLabel>.Vertex v) {
                        vertices2.add(v);
                    }
                };
        t.breadthFirstTraverse(g, a);
        assertEquals("both traversals match each other", vertices, vertices2);
    }

    @Test
    public void testDFS() {
        UndirectedGraph<String, NoLabel> g =
                new UndirectedGraph<String, NoLabel>();
        ArrayList<Graph<String, NoLabel>.Vertex> vertices =
            new ArrayList<Graph<String, NoLabel>.Vertex>();
        final ArrayList<Graph<String, NoLabel>.Vertex> vertices2 =
            new ArrayList<Graph<String, NoLabel>.Vertex>();
        Graph<String, NoLabel>.Vertex a = g.add("a");
        vertices.add(a);
        Graph<String, NoLabel>.Vertex b = g.add("b");
        vertices.add(b);
        Graph<String, NoLabel>.Vertex c = g.add("c");
        vertices.add(c);
        Graph<String, NoLabel>.Vertex d = g.add("d");
        Graph<String, NoLabel>.Vertex e = g.add("e");
        vertices.add(e);
        vertices.add(d);
        Graph<String, NoLabel>.Vertex f = g.add("f");
        vertices.add(f);
        g.add(a, b);
        g.add(a, c);
        g.add(c, e);
        g.add(a, d);
        g.add(d, e);
        g.add(d, f);
        System.out.println("DFS");
        Traversal<String, NoLabel> t =
                new Traversal<String, NoLabel>() {
                    @Override
                    public void visit(Graph<String, NoLabel>.Vertex v) {
                        vertices2.add(v);
                    }
                };
        t.depthFirstTraverse(g, a);
        assertEquals("both traversals match each other", vertices, vertices2);
    }
}
