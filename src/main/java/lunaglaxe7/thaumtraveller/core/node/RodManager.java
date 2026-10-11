package lunaglaxe7.thaumtraveller.core.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RodManager {

    private static final List<NodeRod> rods = new ArrayList<>();
    private static final Map<String, Integer> idMap = new ConcurrentHashMap<>();
    private static final NodeRod NULL = build("null", 999, 0, false);

    public static void registerRod(NodeRod rod) {
        if (rod != null && !idMap.containsKey(rod.getTag())) {
            idMap.put(rod.getTag(), rods.size());
            rods.add(rod);
        }
    }

    public static int getRodCount() {
        return rods.size();
    }

    public static NodeRod getByTag(String tag) {
        return checkTag(tag) ? rods.get(idMap.get(tag)) : NULL;
    }

    public static int getId(String tag) {
        return checkTag(tag) ? idMap.get(tag) : -1;
    }

    public static NodeRod getById(int id) {
        return checkId(id) ? rods.get(id) : NULL;
    }

    public static boolean checkId(int id) {
        return id >= 0 && id < rods.size();
    }

    public static boolean checkTag(String tag) {
        return tag != null && idMap.containsKey(tag);
    }

    public static NodeRod build(String tag, int craftCost, int capacity, boolean glow) {
        return new NodeRod(tag, capacity, craftCost, glow);
    }
}
