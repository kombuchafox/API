package trip;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your trip package per se (that is, it must be
 * possible to remove them and still have your package work). */

import ucb.junit.textui;
import static org.junit.Assert.*;

import org.junit.Test;

/** Unit tests for the trip package. */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(trip.Testing.class));
    }

    @Test
    public void checkName() {
        Location here = new Location();
        here.setLocation("San_Francisco");
        here.setX(5.0);
        here.setY(5.5);
        assertEquals("the name is",
                "San_Francisco", here.getName());
    }
    @Test
    public void checkName2() {
        Location here = new Location();
        here.setLocation("P500_1");
        here.setX(5.0);
        here.setY(5.5);
        assertEquals("the name is",
                "P500_1", here.getName());
    }
    @Test
    public void checkRoad() {
        Location one = new Location();
        Location two = new Location();
        Road road = new Road();
        road.setL0(one);
        road.setL1(two);
        assertEquals("the first location is", two,
                road.getTo());
        assertEquals("the two location is", one,
                road.getFrom());
    }
    @Test
    public void checkRoad1() {
        Road road = new Road();
        road.setDirection("north");
        assertEquals("the direction", "north",
                road.getDirect());
    }
    @Test
    public void mapCreation() {
        Main.Map map = new Main.Map();
        Main.Map map2 = new Main.Map();
        map.add(new Location());
        map2.add(new Location());
        map.add(new Location());
        map2.add(new Location());
        assertEquals("the vertex size", 2, map.vertexSize());
    }


}
