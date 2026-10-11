package lunaglaxe7.claraww.core.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lunaglaxe7.claraww.core.IItemAccessor;

public class CapManager {

    private static final List<NodeCap> caps = new ArrayList<>();
    private static final Map<String, Integer> idMap = new ConcurrentHashMap<>();
    private static final NodeCap NULL = build("null", 999, 999, false, IItemAccessor.NONE);

    public static void registerCap(NodeCap cap) {
        if (cap != null && !idMap.containsKey(cap.getTag())) {
            idMap.put(cap.getTag(), caps.size());
            caps.add(cap);
        }
    }

    public static int getCapCount() {
        return caps.size();
    }

    public static int getCraftCost(String tag) {
        return getByTag(tag).getCraftCost();
    }

    public static int getCraftCost(int id) {
        return getById(id).getCraftCost();
    }

    public static float getDiscount(String tag, String aspect) {
        return getByTag(tag).getDiscount(aspect);
    }

    public static float getDiscount(int id, String aspect) {
        return getById(id).getDiscount(aspect);
    }

    public static int getId(String tag) {
        return checkTag(tag) ? idMap.get(tag) : -1;
    }

    public static String getTagById(int id) {
        return checkId(id) ? caps.get(id).getTag() : "null";
    }

    public static NodeCap getByTag(String tag) {
        return checkTag(tag) ? caps.get(idMap.get(tag)) : NULL;
    }

    public static NodeCap getById(int id) {
        return checkId(id) ? caps.get(id) : NULL;
    }

    /**
     * @return check whether list contains this tag
     */
    public static boolean checkTag(String tag) {
        return tag != null && idMap.containsKey(tag);
    }

    public static boolean checkId(int id) {
        return id >= 0 && id < caps.size();
    }

    public static NodeCap build(String tag, int craftCost, float baseDiscount, boolean isMagical,
            IItemAccessor material) {
        Map<String, Float> discount = new ConcurrentHashMap<>();
        for (NodeAspect a : Aspect.getPrimalAspects()) {
            discount.put(a.getTag(), baseDiscount);
        }
        return build(tag, craftCost, baseDiscount, discount, isMagical, material);
    }

    public static NodeCap build(String tag, int craftCost, float baseDiscount, Map<String, Float> discount,
            boolean isMagical, IItemAccessor material) {
        return new NodeCap(tag, craftCost, baseDiscount, discount, isMagical, material);
    }

}
