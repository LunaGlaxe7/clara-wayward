/**
 * This document is created by <lunaglaxe7>. I recreate it from decompile of ThaumTraveller.class because the origin
 * ThaumTraveller.java by lunaglaxe7 at 2021 has lost.
 *
 * Renamed to ClaraWayward at 10 / 11 / 2026.
 */
package lunaglaxe7.claraww.common;

import net.minecraftforge.common.MinecraftForge;

import baubles.api.expanded.BaubleExpandedSlots;
import baubles.common.BaublesExpanded;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import lunaglaxe7.claraww.Config;
import lunaglaxe7.claraww.api.BodyEssence;
import lunaglaxe7.claraww.client.block.ModBlocks;
import lunaglaxe7.claraww.common.event.EventHandlerBodyEssence;
import lunaglaxe7.claraww.common.tile.TileForge;
import lunaglaxe7.claraww.compat.Compat;
import lunaglaxe7.claraww.compat.CompatItems;
import lunaglaxe7.claraww.items.ItemNugget;
import lunaglaxe7.claraww.items.ModItems;

@Mod(
        modid = ModContents.MODID,
        name = ModContents.MODID,
        dependencies = "required-after:" + BaublesExpanded.MODID
                + ";"
                + "required-after:Thaumcraft;after:GalacticraftCore;"
                + // "after:GrimoireOfGaia;" +
                "after:ForbiddenMagic;"
                + "after:appliedenergistics2;after:thaumicbases")
public class ClaraWayward {

    @Mod.Instance(value = ModContents.MODID)
    public static ClaraWayward instance;
    @SidedProxy(
            clientSide = "lunaglaxe7.claraww.client.ClientProxy",
            serverSide = "lunaglaxe7.claraww.common.CommonProxy")
    public static CommonProxy proxy;
    public EventHandlerBodyEssence bodyEssenceHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        Config.configure(event.getSuggestedConfigurationFile());
        BaubleExpandedSlots.tryAssignSlotOfType(BaubleExpandedSlots.beltType);
        // Compat.initiate();
        ModBlocks.registerBlocks();
        GameRegistry.registerTileEntity(TileForge.class, "cww_forge_tile");
        ItemNugget.addNuggets();
        ModItems.itemRegister(event);
        ModAspects.initAspects();

        bodyEssenceHandler = new EventHandlerBodyEssence();

        MinecraftForge.EVENT_BUS.register(this.bodyEssenceHandler);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CompatItems.importItems();
        ModAspects.addAspects();
        Compat.compatify();
        ModRecipes.addRecipes();
        ModResearch.addResearch();
        // if (Compat.gc) {
        // CompatItems.importGCItems();
        // Compat.compatifyGC();
        // TTRRecipes.addRecipesGC();
        // TTRResearch.addResearchGC();
        // }
        // if (Compat.gaia) {
        // Compat.compatifyGaia();
        // }
        // if (Compat.ae2) {
        // CompatItems.importAEItems();
        // TTRRecipes.addRecipesAE();
        // TTRResearch.addResearchAE();
        // }
        // if (Compat.tb) {
        // CompatItems.importTBItems();
        // TTRRecipes.addRecipesTB();
        // TTRResearch.addResearchTB();
        // }
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void registerCommand(FMLServerStartingEvent event) {
        event.registerServerCommand(new BodyEssence.BodyEssenceCommand());
    }
}
