package lunaglaxe7.claraww.common;

import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import lunaglaxe7.claraww.Config;
import lunaglaxe7.claraww.client.block.ModBlocks;
import lunaglaxe7.claraww.compat.Compat;
import lunaglaxe7.claraww.compat.CompatItems;
import lunaglaxe7.claraww.items.ItemNugget;
import thaumcraft.api.ItemApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class ModResearch {

    public static HashMap recipes = new HashMap();

    public static void addResearch() {
        addResearchBasic();
        if (Config.crossMod) {
            if (Compat.gc) {
                addResearchGC();
            }
            if (Compat.ae2) {
                addResearchAE();
            }
            if (Compat.tb) {
                addResearchTB();
            }
        }
    }

    public static void addResearchBasic() {
        ResearchCategories.registerCategory(
                ModContents.CWWRESEARCHCATKEY,
                new ResourceLocation("textures/items/leather_boots.png"),
                new ResourceLocation("claraww", "textures/misc/travel.png"));
        new ResearchItem(
                "CWW.TRAVEL",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.ELDRITCH, 1).add(Aspect.TRAVEL, 1),
                0,
                0,
                1,
                new ItemStack(Items.leather_boots)).setAutoUnlock().setRound()
                        .setPages(new ResearchPage("tc.research_page.CWW.TRAVEL")).registerResearchItem();
        new ResearchItem(
                "CWW.BEANTRANS",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.EXCHANGE, 2).add(Aspect.ENTROPY, 2).add(Aspect.PLANT, 2),
                -2,
                -1,
                1,
                ItemApi.getItem("itemManaBean", 0))
                        .setParents("CWW.TRAVEL")
                        .setPages(
                                new ResearchPage("tc.research_page.CWW.BEANTRANS"),
                                new ResearchPage((CrucibleRecipe) recipes.get("BeanTrans")))
                        .setConcealed().setItemTriggers(ItemApi.getItem("itemManaBean", 0)).registerResearchItem();

        new ResearchItem(
                ModContents.MISMATCHEDCAPKEY,
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.AURA, 5).add(Aspect.ENTROPY, 5).add(Aspect.ORDER, 5).add(Aspect.EARTH, 5)
                        .add(Aspect.MAGIC, 5),
                -4,
                5,
                3,
                CompatItems.ironCap).setParents("CWW.TRAVEL", "CAP_gold")
                        .setPages(new ResearchPage("tc.research_page." + ModContents.MISMATCHEDCAPKEY))
                        .registerResearchItem();

        new ResearchItem(
                "CWW.WANDFORGE",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.ENERGY, 5).add(Aspect.MAGIC, 5).add(Aspect.MECHANISM, 5),
                3,
                3,
                3,
                new ItemStack(ModBlocks.forge))
                        .setParents("VISPOWER")
                        .setPages(
                                new ResearchPage("tc.research_page.CWW.WANDFORGE.1"),
                                new ResearchPage((IArcaneRecipe) recipes.get("WandForge")),
                                new ResearchPage("tc.research_page.CWW.WANDFORGE.2"))
                        .registerResearchItem();

        new ResearchItem(
                "CWW.SILVERTREE",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.EXCHANGE, 1).add(Aspect.PLANT, 1).add(Aspect.POISON, 1),
                -3,
                -1,
                1,
                CompatItems.silverTree)
                        .setPages(
                                new ResearchPage("tc.research_page.CWW.SILVERTREE"),
                                new ResearchPage((CrucibleRecipe) (recipes.get("SilverTree"))))
                        .setSecondary().registerResearchItem();

        new ResearchItem(
                "CWW.WANDFLIGHT",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.AIR,3).add(Aspect.FLIGHT,5).add(Aspect.TRAVEL,3),
                0,-2,3,
                new ItemStack(Items.feather)
        ).setPages(
                new ResearchPage("tc.research_page.CWW.WANDFLIGHT")
        ).setParents("CWW.TRAVEL", "HOVERHARNESS").registerResearchItem();
    }

    public static void addResearchGC() {
        new ResearchItem(
                "CWW.MOON",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(ModAspects.VOYAGE, 1).add(Aspect.ELDRITCH, 1).add(Aspect.EARTH, 1),
                2,
                1,
                1,
                CompatItems.t1Spaceship[0]).setParents("CWW.TRAVEL").setLost().setConcealed()
                        .setItemTriggers(CompatItems.grassMoon, CompatItems.dirtMoon)
                        .setPages(new ResearchPage("tc.research_page.CWW.MOON")).registerResearchItem();
        new ResearchItem(
                "CWW.METEORIC",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.METAL, 3).add(Aspect.FIRE, 3).add(Aspect.CRYSTAL, 3),
                4,
                0,
                1,
                CompatItems.fallenMeteor).setParents("CWW.MOON").setLost().setConcealed()
                        .setItemTriggers(CompatItems.fallenMeteor, CompatItems.rawMeteo, CompatItems.ingotMeteo)
                        .setSecondary()
                        .setPages(
                                new ResearchPage("tc.research_page.CWW.METEORIC"),
                                new ResearchPage(CompatItems.rawMeteo),
                                new ResearchPage((IRecipe) recipes.get("nuggetMeteoricIron")))
                        .registerResearchItem();
        new ResearchItem(
                "CWW.TRANSIRON_METEO",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 3).add(Aspect.FIRE, 3)
                        .add(Aspect.CRYSTAL, 3),
                5,
                -2,
                1,
                new ItemStack(ItemNugget.meteoricIron)).setSecondary().setParents("CWW.METEORIC", "TRANSIRON")
                        .setConcealed()
                        .setPages(
                                new ResearchPage("tc.research_page.CWW.TRANSIRON_METEO"),
                                new ResearchPage((CrucibleRecipe) recipes.get("nuggetMeteoricIronTransiron")))
                        .registerResearchItem();
    }

    public static void addResearchAE() {
        new ResearchItem(
                "CWW.CHARGE_CERTUS",
                ModContents.CWWRESEARCHCATKEY,
                new AspectList().add(Aspect.ENERGY, 3).add(Aspect.CRYSTAL, 1),
                -2,
                2,
                1,
                CompatItems.certusCharged)
                        .setParents("CWW.TRAVEL", "CRUCIBLE")
                        .setPages(
                                new ResearchPage("tc.research_page.CWW.CHARGE_CERTUS"),
                                new ResearchPage((CrucibleRecipe) recipes.get("certusCharge")))
                        .registerResearchItem();
    }

    public static void addResearchTB() {
        ResearchPage[] pages = new ResearchPage[8];
        pages[0] = new ResearchPage("tc.research_page.CWW.VOIDSEED");
        for (int i = 1; i < 8; i++) {
            pages[i] = new ResearchPage((InfusionRecipe) recipes.get("voidSeed" + (i - 1)));
        }
        new ResearchItem(
                "CWW.VOIDSEED",
                "THAUMICBASES",
                new AspectList().add(Aspect.CROP, 1),
                17,
                4,
                1,
                CompatItems.voidSeed).setParents("TB.VoidSeed").setPages(pages).registerResearchItem();
    }
}
