package lunaglaxe7.claraww.core.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aspect {

    public final static Map<String, NodeAspect> aspects = new ConcurrentHashMap<>();
    private final static List<NodeAspect> primals = new ArrayList<>();

    public final static NodeAspect AIR = new NodeAspect("aer");
    public final static NodeAspect FIRE = new NodeAspect("ignis");
    public final static NodeAspect WATER = new NodeAspect("aqua");
    public final static NodeAspect EARTH = new NodeAspect("terra");
    public final static NodeAspect ORDER = new NodeAspect("ordo");
    public final static NodeAspect ENTROPY = new NodeAspect("perditio");

    static {
        init();
    }

    public static void init() {
        primals.add(AIR);
        primals.add(FIRE);
        primals.add(WATER);
        primals.add(EARTH);
        primals.add(ORDER);
        primals.add(ENTROPY);
    }

    public static List<NodeAspect> getPrimalAspects() {
        if (primals.isEmpty()) {
            init();
        }
        return primals;
    }
}
