package lunaglaxe7.claraww.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lunaglaxe7.claraww.api.IHandler;
import lunaglaxe7.claraww.api.ModEvent;

public class ModEventManager {

    private final Map<Class<? extends ModEvent>, List<IHandler>> manager = new ConcurrentHashMap<>();
    public static ModEventManager instance = new ModEventManager();

    public static void init() {
        register(CapInfoEvent.CapDiscountMul.class, new UTypeApplier.Conducting());
        register(CapInfoEvent.CapCraftCost.class, new UTypeApplier.Affinity());
    }

    public static void register(Class<? extends ModEvent> c, IHandler handler) {
        instance.manager.computeIfAbsent(c, k -> new ArrayList<>()).add(handler);
    }

    public static List<IHandler> getHandlers(Class<? extends ModEvent> c) {
        return instance.manager.get(c);
    }

    public static float capCostModifiers(CapInfoEvent.CapCraftCost event) {
        return applyModifiers(event).getModifier();
    }

    public static float multiplyDiscountModifiers(CapInfoEvent.CapDiscountMul event) {
        return applyModifiers(event).getModifier();
    }

    public static float multiplySpecialDiscountModifiers(CapInfoEvent.SpecialDiscountMul event) {
        return applyModifiers(event).getModifier();
    }

    private static <T extends ModEvent> T applyModifiers(T event) {
        List<IHandler> handlers = getHandlers(event.getClass());
        if (handlers != null && !handlers.isEmpty()) {
            for (IHandler handler : handlers) {
                handler.handle(event);
            }
        }
        return event;
    }

}
