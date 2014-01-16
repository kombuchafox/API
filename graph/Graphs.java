package graph;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import java.util.List;
import java.util.PriorityQueue;

/** Assorted graph algorithms.
 *  @author Ian Fox
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G, Graph<VLabel, ELabel>.Vertex V0,
                 final Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h,
                 final Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        ArrayList<Graph<VLabel, ELabel>.Vertex> closedList =
                new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        final HashMap<Graph<VLabel, ELabel>.Vertex, Double> fScores =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> gScores =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Edge>
            cameFrom =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel,
                ELabel>.Edge>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> openList =
            new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(G.vertexSize(),
                    new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                    @Override
                    public int compare(Graph<VLabel, ELabel>.Vertex v0,
                        Graph<VLabel, ELabel>.Vertex v1) {
                        return fScores.get(v0).compareTo(fScores.get(v1));
                    }
                });
        openList.add(V0);
        gScores.put(V0, 0.0);
        fScores.put(V0, gScores.get(V0) + h.dist(V0.getLabel(),
                V1.getLabel()));
        while (!openList.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex v = openList.peek();
            if (v == V1) { return reconsPath(v, cameFrom); }
            v = openList.poll();
            closedList.add(v);
            for (Graph<VLabel, ELabel>.Edge e : G.edges(v)) {
                Graph<VLabel, ELabel>.Vertex neigh = e.getV(v);
                double tentGscore = gScores.get(v)
                        + eweighter.weight(e.getLabel());
                double tentFscore = tentGscore + h.dist(neigh.getLabel(),
                        V1.getLabel());
                if (closedList.contains(neigh) && tentFscore
                        >= fScores.get(neigh)) {
                    continue;
                }
                if (!openList.contains(neigh) || tentFscore
                        < fScores.get(neigh)) {
                    cameFrom.put(neigh, e);
                    gScores.put(neigh, tentGscore);
                    fScores.put(neigh, tentFscore);
                    if (!openList.contains(neigh)) {
                        openList.add(neigh);
                    }
                }

            }
        }
        return null;
    }


    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G, Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V, Distancer<? super VLabel> h) {
        ArrayList<Graph<VLabel, ELabel>.Vertex> closedList =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        final HashMap<Graph<VLabel, ELabel>.Vertex, Double> fScores =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> gScores =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Edge>
            cameFrom =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel,
                ELabel>.Edge>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> openList =
            new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(G.vertexSize(),
                    new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                    @Override
                    public int compare(Graph<VLabel, ELabel>.Vertex v0,
                        Graph<VLabel, ELabel>.Vertex v1) {
                        return fScores.get(v0).compareTo(fScores.get(v1));
                    }
                });
        openList.add(V0);
        gScores.put(V0, 0.0);
        fScores.put(V0, gScores.get(V0) + h.dist(V0.getLabel(),
                V.getLabel()));
        while (!openList.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex v = openList.peek();
            if (v == V) { return reconsPath(v, cameFrom); }
            v = openList.poll();
            closedList.add(v);
            for (Graph<VLabel, ELabel>.Edge e : G.edges(v)) {
                Graph<VLabel, ELabel>.Vertex neigh = e.getV(v);
                double tentGscore = gScores.get(v) + e.getLabel().weight();
                double tentFscore = tentGscore + h.dist(neigh.getLabel(),
                        V.getLabel());
                if (closedList.contains(neigh) && tentFscore
                        >= fScores.get(neigh)) {
                    continue;
                }
                if (!openList.contains(neigh) || tentFscore
                        < fScores.get(neigh)) {
                    cameFrom.put(neigh, e);
                    gScores.put(neigh, tentGscore);
                    fScores.put(neigh, tentFscore);
                    if (!openList.contains(neigh)) {
                        openList.add(neigh);
                    }
                }

            }
        }
        return null;
    }
    /**@return ARRAYLIST<GRAPH<VLABEL, ELABEL>.EDGE> using the finael V and a.
     * hashmap of vertices to edges to CAMEFROM.*/
    static <VLabel, ELabel> ArrayList<Graph<VLabel, ELabel>.Edge> reconsPath(
        Graph<VLabel, ELabel>.Vertex v, HashMap<Graph<VLabel, ELabel>.Vertex,
        Graph<VLabel, ELabel>.Edge> cameFrom) {
        ArrayList<Graph<VLabel, ELabel>.Edge> path = new ArrayList<Graph<VLabel,
            ELabel>.Edge>();
        while (cameFrom.containsKey(v)) {
            Graph<VLabel, ELabel>.Edge e = cameFrom.get(v);
            path.add(0, e);
            v = e.getV(v);
        }
        return path;
    }




    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };
}
