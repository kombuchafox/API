package graph;

import java.util.ArrayList;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** A directed graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Ian Fox
 */
public class DirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public DirectedGraph() {
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int outDegree(Vertex v) {
        int total = 0;
        for (Edge e : getMap().get(v)) {
            if (e.getV0() == v) {
                total += 1;
            }
        }
        return total;
    }
    @Override
    public int inDegree(Vertex v) {
        int total = 0;
        for (Edge e : getMap().get(v)) {
            if (e.getV1() == v) {
                total += 1;
            }
        }
        return total;
    }

    @Override
    public Iteration<Edge> inEdges(Vertex v) {
        ArrayList<Edge> ed = new ArrayList<Edge>();
        for (Edge e : getMap().get(v)) {
            if (e.getV1() == v && !ed.contains(e)) {
                ed.add(e);
            }
        }
        return Iteration.iteration(ed.iterator());
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        ArrayList<Edge> ed = new ArrayList<Edge>();
        for (Edge e : getMap().get(v)) {
            if (e.getV0() == v && !ed.contains(e)) {
                ed.add(e);
            }
        }
        return Iteration.iteration(ed.iterator());
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Vertex> iter = new ArrayList<Vertex>();
        for (Edge e : getMap().get(v)) {
            if (e.getV0() == v && !iter.contains(e.getV1())) {
                iter.add(e.getV1());
            }
        }
        return Iteration.iteration(iter.iterator());
    }
    @Override
    public Iteration<Vertex> predecessors(Vertex v) {
        ArrayList<Vertex> iter = new ArrayList<Vertex>();
        for (Edge e : getMap().get(v)) {
            if (e.getV1() == v && !iter.contains(e.getV0())) {
                iter.add(e.getV0());
            }
        }
        return Iteration.iteration(iter.iterator());
    }
    @Override
    public boolean contains(Vertex u, Vertex v) {
        for (Edge e : getMap().get(u)) {
            for (Edge e2 : getMap().get(v)) {
                if (e == e2 && e.getV0() == u && e.getV1() == v) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(Vertex u, Vertex v, ELabel label) {
        for (Edge e : getMap().get(u)) {
            for (Edge e2 : getMap().get(v)) {
                if (e == e2 && e.getV0() == u && e.getV1() == v) {
                    if (e.getLabel() == label && e2.getLabel()
                        == label) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
