package lunaglaxe7.claraww.adapter;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import lunaglaxe7.claraww.core.node.CapManager;

public class ItemWandCap extends Item {

    public static ItemWandCap instance = new ItemWandCap();

    public ItemWandCap() {
        this.setMaxDamage(0);
        this.setMaxStackSize(64);
        this.setUnlocalizedName("WandCap");
        this.setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        IIcon icon = NodeCapAdapter.getIcon(Math.min(damage, CapManager.getCapCount() - 1));
        return icon == null ? super.getIconFromDamage(damage) : icon;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        // Thanks to Azanor
        int id = itemStack.getItemDamage();
        String tag = CapManager.getTagById(id);
        return StatCollector.translateToLocal("item.Wand." + tag + ".cap");
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> subItems) {
        for (int i = 0; i < CapManager.getCapCount(); i++) {
            subItems.add(new ItemStack(this, 1, i));
        }
    }
}
