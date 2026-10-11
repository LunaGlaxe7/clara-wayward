package lunaglaxe7.thaumtraveller.items;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import lunaglaxe7.thaumtraveller.adapter.ItemWandCap;
import lunaglaxe7.thaumtraveller.adapter.ItemWandRod;
import lunaglaxe7.thaumtraveller.common.TTRContents;

// regist items of thaumtraveller
public class TTRItems {

    public static boolean isOreDicRegisted;

    public static void itemRegister(FMLPreInitializationEvent event) {
        if (ItemNugget.nuggets == null) {
            return;
        }
        for (ItemNugget nugget : ItemNugget.nuggets) {
            GameRegistry.registerItem(nugget, nugget.name, TTRContents.MODID);
            isOreDicRegisted = OreDictionary.doesOreNameExist(nugget.name);
            if (isOreDicRegisted) continue;
            OreDictionary.registerOre(nugget.name, nugget);
        }
        GameRegistry.registerItem(ItemWandCap.instance, ItemWandCap.instance.getUnlocalizedName(), TTRContents.MODID);
        GameRegistry.registerItem(ItemWandRod.instance, ItemWandRod.instance.getUnlocalizedName(), TTRContents.MODID);
    }
}
