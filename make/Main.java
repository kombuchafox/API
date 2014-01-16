package make;
import graph.RejectException;
import graph.Traversal;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** Initial class for the 'make' program.
 *  @author Ian Fox
 */
public final class Main {
    /** matches a header of target to the style of target: P1 P2 ... */
    private static final Pattern HEADER =
        Pattern.compile("(^[^:=\\#]+):(\\s+(.*)\\s*)*");
    /** matches the line to a comment which we should just skip.*/
    private static final Pattern SKIPLINE = Pattern.compile("^#.*|\\s*");
    /** matches the line to a command that begins with a tabe or white space.*/
    private static final Pattern COMMAND = Pattern.compile("^(\\s+|\\t+)(.*)");

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }
        make(makefileName, fileInfoName, targets);
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        _timeMap = recordDate(fileInfoName);
        _rules = recordMakefile(makefileName);
        DAG graph = makeGraph(_rules, targets);
        Build traversal = build(graph, false);

        if (_initialVertex == null) {
            for (DAG.Vertex v : _targets) {
                traversal.topo(graph, v);
            }
        } else {
            traversal.topo(graph, _initialVertex);
        }
    }


    /**@return ARRAYLIST<RULE> which dictates all of the info to be built.
     * Specifically this takes in MAKFILEN, parses through the information
     * and creates rule object in which it appends to the arrayList.
     */
    static ArrayList<Rule> recordMakefile(String makfileN) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(makfileN));
            ArrayList<Rule> rules = new ArrayList<Rule>();
            ArrayList<String> targets = new ArrayList<String>();
            String line = file.readLine();
            Rule currenRule = null;
            while (line != null) {
                Matcher headerFind = HEADER.matcher(line);
                Matcher skip = SKIPLINE.matcher(line);
                Matcher addComm = COMMAND.matcher(line);
                if (headerFind.matches()) {
                    String targ = headerFind.group(1);
                    String rest = line.substring(targ.length() + 1).trim();
                    line = file.readLine();
                    if (targets.contains(targ)) {
                        Rule test = null;
                        for (Rule r : rules) {
                            if (r.getName().equals(targ)) { test = r; }
                        }
                        Matcher badCom = (line != null) ? COMMAND.matcher(line)
                            : COMMAND.matcher("");
                        if (test.getComm().size() > 0
                            && badCom.matches()) {
                            usage();
                        }
                    }
                    targets.add(targ);
                    currenRule = new Rule();
                    rules.add(currenRule);
                    currenRule.setTarget(targ);
                    String[] prereq = (rest.length() > 0) ? rest.split("\\s+")
                            : new String[0];
                    for (int i = 0; i < prereq.length; i++) {
                        if (prereq[i].matches("[#:=\\\\]")) { usage(); }
                        currenRule.addPreReq(prereq[i].trim());
                    }
                } else if (skip.matches()) {
                    line = file.readLine();
                } else if (addComm.matches() && currenRule != null) {
                    currenRule.addComm(line);
                    line = file.readLine();
                } else {
                    usage();
                }
            }
            return rules;
        } catch (IOException e) {

            usage();
            return null;
        }
    }

    /** Parse of @param fileInofoName , a mapping from the Name to ChangeDate.
     * @return HASHMAP<STRING, INTEGER> of the targ to date.*/
    static HashMap<String, Integer> recordDate(String fileInofoName) {
        HashMap<String, Integer> map = null;
        try {
            BufferedReader file = new BufferedReader(
                new FileReader(fileInofoName));
            map = new HashMap<String, Integer>();
            _currentTime = Integer.parseInt(file.readLine());
            String line = file.readLine();
            while (line != null) {
                String[] lineSplit = line.split("\\s+");
                if (lineSplit.length != 2) {
                    System.out.println(1);
                    System.out.println(1);
                    usage();
                }
                if (!lineSplit[0].matches("[#:=\\\\]")
                        && lineSplit[1].matches("\\d+")) {
                    if (Integer.parseInt(lineSplit[1]) > _currentTime) {
                        usage();
                    }
                    map.put(lineSplit[0], Integer.parseInt(lineSplit[1]));
                } else {
                    usage();
                }
                line = file.readLine();
            }
            file.close();
            return map;
        } catch (IOException e) {
            usage();
        }
        return map;
    }

    /** make @return DAG by taking the arraylist of.
     * RULES
     * TARGETS */
    public static DAG makeGraph(ArrayList<Rule> rules, List<String> targets) {
        if (targets.size() == 0) {
            Rule first = rules.get(0);
            return makeGraph(rules, first.getPreReqs());
        } else {
            return new DAG(rules, targets);
        }
    }
    /** constructs and @return BUILD and takes the GRAPH.
     * and boolean to tell if we are TESTING
     **/
    public static Build build(DAG graph, boolean testing) {
        _testing = testing;
        return new Build(graph);
    }
    /** DAG, A subclass of directed graphs. this class creates a DAG.
     * if a dag cannot be create then if throws an error in its make
     * DAG function.
     */
    static class DAG extends graph.DirectedGraph<String, String> {
        /** this maps a specific vertex with the commands 2 be build.*/
        private HashMap<Vertex, ArrayList<String>> _thingstoBuild = new
            HashMap<Vertex, ArrayList<String>>();
        /** the string of the order at which the targets are.*/
        private List<String> _order;
        /** @return ARRAYLIST<STRING> of commands from V.*/
        public  ArrayList<String> getComm(Vertex v) {
            return _thingstoBuild.get(v);
        }
        /** creates a new DAG graph with RULES and TARGETS.*/
        public DAG(ArrayList<Rule> rules, List<String> targets) {
            if (rules.get(0).getPreReqs().equals(targets)) {
                _order = new ArrayList<String>();
                makeGraph(rules);
            } else {
                _order = targets;
                makeGraph(rules);
            }
        }
        /** makes the graph for dage from RULES.*/
        private void makeGraph(ArrayList<Rule> rules) {
            HashMap<String, Vertex> createTargets = new
                HashMap<String, Vertex>();
            _vertTime = new HashMap<Vertex, Integer>();
            for (String target : _timeMap.keySet()) {
                Vertex v = this.add(target);
                createTargets.put(target, v);
                _vertTime.put(v, _timeMap.get(target));
                _thingstoBuild.put(v, new ArrayList<String>());
            }
            for (Rule r : rules) {
                Vertex v;
                if (_timeMap.containsKey(r.getName())) {
                    v = createTargets.get(r.getName());
                } else  {
                    v = this.add(r.getName());
                    createTargets.put(r.getName(), v);
                }
                _thingstoBuild.put(v, r.getComm());
            }
            if (_order.size() == 0) {
                _initialVertex = createTargets.get(rules.get(0).getName());
            } else {
                for (String targ : _order) {
                    if (createTargets.containsKey(targ)) {
                        _targets.add(createTargets.get(targ));
                    }
                }
            }
            for (Rule r : rules) {
                Vertex from = createTargets.get(r.getName());
                ArrayList<String> reqs = r.getPreReqs();
                for (String req : reqs) {
                    if (createTargets.containsKey(req)) {
                        Vertex to  = createTargets.get(req);
                        this.add(from, to, "");
                    } else {
                        usage();
                    }
                }
            }
            checkForCycles();
        }
        /** checks for cycles with the DAG.*/
        private void checkForCycles() {
            for (Vertex v : this.vertices()) {
                Cycle temp = new Cycle(this);
                temp.topo(this, v);
            }
        }
    }
    /** makeTopological is a DFS traversal. It calls build.
      * in its posvisit of its children.
      */
    static class Build extends Traversal<String, String> {
        /** if a change of date has occured.*/
        private boolean _time = false;
        /** the graph that this BUILD traversal acts on.*/
        private DAG _graph;
        /** an arraylist of visited vertices.*/
        private ArrayList<DAG.Vertex> _visited =
            new ArrayList<DAG.Vertex>();
        /** the arraylist of postvisited vertices.*/
        private ArrayList<DAG.Vertex> _posVisited =
            new ArrayList<DAG.Vertex>();
        /**vertices that have been traversed.*/
        private ArrayList<Main.DAG.Edge> _traversed =
            new ArrayList<Main.DAG.Edge>();
        /** the constructor of a build a traversal using GRAPH.*/
        public Build(DAG graph) {
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

        /** performs a topological sort of G and V.*/
        public void topo(DAG G, DAG.Vertex v) {
            try {
                visit(v);
                for (DAG.Edge nex : G.edges(v)) {
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

            } catch (graph.RejectException e) {
                return;
            }
        }
        @Override
        public void postVisit(DAG.Vertex v) {
            if (!_posVisited.contains(v) && _print) {
                _posVisited.add(v);
                for (String comm : _graph.getComm(v)) {
                    System.out.println(comm);
                }
            }

        }
        @Override
        public void visit(DAG.Vertex v) {
            if (_vertTime.containsKey(v) && !_visited.contains(v)) {
                Build subTraverse = new Build(_graph) {
                    @Override
                    public void preVisit(DAG.Edge e, DAG.Vertex v) {
                        if (_visited.contains(e.getV(v))
                                && !_posVisited.contains(e.getV(v))) {
                            usage();
                        }
                        if (_vertTime.containsKey(e.getV(v))
                                && (_vertTime.get(v)
                                < _vertTime.get(e.getV(v)))) {
                            if (_time) {
                                throw new RejectException();
                            }
                            _time = true;
                            _vertTime.put(v, _currentTime);
                        } else if (!_vertTime.containsKey(e.getV(v))) {
                            throw new RejectException();
                        }
                    }
                    @Override
                    public void visit(DAG.Vertex v) {
                        if (!_visited.contains(v)) {
                            _visited.add(v);
                        }
                    }
                    @Override
                    public void postVisit(DAG.Vertex v) {
                        if (_time && _print) {
                            _posVisited.add(v);
                            if (_graph.getComm(v).size() != 0) {

                                for (String comm : _graph.getComm(v)) {
                                    System.out.println(comm);
                                }
                            }
                        }

                    }
                };
                subTraverse.print(this.getPrint());
                subTraverse.topo(_graph, v);

            } else if (!_visited.contains(v)) {
                _visited.add(v);
            } else {
                throw new RejectException();
            }
        }

        @Override
        public void preVisit(DAG.Edge e, DAG.Vertex v0) {
        }
    }


    /** Print a brief usage message and exit program abnormally. */
    static void usage() {
        if (_testing) {
            _error = true;
        } else {
            System.err.println("ERROR");
            System.exit(1);
        }
    }
    /** @return BOOLEAN to indicate if there has been an error.
     * This is mainly for testing.*/
    static boolean getError() {
        boolean temp = _error;
        _error = false;
        return temp;
    }
    /**takes in the boolean TEST and changes the values of _testing.
     * this is used for junits to see if something is failing
     */
    public static void testing(boolean test) {
        _testing = test;
    }
    /** sets the timeMap to TIMEMAP. this is for testing.*/
    public static void setTimeMap(HashMap<String, Integer> timemap) {
        _timeMap = timemap;
    }
    /** a hashmap of all the objects that exist and a time they were built.*/
    private static HashMap<String, Integer> _timeMap =
        new HashMap<String, Integer>();
    /**a hashmap that it similar to _time map except that it used to access
     * the vertex in the file infor and the time it has been built.*/
    private static HashMap<DAG.Vertex, Integer> _vertTime;
    /** the integer last time at which the file was initialized.*/
    private static int _currentTime;
    /** an ArrayList of all the rules in the makefile.*/
    private static ArrayList<Rule> _rules;
    /**this indicates if we are currently testing.*/
    private static boolean _testing = false;
    /** this indicate if there has been an error.*/
    private static boolean _error = false;
    /** this is for the case if there is no initial target inputs.*/
    private static DAG.Vertex _initialVertex = null;
    /** this is an ArrayList of vertices which are the targets.*/
    private static ArrayList<DAG.Vertex> _targets = new ArrayList<DAG.Vertex>();
}
