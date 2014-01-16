package trip;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import graph.Traversal;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
/** Initial class for the 'trip' program.
 *  @author Ian Fox
 */
public final class Main extends Traversal {
    /** this.*/
    private static final Pattern LOCATION_ROAD = Pattern.compile(
        "(\\s*L\\s+(\\w+[-\\w+]*)\\s+(-?\\d+.\\d+)\\s+(-?\\d+.\\d+))|"
        + "(\\s*R\\s+(\\w+[-\\w+]*)\\s+(\\w+[-\\w+]*)\\s+(\\d+.\\d+)\\s+"
        + "(NS|SN|EW|WE)\\s+(\\w+[-\\w+]*))");
    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;
        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                                  outFileName);
                System.exit(1);
            }
        }
        trip(mapFileName);
    }

    /**takes in MAP and parses the information. Checking.
     * using the rejects pattern which either maps to a location or a
     * a road. Only one location can exist. */
    static void parseMap(String map) {
        try {
            Reader reader = new FileReader(new File(map));
            Scanner inp = new Scanner(reader);
            ArrayList<String> locations = new ArrayList<String>();
            _locations = new HashMap<String, Location>();
            _roads = new ArrayList<Road>();
            while (inp.findWithinHorizon(LOCATION_ROAD, 0) != null) {
                MatchResult match = inp.match();
                if (match.group(1) != null) {
                    if (locations.contains(match.group(2))) {
                        System.out.println("here");
                        usage();
                    }
                    String name = match.group(2);
                    locations.add(name);
                    Location location = new Location();
                    location.setLocation(name);

                    location.setX(Double.parseDouble(match.group(3)));
                    location.setY(Double.parseDouble(match.group(4)));

                    _locations.put(name, location);
                } else if (match.group(5) != null) {
                    Road road = new Road();
                    Road road2 = new Road();
                    String from = match.group(6);

                    String to = match.group(10);

                    if (!_locations.containsKey(from)
                        || !_locations.containsKey(to)) {
                        System.out.println("here2");
                        usage();
                    }
                    Location fromObject = _locations.get(from);
                    Location toObject = _locations.get(to);
                    road.setL0(fromObject);
                    road.setL1(toObject);
                    road2.setL0(toObject);
                    road2.setL1(fromObject);
                    setDirection(road, road2, match.group(9));
                    road.setRoad(match.group(7));
                    road2.setRoad(match.group(7));
                    road.setWeight(Double.parseDouble(match.group(8)));
                    road2.setWeight(Double.parseDouble(match.group(8)));
                    _roads.add(road);
                    _roads.add(road2);
                } else {
                    System.out.println("here3");
                    usage();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }
    /** uses TO FROM D.*/
    private static void setDirection(Road to, Road from, String d) {
        if (d.equals("NS")) {
            to.setDirection("south");
            from.setDirection("north");
        } else if (d.equals("SN")) {
            to.setDirection("north");
            from.setDirection("south");
        } else if (d.equals("EW")) {
            to.setDirection("west");
            from.setDirection("east");
        } else if (d.equals("WE")) {
            to.setDirection("east");
            from.setDirection("west");
        }
    }
    /** the representation of the Map through a graph.*/
    static class Map extends graph.DirectedGraph<Location, Road> {
        /** creates a new Map object.*/
        public Map() {
            try {
                for (String local : _locations.keySet()) {
                    Location  here = _locations.get(local);
                    _vertices.put(here, this.add(here));
                }
                for (Road road : _roads) {
                    Location f = road.getFrom();
                    Location t = road.getTo();
                    this.add(_vertices.get(f), _vertices.get(t), road);
                }
            } catch (NullPointerException n) {
                return;
            }
        }
    }
    /** Print a trip for the request on the standard input to the stsndard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        parseMap(mapFileName);
        Map m = new Map();
        Scanner input = new Scanner(System.in);
        ArrayList<Location> navigation = new ArrayList<Location>();
        boolean isNext = false;
        while (input.hasNextLine()) {
            String[] line = input.nextLine().split("\\s+");
            for (int i = 0; i < line.length; i++) {
                if (line[i].charAt(line[i].length() - 1) == ',') {
                    line[i] = line[i].substring(0, line[i].length() - 1);
                    isNext = true;
                } else {
                    isNext = false;
                }
            }
            for (String s : line) {
                if (_locations.containsKey(s)) {
                    navigation.add(_locations.get(s));
                }
            }
        }
        if (navigation.size() < 2 || isNext) {
            usage();
        }
        ArrayList<String> directions = conDirections(m, navigation);
        for (String direct : directions) {
            System.out.println(direct);
        }
    }
    /** @return ARRAYLIST<STRING> of the path from M, using the list.
     * which is the NAVIGATION of locations you should use.*/
    private static ArrayList<String> conDirections(Map m,
        ArrayList<Location> navigation) {
        ArrayList<String> directions = new ArrayList<String>();
        directions.add("From " + navigation.get(0).getName() + ":");
        directions.add("");
        String current = "", currentStreet = "", direct = "";
        int count = 0;
        Location last = navigation.get(navigation.size() - 1);
        List<graph.Graph<Location, Road>.Edge> result = null;
        for (int ind = 1; ind < navigation.size(); ind += 1) {
            Map.Vertex from = _vertices.get(navigation.get(ind - 1));
            Map.Vertex to = _vertices.get(navigation.get(ind));
            result = graph.Graphs.shortestPath(m, from, to,
                graph.Graphs.ZERO_DISTANCER);
            for (int j = 0; j < result.size(); j++) {
                Road road = result.get(j).getLabel();
                String roadName = road.getName();
                if (roadName.equals(currentStreet)
                    && road.getDirect().equals(direct)) {
                    String[] direction = current.split("\\s+");
                    double length = Double.parseDouble(direction[5]);
                    length += road.weight();
                    direction[5] = Double.toString(length);
                    String newCurr = "";
                    for (int i = 0; i < direction.length; i++) {
                        if (i + 1 == direction.length) {
                            continue;
                        } else {
                            newCurr += direction[i] + " ";
                        }
                    }
                    current = newCurr;
                } else {
                    if (!current.equals("")) {
                        directions.add(roundOff(current));
                    }
                    count += 1;
                    current = Integer.toString(count) + ". Take ";
                    currentStreet = road.getName();
                    current += currentStreet + " ";
                    current += road.getDirect() + " for ";
                    direct = road.getDirect();
                    double dist = road.weight();
                    current += Double.toString(dist);
                }
                if ((ind != navigation.size() - 1)
                    && road.getTo().equals(to.getLabel())) {
                    current += " miles " + "to " + to.getLabel().getName()
                        + ".";
                    directions.add(roundOff(current));
                    current = currentStreet = "";
                } else if (!road.getTo().equals(last)) {
                    current += " miles.";
                }
            }
        }
        directions.add(roundOff((current
            + " miles to " + last.getName() + ".")));
        return directions;
    }
    /** @return STRING which is the values of S, with the double rounded.*/
    private static String roundOff(String s) {
        String[] sentence = s.split("\\s+");
        for (int i = 3; i < sentence.length; i++) {
            if (sentence[i].matches("\\d+.\\d+")) {
                double dist = Math.round(
                    Double.parseDouble(sentence[i]) * 10.0) / 10.0;
                sentence[i] = Double.toString(dist);
            }
        }
        String result = "";
        for (int i = 0; i < sentence.length; i++) {
            if (i + 1 == sentence.length) {
                result += sentence[i];
            } else {
                result += sentence[i] + " ";
            }
        }
        return result;
    }
    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.exit(1);
    }

    /** a hashmap of the location names to the actual location objext.*/
    private static HashMap<String, Location> _locations;
    /** an list of roads in this map.*/
    private static ArrayList<Road> _roads;
    /** a map of locations to vertexes.*/
    private static HashMap<Location, Map.Vertex> _vertices =
        new HashMap<Location, Map.Vertex>();


}
