package lunaglaxe7.thaumtraveller.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import lunaglaxe7.thaumtraveller.adapter.WandAdapter;
import lunaglaxe7.thaumtraveller.api.BodyEssence;
import lunaglaxe7.thaumtraveller.adapter.AspectHelper;
import lunaglaxe7.thaumtraveller.common.event.TravelEventManager;
import lunaglaxe7.thaumtraveller.common.network.NetHandler;

public class CommonProxy {

    BodyEssence bodyEssence = new BodyEssence();

    public void preInit(FMLPreInitializationEvent event) {
        NetHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ThaumTraveller.instance, new TTRContents());
    }

    public void postInit(FMLPostInitializationEvent event) {
        AspectHelper.init();
        TravelEventManager.init();
        WandAdapter.init();
    }

    public BodyEssence getBodyEssence() {
        return bodyEssence;
    }
}
