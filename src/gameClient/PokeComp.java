package gameClient;

import java.util.Comparator;

/**
 * This class is an implementation of Comparator for CL_Pokemon Object type.
 */
public class PokeComp implements Comparator<CL_Pokemon> {
    /**
     * This method overrides Comparator method - compare.
     * as the desired way to compare is high to low, it uses the Double compare method, but in the opposite way.
     */
    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {
        return Double.compare(o2.getValue(),o1.getValue());
    }
}
