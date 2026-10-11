package lunaglaxe7.claraww.items;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import lunaglaxe7.claraww.adapter.ItemWandCap;
import lunaglaxe7.claraww.adapter.ItemWandRod;
import lunaglaxe7.claraww.common.ModContents;

// regist items of claraww
public class TTRItems {

    public static boolean isOreDicRegisted;

    public static void itemRegister(FMLPreInitializationEvent event) {
        if (ItemNugget.nuggets == null) {
            return;
        }
        for (ItemNugget nugget : ItemNugget.nuggets) {
            GameRegistry.registerItem(nugget, nugget.name, ModContents.MODID);
            isOreDicRegisted = OreDictionary.doesOreNameExist(nugget.name);
            if (isOreDicRegisted) continue;
            OreDictionary.registerOre(nugget.name, nugget);
        }
        GameRegistry.registerItem(ItemWandCap.instance, ItemWandCap.instance.getUnlocalizedName(), ModContents.MODID);
        GameRegistry.registerItem(ItemWandRod.instance, ItemWandRod.instance.getUnlocalizedName(), ModContents.MODID);
    }
}
