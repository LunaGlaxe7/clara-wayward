package lunaglaxe7.thaumtraveller.adapter;

import lunaglaxe7.thaumtraveller.LogHandler;
import lunaglaxe7.thaumtraveller.core.node.CapManager;
import lunaglaxe7.thaumtraveller.core.node.ItemId;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Optional;
import lunaglaxe7.thaumtraveller.common.TTRContents;
import lunaglaxe7.thaumtraveller.core.node.NodeRod;
import lunaglaxe7.thaumtraveller.core.node.RodManager;
import net.minecraft.util.IIcon;
import org.apache.logging.log4j.Level;
import thaumcraft.api.wands.WandRod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeRodAdapter {
    private static final Map<ItemId, String> rodItemMap = new ConcurrentHashMap<>();

    public static void init() {
        fromTC4();
    }

    @Optional.Method(modid = TTRContents.TCID)
    private static void fromTC4() {
        NodeRod rod;
        for (WandRod r : WandRod.rods.values()) {
            ItemStack item = r.getItem();
            if (item != null && item.getItem() != null){
                registerRodItemId(item.getItem(),item.getItemDamage(),r.getTag());
            }
            if (RodManager.checkTag(r.getTag())) continue;
            if (r.getTag() != null) {
                rod = RodManager.build(r.getTag(), r.getCraftCost(), r.getCapacity(), r.isGlowing());
                RodManager.registerRod(rod);
            }
        }
    }

    public static IIcon getIcon(int id){
        String tag = RodManager.getById(id).getTag();
        WandRod r = WandRod.rods.get(tag);
        if (r != null){
            ItemStack rod = r.getItem();
            return rod.getIconIndex();
        }
        return null;
    }

    public static void registerRodItemId(Item item, int meta, String tag){
        int itemId = Item.getIdFromItem(item);
        if (itemId > 0 && !rodItemMap.containsValue(tag)){
            rodItemMap.put(new ItemId(itemId,meta),tag);
        }
    }

    public static int updateRods(ItemStack rod) {
        if (rod != null && rod.getItem() != null) {
            int itemId = Item.getIdFromItem(rod.getItem());
            if (itemId > 0){
                String tag = rodItemMap.get(new ItemId(itemId,rod.getItemDamage()));
                if (RodManager.checkTag(tag)){
                    return RodManager.getId(tag);
                }
            }
        }
        return -1;
    }
}
