package make;
import java.util.ArrayList;
/** checks for cycle.
 * @author Ian Fox */
public class Cycle extends graph.Traversal<String, String> {
    /** if a change of date has occured.*/
    private boolean _time = false;
    /** the graph that this BUILD traversal acts on.*/
    private Main.DAG _graph;
    /**vertices that have been traversed.*/
    private ArrayList<Main.DAG.Edge> _traversed =
        new ArrayList<Main.DAG.Edge>();
    /** an arraylist of visited vertices.*/
    private ArrayList<Main.DAG.Vertex> _visited =
        new ArrayList<Main.DAG.Vertex>();
    /** the arraylist of postvisited vertices.*/
    private ArrayList<Main.DAG.Vertex> _posVisited =
        new ArrayList<Main.DAG.Vertex>();
    /** the constructor of a build a traversal using GRAPH.*/
    public Cycle(Main.DAG graph) {
        _graph = graph;
    }
    /** determines if this traversal should print in its postvisit.*/
    private boolean _print = true;
    /** a boolean that determines if the postvisit prints.
     * this is according to B */
    public void print(boolean b) {
        _print = b;
    }
    /** @return boolean to indicate if we are printing.*/
    public boolean getPrint() {
        return _print;
    }
    /** uses V and G.*/
    public void topo(Main.DAG G, Main.DAG.Vertex v) {
        try {
            if (!_visited.contains(v)) {
                visit(v);
                for (Main.DAG.Edge nex : G.edges(v)) {
                    if (!_traversed.contains(nex)) {
                        try {
                            preVisit(nex, v);
                            topo(G, nex.getV(v));
                        } catch (graph.RejectException e) {
                            continue;
                        }
                    }
                }
                postVisit(v);
            }
        } catch (graph.RejectException e) {
            return;
        }
    }
    @Override
    public void postVisit(Main.DAG.Vertex v) {
        if (!_posVisited.contains(v) && _print) {
            _posVisited.add(v);
        }

    }
    @Override
    public void visit(Main.DAG.Vertex v) {
        if (!_visited.contains(v)) {
            _visited.add(v);
        } else {
            throw new graph.RejectException();
        }
    }
    @Override
    public void preVisit(Main.DAG.Edge e, Main.DAG.Vertex v0) {
        if (_visited.contains(e.getV(v0))
            && !_posVisited.contains(e.getV(v0))) {
            System.out.println("hre");
            Main.usage();
        }
    }
}
