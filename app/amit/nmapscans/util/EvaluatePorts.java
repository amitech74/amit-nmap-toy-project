package amit.nmapscans.util;

import amit.nmapscans.models.Port;
import com.google.common.collect.Sets;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Evaluates ports that are intersection, added or removed using the two collections passed in.
 */
public class EvaluatePorts {

  /**
   * Main method to evaluate common, added or removed ports
   *
   */
  public static EvaluatedPorts evaluate(Set<Port> newPorts, Set<Port> prevPorts){
    //-- Get intersection
    Set<Port> intersectionPorts = new LinkedHashSet<>();
    Sets.intersection(newPorts, prevPorts).copyInto(intersectionPorts);

    //-- Get Added
    Set<Port> addedPorts = new LinkedHashSet<>();
    Sets.difference(newPorts, prevPorts).copyInto(addedPorts);

    //-- Get Removed
    Set<Port> removedPorts = new LinkedHashSet<>();
    Sets.difference(prevPorts, newPorts).copyInto(removedPorts);

    return new EvaluatedPorts(intersectionPorts, addedPorts, removedPorts);
  }


  public static class EvaluatedPorts {
    private Set<Port> intersectionPorts;
    private Set<Port> addedPorts;
    private Set<Port> removedPorts;

    private EvaluatedPorts(Set<Port> intersectionPorts, Set<Port> addedPorts, Set<Port> removedPorts) {
      this.intersectionPorts = intersectionPorts;
      this.addedPorts = addedPorts;
      this.removedPorts = removedPorts;
    }

    public Set<Port> getIntersectionPorts() {
      return intersectionPorts;
    }

    public Set<Port> getAddedPorts() {
      return addedPorts;
    }

    public Set<Port> getRemovedPorts() {
      return removedPorts;
    }

  }
}
