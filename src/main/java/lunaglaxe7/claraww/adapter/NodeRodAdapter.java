package lunaglaxe7.claraww.adapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.Optional;
import lunaglaxe7.claraww.accessor.InfusionRecipeAccessor;
import lunaglaxe7.claraww.common.ModContents;
import lunaglaxe7.claraww.core.node.ItemId;
import lunaglaxe7.claraww.core.node.NodeRod;
import lunaglaxe7.claraww.core.node.RodManager;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.wands.WandRod;

public class NodeRodAdapter {

    private static final Map<ItemId, String> rodItemMap = new ConcurrentHashMap<>();

    public static void init() {
        fromTC4();
    }

    @Optional.Method(modid = ModContents.TCID)
    private static void fromTC4() {
        NodeRod rod;
        for (WandRod r : WandRod.rods.values()) {
            ItemStack item = r.getItem();
            if (item != null && item.getItem() != null) {
                registerRodItemId(item.getItem(), item.getItemDamage(), r.getTag());
            }
            if (RodManager.checkTag(r.getTag())) continue;
            if (r.getTag() != null) {
                rod = RodManager.build(r.getTag(), r.getCraftCost(), r.getCapacity(), r.isGlowing());
                RodManager.registerRod(rod);
            }
        }
    }

    public static IIcon getIcon(int id) {
        String tag = RodManager.getById(id).getTag();
        WandRod r = WandRod.rods.get(tag);
        if (r != null) {
            ItemStack rod = r.getItem();
            return rod.getIconIndex();
        }
        return null;
    }

    public static void registerRodItemId(Item item, int meta, String tag) {
        int itemId = Item.getIdFromItem(item);
        if (itemId > 0 && !rodItemMap.containsValue(tag)) {
            rodItemMap.put(new ItemId(itemId, meta), tag);
        }
    }

    private static void modifyRecipes() {
        var recipes = ThaumcraftApi.getCraftingRecipes();
        for (Object o : recipes) {
            if (o instanceof ShapedArcaneRecipe s) {
                ItemStack out = s.output;
                int id = updateRods(out);
                if (id >= 0) {
                    s.output = new ItemStack(ItemWandRod.instance, 1, id);
                }
                Object[] in = s.input;
                if (in != null) {
                    for (int i = 0; i < in.length; i++) {
                        if (in[i] instanceof ItemStack stack) {
                            int n = updateRods(stack);
                            if (n >= 0) {
                                in[i] = new ItemStack(ItemWandRod.instance, 1, n);
                            }
                        }
                    }
                }
            }

            if (o instanceof InfusionRecipe f) {
                Object out = f.getRecipeOutput();
                if (out instanceof ItemStack) {
                    int id = updateRods((ItemStack) out);
                    if (id >= 0) {
                        if (o instanceof InfusionRecipeAccessor a) {
                            a.ttr$setOutput(new ItemStack(ItemWandRod.instance, 1, id));
                        }
                    }
                }
                ItemStack[] c = f.getComponents();
                if (c != null) {
                    for (int i = 0; i < c.length; i++) {
                        int id = updateRods(c[i]);
                        if (id >= 0) {
                            c[i] = new ItemStack(ItemWandRod.instance, 1, id);
                        }
                    }
                }
            }
        }
    }

    public static int updateRods(ItemStack rod) {
        if (rod != null && rod.getItem() != null) {
            int itemId = Item.getIdFromItem(rod.getItem());
            if (itemId > 0) {
                String tag = rodItemMap.get(new ItemId(itemId, rod.getItemDamage()));
                if (RodManager.checkTag(tag)) {
                    return RodManager.getId(tag);
                }
            }
        }
        return -1;
    }
}
