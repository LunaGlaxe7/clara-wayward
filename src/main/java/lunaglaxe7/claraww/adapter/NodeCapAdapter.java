package lunaglaxe7.claraww.adapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.Level;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.Optional;
import lunaglaxe7.claraww.LogHandler;
import lunaglaxe7.claraww.accessor.InfusionRecipeAccessor;
import lunaglaxe7.claraww.accessor.ShapedOreRecipeAccessor;
import lunaglaxe7.claraww.accessor.ShapedRecipesAccessor;
import lunaglaxe7.claraww.common.ModContents;
import lunaglaxe7.claraww.core.MaterialCates;
import lunaglaxe7.claraww.core.node.Aspect;
import lunaglaxe7.claraww.core.node.CapManager;
import lunaglaxe7.claraww.core.node.ItemId;
import lunaglaxe7.claraww.core.node.NodeAspect;
import lunaglaxe7.claraww.core.node.NodeCap;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.wands.WandCap;

public class NodeCapAdapter {

    private static final Map<ItemId, String> capItemMap = new ConcurrentHashMap<>();

    public static void init() {
        materialsFromJson();
        vanilaCapsAdjust();
        fromTC4();
    }

    public static IIcon getIcon(int id) {
        String tag = CapManager.getTagById(id);
        WandCap c = WandCap.caps.get(tag);
        if (c != null) {
            ItemStack oldCap = c.getItem();
            return oldCap.getIconIndex();
        }
        return null;
    }

    // get id mapping old caps
    public static int updateCaps(ItemStack cap) {
        if (cap != null && cap.getItem() != null) {
            int itemId = Item.getIdFromItem(cap.getItem());
            if (itemId >= 0) {
                String tag = capItemMap.get(new ItemId(itemId, cap.getItemDamage()));
                if (CapManager.checkTag(tag)) {
                    return CapManager.getId(tag);
                }
            }
        }
        return -1;
    }

