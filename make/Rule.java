package make;
import java.util.ArrayList;

/** Holds the components that make up a rule.
 * this include the prereqs and the commands.
 * @author Ian Fox*/
public class Rule {
    /** a rule which holds a target name, its prereqs, and its commands.*/
    public Rule() {
        _commands = new ArrayList<String>();
        _prereqs = new ArrayList<String>();
    }

    /** Takes in a PREREQ and adds to the List of prereqs. */
    void addPreReq(String prereq) {
        _prereqs.add(prereq);
    }

    /** Takes in a COMM and adds it to the List of commands.*/
    void addComm(String comm) {
        _commands.add(comm);
    }
    /** sets the target to TARG.*/
    void setTarget(String targ) {
        _target = targ;
    }
    /** @return STRING of the name of this target.*/
    String getName() {
        return _target;
    }
    /** @return ARRAYLIST<STRING> the commands of this.*/
    ArrayList<String> getComm() {
        return _commands;
    }
    /** @return ARRAYLIST<STRING> the prerequsites of this.*/
    ArrayList<String> getPreReqs() {
        return _prereqs;
    }

    /**an ArrayList of the commands for a certain rule.*/
    private ArrayList<String> _prereqs;
    /**an ArrayList of the commands for a certain rule.*/
    private ArrayList<String> _commands;
    /** a string labeled as the target.*/
    private String _target;
}
