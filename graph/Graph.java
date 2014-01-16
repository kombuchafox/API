package graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make changes that don't affect the API as seen
 * from outside the graph package:
 *   + You may make methods in Graph abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may private and package private members.
 *   + You may add additional non-public classes to the graph package.
 */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  Graphs may have self edges and may have multiple edges between vertices.
 *
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Ian Fox
 */


public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {
        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The label on this vertex. */
        private final VLabel _label;
    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return _vertexDegree.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        int total = 0;
        for (Vertex v : _vertexDegree.keySet()) {
            total += _vertexDegree.get(v).size();
        }
        return total / 2;
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        return _vertexDegree.get(v).size();
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        return outDegree(v);
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return this.outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        for (Edge e : _vertexDegree.get(u)) {
            if (e.getV(u) == v) {
                return true;
            }
        }
        return false;
    }


    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v, ELabel label) {
        for (Edge e : _vertexDegree.get(u)) {
            if (label == e.getLabel() && e.getV(u) == v) {
                return true;
            }
        }
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex vNew = new Vertex(label);
        _vertexDegree.put(vNew, new ArrayList<Edge>());
        return vNew;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from, Vertex to, ELabel label) {
        Edge eNew = new Edge(from, to, label);
        if (!(from == to)) {
            _vertexDegree.get(from).add(eNew);
            _vertexDegree.get(to).add(eNew);
        } else {
            _vertexDegree.get(from).add(eNew);
        }
        return eNew;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from, Vertex to) {
        return add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        ArrayList<Edge> edges = _vertexDegree.remove(v);
        for (Edge e : edges) {
            _vertexDegree.get(e.getV(v)).remove(e);
        }
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        for (Vertex v : _vertexDegree.keySet()) {
            if (_vertexDegree.get(v).contains(e)) {
                _vertexDegree.get(v).remove(e);
            }
        }
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        if (_vertexDegree.containsKey(v1) && _vertexDegree.containsKey(v2)) {
            for (int i = _vertexDegree.get(v1).size() - 1; i > -1; i++) {
                if (_vertexDegree.get(v1).get(i).getV(v1) == v2) {
                    _vertexDegree.get(v1).remove(_vertexDegree.get(v1).get(i));
                }
            }
        }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        ArrayList<Graph<VLabel, ELabel>.Vertex> vlist =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        for (Vertex v : _vertexDegree.keySet()) {
            vlist.add(v);
        }
        return Iteration.iteration(vlist.iterator());
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Vertex> iter = new ArrayList<Vertex>();
        for (Edge e : _vertexDegree.get(v)) {
            if (!iter.contains(e.getV(v))) {
                iter.add(e.getV(v));
            }
        }
        return Iteration.iteration(iter.iterator());
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        ArrayList<Vertex> iter = new ArrayList<Vertex>();
        for (Edge e : _vertexDegree.get(v)) {
            if (!iter.contains(e.getV(v))) {
                iter.add(e.getV(v));
            }
        }
        return Iteration.iteration(iter.iterator());
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        if (_comparator == null) {
            ArrayList<Edge> edges = new ArrayList<Edge>();
            for (Vertex v : _vertexDegree.keySet()) {
                for (Edge e : _vertexDegree.get(v)) {
                    if (!edges.contains(e)) {
                        edges.add(e);
                    }
                }
            }
            _edges = edges;
        }
        return Iteration.iteration(_edges.iterator());
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        return Iteration.iteration(_vertexDegree.get(v).iterator());
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        return outEdges(v);
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if intComp = Graph.<Integer>naturalOrder(), then
     *  intComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    /** Cause subsequent calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        constructComp(comparator);
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Vertex v : _vertexDegree.keySet()) {
            for (Edge e : _vertexDegree.get(v)) {
                if (!edges.contains(e)) {
                    edges.add(e);
                }
            }
        }
        _edges = edges;
        Collections.sort(_edges, _comparator);
    }

    /** creates a comparator using a VLabel COMPARATOR.
     */
    private void constructComp(Comparator<ELabel> comparator) {
        final Comparator<ELabel> c = comparator;
        _comparator = new Comparator<Graph<VLabel, ELabel>.Edge>() {
            @Override
            public int compare(Graph<VLabel, ELabel>.Edge e1,
                    Graph<VLabel, ELabel>.Edge e2) {
                return c.compare(e1.getLabel(), e2.getLabel());
            }
        };
    }

    /**@return HASHMAP<VERTEX, ARRAYLIST<EDGE>> of me.*/
    HashMap<Vertex, ArrayList<Edge>> getMap() {
        return _vertexDegree;
    }

    /** the arrayList of Edges. */
    private ArrayList<Edge> _edges;
    /** a HashMap of the indegees for each edges of me.*/
    private HashMap<Vertex, ArrayList<Edge>> _vertexDegree =
            new HashMap<Vertex, ArrayList<Edge>>();
    /** the comparator for ordering edges.*/
    private Comparator<Graph<VLabel, ELabel>.Edge> _comparator;

}
