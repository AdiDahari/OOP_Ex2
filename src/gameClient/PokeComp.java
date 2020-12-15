package gameClient;

import java.util.Comparator;

public class PokeComp implements Comparator<CL_Pokemon> {
    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {
        return Double.compare(o2.getValue(),o1.getValue());
    }
}
