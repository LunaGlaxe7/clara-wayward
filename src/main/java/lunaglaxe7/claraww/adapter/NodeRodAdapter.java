package lunaglaxe7.claraww.adapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.Optional;
import lunaglaxe7.claraww.common.ModContents;
import lunaglaxe7.claraww.core.node.ItemId;
import lunaglaxe7.claraww.core.node.NodeRod;
import lunaglaxe7.claraww.core.node.RodManager;
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
