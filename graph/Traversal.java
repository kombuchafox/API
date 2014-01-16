package graph;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.ArrayList;



/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular set of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.  The client can dictate an ordering on
 *  the fringe, determining which item is next removed, by which kind
 *  of traversal is requested.
 *     + A depth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at one end.  It also revisits the node
 *       itself after traversing all successors by calling the
 *       postVisit method on it.
 *     + A breadth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at different ends.  It also revisits the node
 *       itself after traversing all successors as for depth-first
 *       traversals.
 *     + A general traversal treats the fringe as an ordered set, as
 *       determined by a Comparator argument.  There is no postVisit
 *       for this type of traversal.
 *  As vertices are added to the fringe, the traversal calls a
 *  preVisit method on the vertex.
 *
 *  Generally, the client will extend Traversal, overriding the visit,
 *  preVisit, and postVisit methods, as desired (by default, they do nothing).
 *  Any of these methods may throw StopException to halt the traversal
 *  (temporarily, if desired).  The preVisit method may throw a
 *  RejectException to prevent a vertex from being added to the
 *  fringe, and the visit method may throw a RejectException to
 *  prevent its successors from being added to the fringe.
 *  @author Ian Fox
 */

public class Traversal<VLabel, ELabel> {

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited.  The effect of specifying an
     *  ORDER whose results change as a result of modifications made during the
     *  traversal is undefined. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        _order = order;
        if (_order == null) {
            _fringePQ = new PriorityQueue<Graph<VLabel, ELabel>.
                Vertex>(G.edgeSize());
        } else {
            _fringePQ = new PriorityQueue<Graph<VLabel, ELabel>.
                    Vertex>(G.edgeSize(), naturalOrder());
        }
        _fringePQ.add(v);
        Graph<VLabel, ELabel>.Edge edgefinal = null;
        Graph<VLabel, ELabel>.Vertex vertexfinal = null;
        try {
            while (!_fringePQ.isEmpty()) {
                Graph<VLabel, ELabel>.Vertex v0 = _fringePQ.poll();
                try {
                    if (!_visit.contains(v0)) {
                        _visit.add(v0);
                        vertexfinal = v0;
                        visit(v0);
                        for (Graph<VLabel, ELabel>.Edge e : G.outEdges(v0)) {
                            edgefinal = e;
                            vertexfinal = e.getV(v0);
                            try {
                                preVisit(e, v0);
                                _fringePQ.add(e.getV(v0));
                            } catch (RejectException x) {
                                continue;
                            }
                        }
                    }
                } catch (RejectException x) {
                    continue;
                }
            }
        } catch (StopException e) {
            _finalEdge = edgefinal;
            _finalVertex = vertexfinal;
            _graph = G;
        }
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        try {
            _previousTraversal = "DFS";
            dfs(G, v);
            _visitDF = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
            _posVisitDF = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        } catch (StopException e) {
            _graph = G;
        }
    }
    /** the helper function which executes the actual DFS.
     * This is called from depthFirstTraversal.
     * @param G
     * @param V
     */
    public void dfs(Graph<VLabel, ELabel> G, Graph<VLabel, ELabel>.Vertex v) {
        try {
            visit(v);
            _visitDF.add(v);
            _finalVertex = v;
            for (Graph<VLabel, ELabel>.Edge nex : G.edges(v)) {
                if (!_visitDF.contains(nex.getV(v))) {
                    try {
                        preVisit(nex, v);
                        dfs(G, nex.getV(v));
                    } catch (RejectException e) {
                        continue;
                    }
                }
            }
            _posVisitDF.add(v);
            _finalVertex = v;
            postVisit(v);
        } catch (RejectException e) {
            return;
        }
    }
    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
        _previousTraversal = "BFS";
        _fringeBFS = new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        Graph<VLabel, ELabel>.Edge edgefinal = null;
        Graph<VLabel, ELabel>.Vertex vertexfinal = null;
        try {
            _fringeBFS.add(v);
            while (!_fringeBFS.isEmpty()) {
                Graph<VLabel, ELabel>.Vertex v0 = _fringeBFS.pop();
                try {
                    if  (!_visitBF.contains(v0)) {
                        vertexfinal = v0;
                        visit(v0);
                        _visitBF.add(v0);
                        for (Graph<VLabel, ELabel>.Edge successor
                            : G.outEdges(v0)) {
                            edgefinal = successor;
                            vertexfinal = successor.getV(v0);
                            try {
                                if (!_visitBF.contains(successor.getV(v0))) {
                                    preVisit(successor, v0);
                                    _fringeBFS.add(successor.getV(v0));
                                }
                            } catch (RejectException e) {
                                continue;
                            }
                        }
                        _fringeBFS.add(v0);
                    } else if (!_posVisitBF.contains(v0)
                        && _visitBF.contains(v0)) {
                        vertexfinal = v0;
                        postVisit(v0);
                        _posVisitBF.add(v0);
                    }
                } catch (RejectException x) {
                    continue;
                }
            }
            _visitBF = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
            _posVisitBF = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        } catch (StopException e) {
            _finalEdge = edgefinal;
            _finalVertex = vertexfinal;
            _graph = G;
        }
    }

    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        switch (_previousTraversal) {
        case("BFS"):
            breadthFirstTraverse(theGraph(), v);
            break;
        case("DFS"):
            depthFirstTraverse(theGraph(), v);
            break;
        default:
            traverse(theGraph(), v, _order);
        }
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit, visit, or postVisit that caused a Visit routine to
     *  return false.  Otherwise, returns null. */
    public Graph<VLabel, ELabel>.Vertex finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit that caused a Visit routine to return false. If it was not
     *  an edge that caused termination, returns null. */
    public Graph<VLabel, ELabel>.Edge finalEdge() {
        return _finalEdge;
    }

    /** Returns the last graph argument to a traverse routine, or null if none
     *  of these methods have been called. */
    protected Graph<VLabel, ELabel> theGraph() {
        return _graph;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {

    }
    /** @return COMPARATOR<GRAPH<VLABEL, ELABEL>.VERTEX> the naturalorder.*/
    protected Comparator<Graph<VLabel, ELabel>.Vertex> naturalOrder() {
        return new Comparator<Graph<VLabel, ELabel>.Vertex>() {
            @Override
            public int compare(Graph<VLabel, ELabel>.Vertex v0,
                    Graph<VLabel, ELabel>.Vertex v1) {
                return _order.compare(v0.getLabel(), v1.getLabel());
            }
        };
    }

    /** The Vertex (if any) that terminated the last traversal. */
    private Graph<VLabel, ELabel>.Vertex _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    private Graph<VLabel, ELabel>.Edge _finalEdge;
    /** The last graph traversed. */
    private Graph<VLabel, ELabel> _graph = null;
    /** the fringe for a generic traversal.*/
    private PriorityQueue<Graph<VLabel, ELabel>.Vertex> _fringePQ;
    /** the fringe for a depth first traversal.*/
    private Stack<Graph<VLabel, ELabel>.Vertex> _fringeDFS;
    /** the fringe for Breadth first traversal.*/
    private LinkedList<Graph<VLabel, ELabel>.Vertex> _fringeBFS;
    /** the previous traversal.*/
    protected String _previousTraversal = "";
    /** the last used comparator.*/
    protected Comparator<VLabel> _order;
    /** the list of visited components.*/
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _visitBF =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** the list of posvisted components.*/
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _posVisitBF =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** the list of visited components for general traversal.*/
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _visit =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** the list of posvisted components.*/
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _posVisit =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** the list of visited components.*/
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _visitDF =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** the list of posvisted components.*/
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _posVisitDF =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();

}
