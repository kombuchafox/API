package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import java.util.ArrayList;

import org.junit.Test;



import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the make package. */
public class Testing {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.Testing.class));
    }

    @Test
    public void parse() {
        ArrayList<Rule> text = Main.recordMakefile("make/basic.txt");
        assertEquals("text", true, !text.equals(null));
    }

    @Test
    public void buildBasic() {

        ArrayList<Rule> text = Main.recordMakefile("make/basic.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        graph.Graph<String, String>.Vertex start = null;
        for (graph.Graph<String, String>.Vertex v : g.vertices()) {
            if (v.getLabel().equals("computer")) {
                start = v;
                break;
            }
        }
        Main.Build t = Main.build(g, true);
        t.depthFirstTraverse(g, start);
        assertEquals("completes traverse w/o error", false, Main.getError());
    }

    @Test
    public void identicalPrereq() {
        ArrayList<Rule> text = Main.recordMakefile("make/identical"
                + "PreReqs.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        graph.Graph<String, String>.Vertex start = null;
        for (graph.Graph<String, String>.Vertex v : g.vertices()) {
            if (v.getLabel().equals("sandwhich")) {
                start = v;
                break;
            }
        }
        Main.Build t = Main.build(g, true);
        t.depthFirstTraverse(g, start);
        assertEquals("completes traverse w/o error", false, Main.getError());
    }

    @Test
    public void cyclic() {
        ArrayList<Rule> text = Main.recordMakefile("make/cyclic.txt");
        Main.testing(true);
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        assertEquals("finds cycles", false, Main.getError());
    }
    @Test
    public void noTarget() {

        Main.testing(true);
        ArrayList<Rule> text = Main.recordMakefile("make/noTarget.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        assertEquals("discovers lack of target", true, Main.getError());
    }

    @Test
    public void unusedRule() {
        ArrayList<Rule> text = Main.recordMakefile("make/unusedRule.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        graph.Graph<String, String>.Vertex start = null;
        for (graph.Graph<String, String>.Vertex v : g.vertices()) {
            if (v.getLabel().equals("cycle")) {
                start = v;
                break;
            }
        }
        Main.Build t = Main.build(g, true);
        t.depthFirstTraverse(g, start);
        assertEquals("completes traverse w/o error", false, Main.getError());
    }
    @Test
    public void cycleUnusedTarg() {
        Main.testing(true);
        ArrayList<Rule> text = Main.recordMakefile("make/cycleInUnused"
                + "Targs.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        assertEquals("finds cycles", false, Main.getError());
    }

    @Test
    public void emptyCommands() {
        ArrayList<Rule> text = Main.recordMakefile("make/emptyCommands.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        graph.Graph<String, String>.Vertex start = null;
        for (graph.Graph<String, String>.Vertex v : g.vertices()) {
            if (v.getLabel().equals("cycle")) {
                start = v;
                break;
            }
        }
        Main.Build t = Main.build(g, true);
        t.depthFirstTraverse(g, start);
        assertEquals("completes traverse w/o error", false, Main.getError());
    }
    @Test
    public void fileInfoTest() {
        Main.testing(true);
        Main.setTimeMap(Main.recordDate("make/BasicInfo.txt"));
        ArrayList<Rule> text = Main.recordMakefile("make/basic.txt");
        Main.DAG g = Main.makeGraph(text, new ArrayList<String>());
        graph.Graph<String, String>.Vertex start = null;
        for (graph.Graph<String, String>.Vertex v : g.vertices()) {
            if (v.getLabel().equals("computer")) {
                start = v;
                break;
            }
        }
        Main.Build t = Main.build(g, true);
        t.depthFirstTraverse(g, start);
        assertEquals("completes traverse w/o error", false, Main.getError());
    }
}