    public static void materialsFromJson() {
        try {
            IResource r = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("claraww", "config/magicalMaterial.json"));

            InputStream i = r.getInputStream();
            Reader re = new InputStreamReader(i, StandardCharsets.UTF_8);
            JsonArray j = new JsonParser().parse(re).getAsJsonArray();
            String[] materials = new Gson().fromJson(j, String[].class);
            for (String s : materials) {
                MaterialCates.registerMagicalCapMaterial(s);
            }

            r = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("claraww", "config/naturalMaterial.json"));
            i = r.getInputStream();
            re = new InputStreamReader(i, StandardCharsets.UTF_8);
            j = new JsonParser().parse(re).getAsJsonArray();
            materials = new Gson().fromJson(j, String[].class);
            for (String s : materials) {
                MaterialCates.registerNaturalCapMaterial(s);
            }

        } catch (Exception e) {
            LogHandler.log(Level.ERROR, "can't load materials from json");
        }
    }

    private static void vanilaCapsAdjust() {
        NodeCap node = build("iron", 1, 1.1f, Arrays.asList(thaumcraft.api.aspects.Aspect.EARTH), 1, false);
        CapManager.registerCap(node);

        node = build("gold", 3, 1.05f, Arrays.asList(thaumcraft.api.aspects.Aspect.WATER), 0.95f, false);
        CapManager.registerCap(node);

        node = build("thaumium", 9, 0.92f, Arrays.asList(thaumcraft.api.aspects.Aspect.EARTH), 0.82f, true);
        CapManager.registerCap(node);

        node = build(
                "void",
                12,
                0.85f,
                Arrays.asList(thaumcraft.api.aspects.Aspect.AIR, thaumcraft.api.aspects.Aspect.ENTROPY),
                0.73f,
                true);
        CapManager.registerCap(node);
    }

    @Optional.Method(modid = ModContents.TCID)
    private static void fromTC4() {
        for (String tag : WandCap.caps.keySet()) {
            if (tag == null) continue;
            WandCap cap = WandCap.caps.get(tag);
            ItemStack item = cap.getItem();
            if (item != null && item.getItem() != null) {
                registerCapItemId(item.getItem(), item.getItemDamage(), cap.getTag());
            }
            if (CapManager.checkTag(tag)) continue;
            NodeCap node;
            if (cap.getSpecialCostModifierAspects() != null) {
                node = build(
                        cap.getTag(),
                        cap.getCraftCost(),
                        cap.getBaseCostModifier(),
                        cap.getSpecialCostModifierAspects(),
                        cap.getSpecialCostModifier(),
                        MaterialCates.isMagicalCapMaterial(tag));
            } else {
                node = CapManager.build(
                        cap.getTag(),
                        cap.getCraftCost(),
                        cap.getBaseCostModifier(),
                        MaterialCates.isMagicalCapMaterial(tag),
                        new CapMaterial(tag));
            }
            CapManager.registerCap(node);
        }
        ModifyCapRecipes();
    }

    /**
     * Change all cap items in recipes to my item caps.
     */
    private static void ModifyCapRecipes() {
        List recipes = ThaumcraftApi.getCraftingRecipes();
        for (Object o : recipes) {
            if (o instanceof ShapedArcaneRecipe) {
                ShapedArcaneRecipe s = (ShapedArcaneRecipe) o;
                int id = updateCaps(s.output);
                if (id >= 0) {
                    s.output = new ItemStack(ItemWandCap.instance, 1, id);
                }
                Object[] in = s.input;
                if (in != null) {
                    for (int i = 0; i < in.length; i++) {
                        if (in[i] instanceof ItemStack) {
                            id = updateCaps((ItemStack) in[i]);
                            if (id >= 0) {
                                s.input[i] = new ItemStack(ItemWandCap.instance, 1, id);
                            }
                        }
                    }
                }
            }
            if (o instanceof InfusionRecipe) {
                InfusionRecipe f = (InfusionRecipe) o;
                Object out = f.getRecipeOutput();
                if (out instanceof ItemStack) {
                    int id = updateCaps((ItemStack) out);
                    if (id >= 0) {
                        if (o instanceof InfusionRecipeAccessor) {
                            InfusionRecipeAccessor a = (InfusionRecipeAccessor) o;
                            a.ttr$setOutput(new ItemStack(ItemWandCap.instance, 1, id));
                        }
                    }
                }
                ItemStack[] c = f.getComponents();
                if (c != null) {
                    for (int i = 0; i < c.length; i++) {
                        int id = updateCaps(c[i]);
                        if (id >= 0) {
                            c[i] = new ItemStack(ItemWandCap.instance, 1, id);
                        }
                    }
                }
            }
        }

        recipes = CraftingManager.getInstance().getRecipeList();
        for (Object o : recipes) {
            if (o instanceof ShapedRecipes) {
                ShapedRecipes s = (ShapedRecipes) o;
                ItemStack out = s.getRecipeOutput();
                int id = updateCaps(out);
                if (id >= 0) {
                    if (o instanceof ShapedRecipesAccessor) {
                        ShapedRecipesAccessor a = (ShapedRecipesAccessor) o;
                        a.ttr$setOutput(new ItemStack(ItemWandCap.instance, 1, id));
                    }
                }
            }
            if (o instanceof ShapedOreRecipe) {
                ShapedOreRecipe s = (ShapedOreRecipe) o;
                ItemStack out = s.getRecipeOutput();
                int id = updateCaps(out);
                if (id >= 0) {
                    if (o instanceof ShapedOreRecipeAccessor) {
                        ShapedOreRecipeAccessor a = (ShapedOreRecipeAccessor) o;
                        a.ttr$setOutput(new ItemStack(ItemWandCap.instance, 1, id));
                    }
                }
            }
        }
    }

    public static void registerCapItemId(Item item, int meta, String tag) {
        int itemId = Item.getIdFromItem(item);
        if (itemId > 0 && !capItemMap.containsValue(tag)) {
            capItemMap.put(new ItemId(itemId, meta), tag);
        }
    }

    public static NodeCap build(String tag, int craftCost, float baseDiscount,
            List<thaumcraft.api.aspects.Aspect> aspects, float specialDiscount, boolean isMagical) {
        Map<String, Float> discount = new ConcurrentHashMap<>();
        for (thaumcraft.api.aspects.Aspect a : aspects) {
            discount.put(a.getTag(), specialDiscount);
        }
        List<NodeAspect> primal = Aspect.getPrimalAspects();
        for (NodeAspect nodeAspect : primal) {
            String t = nodeAspect.getTag();
            if (discount.containsKey(t)) continue;
            discount.put(t, baseDiscount);
        }
        return CapManager.build(tag, craftCost, baseDiscount, discount, isMagical, new CapMaterial(tag));
    }
}
