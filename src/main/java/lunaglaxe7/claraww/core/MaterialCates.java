package lunaglaxe7.claraww.core;

import java.util.LinkedHashSet;
import java.util.Set;

public class MaterialCates {

    private static final Set<String> naturalCap = new LinkedHashSet<>();
    private static final Set<String> magicalCap = new LinkedHashSet<>();

    public static void registerNaturalCapMaterial(String material) {
        if (material != null) {
            naturalCap.add(material);
        }
    }

    public static void registerMagicalCapMaterial(String material) {
        if (material != null) {
            magicalCap.add(material);
        }
    }

    public static boolean isCapMaterial(String material) {
        return naturalCap.contains(material) || magicalCap.contains(material);
    }

    public static boolean isNaturalCapMaterial(String material) {
        return naturalCap.contains(material);
    }

    public static boolean isMagicalCapMaterial(String material) {
        return magicalCap.contains(material);
    }
}
