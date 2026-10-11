package lunaglaxe7.claraww.adapter;

import cpw.mods.fml.common.Optional;
import lunaglaxe7.claraww.common.ModContents;
import lunaglaxe7.claraww.core.node.NodeAspect;
import thaumcraft.api.aspects.Aspect;

public class AspectAdapter {

    public static boolean tc = false;

    public static void init() {
        fromTC4();
    }

    @Optional.Method(modid = ModContents.TCID)
    private static void fromTC4() {
        for (String t : Aspect.aspects.keySet()) {
            new NodeAspect(t);
        }
        tc = true;
    }
}
