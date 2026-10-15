package lunaglaxe7.claraww;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean crossMod = true;
    public static boolean gc = true;
    public static int wandFlightUpdateFrequency = 1;

    public static void configure(File targ) {
        Configuration conf = new Configuration(targ);
        conf.load();
        crossMod = conf.get("compatibility", "Cross-Mod Interaction", crossMod, "Disable to keep mods segregated.")
                .getBoolean(true);
        gc = conf.get("compatibility", "Galacticraft Interaction", gc).getBoolean(true);
        wandFlightUpdateFrequency = conf.get("config", "frequency", 1, "Frequency that entity flying wand is updated.")
                .getInt(1);
        conf.save();
    }
}
