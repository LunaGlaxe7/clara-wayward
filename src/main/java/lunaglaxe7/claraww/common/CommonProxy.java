package lunaglaxe7.claraww.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import lunaglaxe7.claraww.Config;
import lunaglaxe7.claraww.adapter.AspectHelper;
import lunaglaxe7.claraww.adapter.WandAdapter;
import lunaglaxe7.claraww.api.BodyEssence;
import lunaglaxe7.claraww.common.entity.EntityWandFly;
import lunaglaxe7.claraww.common.event.ModEventManager;
import lunaglaxe7.claraww.common.network.NetHandler;

public class CommonProxy {

    BodyEssence bodyEssence = new BodyEssence();

    public void preInit(FMLPreInitializationEvent event) {
        EntityRegistry.registerModEntity(
                EntityWandFly.class,
                "wandFlying",
                999,
                ClaraWayward.instance,
                80,
                Config.wandFlightUpdateFrequency,
                true);
        NetHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ClaraWayward.instance, new ModContents());
    }

    public void postInit(FMLPostInitializationEvent event) {
        AspectHelper.init();
        ModEventManager.init();
        WandAdapter.init();
    }

    public BodyEssence getBodyEssence() {
        return bodyEssence;
    }
}
