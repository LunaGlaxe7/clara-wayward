package lunaglaxe7.claraww.adapter;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import lunaglaxe7.claraww.core.node.NodeRod;
import lunaglaxe7.claraww.core.node.RodManager;

public class ItemWandRod extends Item {

    public static ItemWandRod instance = new ItemWandRod();

    public ItemWandRod() {
        this.setHasSubtypes(true);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setUnlocalizedName("WandRod");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int damage = stack.getItemDamage();
        if (RodManager.checkId(damage)) {
            NodeRod rod = RodManager.getById(damage);
            String tag = rod.getTag();
            if (rod.isStaff()) {
                tag = tag.substring(0, tag.indexOf("_staff"));
            }
            return StatCollector.translateToLocal("item.Wand." + tag + ".rod")
                    + StatCollector.translateToLocal("item.wandParts.rod" + (rod.isStaff() ? ".staff" : ""));
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        IIcon icon = NodeRodAdapter.getIcon(Math.min(damage, RodManager.getRodCount() - 1));
        return icon == null ? super.getIconFromDamage(damage) : icon;
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> out) {
        for (int i = 0; i < RodManager.getRodCount(); i++) {
            out.add(new ItemStack(this, 1, i));
        }
    }
}
