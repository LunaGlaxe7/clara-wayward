package lunaglaxe7.claraww.adapter;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import lunaglaxe7.claraww.common.event.CapInfoEvent;
import lunaglaxe7.claraww.common.event.ModEventManager;
import lunaglaxe7.claraww.core.node.CapManager;
import lunaglaxe7.claraww.core.node.RodManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandCap;

public class WandAdapter {

    public static void setRod(ItemStack wand, ItemStack rod) {
        wand.setTagInfo("rod", new NBTTagString(RodManager.getById(rod.getItemDamage()).getTag()));
        if (rod.hasTagCompound()) wand.setTagInfo("rod#", rod.getTagCompound());
    }

    public static void setCapsNbt(ItemStack wand, ItemStack cap1, ItemStack cap2) {
        if (cap1.hasTagCompound()) wand.setTagInfo("cap1#", cap1.getTagCompound());
        if (cap2.hasTagCompound()) wand.setTagInfo("cap2#", cap2.getTagCompound());
    }

    public static void setCaps(ItemStack wand, ItemStack cap1, ItemStack cap2) {
        wand.setTagInfo("cap1", new NBTTagString(CapManager.getTagById(cap1.getItemDamage())));
        wand.setTagInfo("cap2", new NBTTagString(CapManager.getTagById(cap2.getItemDamage())));
    }

    public static boolean hasNBTKey(ItemStack item, String key) {
        if (item != null && item.getItem() != null) {
            return item.hasTagCompound() && item.getTagCompound().hasKey(key);
        }
        return false;
    }

    public static ItemStack getTopCapItem(ItemStack wand) {
        return getCapItemFromWand(wand, 2);
    }

    public static ItemStack getBotCapItem(ItemStack wand) {
        return getCapItemFromWand(wand, 1);
    }

    public static WandCap getTopCap(ItemStack wand) {
        return getWandCapFromWand(wand, 2);
    }

    public static WandCap getBotCap(ItemStack wand) {
        return getWandCapFromWand(wand, 1);
    }

    public static String getTagFromCap(ItemStack cap) {
        return isCap(cap) ? getTagFromCapNoCheck(cap) : "null";
    }

    public static String getTagFromCapNoCheck(ItemStack cap) {
        return CapManager.getTagById(cap.getItemDamage());
    }

    public static String getTagFromRod(ItemStack rod) {
        if (rod.getItem() instanceof ItemWandRod) {
            return RodManager.getById(rod.getItemDamage()).getTag();
        }
        if (rod.isItemEqual(new ItemStack(Items.stick))) {
            return "wood";
        }
        return "null";
    }

    public static boolean isCap(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemWandCap;
    }

    public static boolean isRod(ItemStack stack) {
        return stack != null
                && (stack.getItem() instanceof ItemWandRod || stack.isItemEqual(new ItemStack(Items.stick)));
    }

    public static int rodCraftCost(ItemStack rod) {
        if (isRod(rod)) {
            if (rod.isItemEqual(new ItemStack(Items.stick))) return 1;
            return RodManager.getById(rod.getItemDamage()).getCraftCost();
        }
        // shouldn't go here
        return 999;
    }

    // wand cant be not ItemWandCasting
    public static float calculateDiscount(ItemStack wand, Aspect a) {
        float out = 999f;
        if (wand.hasTagCompound()) {
            NBTTagCompound nbt = wand.getTagCompound();
            if (!nbt.hasKey("cap") && !nbt.hasKey("cap1")) out = CapManager.getDiscount("iron", a.getTag());
            // will be deleted
            if (nbt.hasKey("cap")) out = CapManager.getDiscount(nbt.getString("cap"), a.getTag());
            if (nbt.hasKey("cap1")) {
                if (!hasNBTKey(wand, "sceptre")) {
                    out = (CapManager.getDiscount(nbt.getString("cap1"), a.getTag())
                            * ModEventManager.multiplyDiscountModifiers(new CapInfoEvent.CapDiscountMul(wand, 1))
                            * ModEventManager
                                    .multiplySpecialDiscountModifiers(new CapInfoEvent.SpecialDiscountMul(wand, a, 1))
                            + CapManager.getDiscount(nbt.getString("cap2"), a.getTag())
                                    * ModEventManager
                                            .multiplyDiscountModifiers(new CapInfoEvent.CapDiscountMul(wand, 2))
                                    * ModEventManager.multiplySpecialDiscountModifiers(
                                            new CapInfoEvent.SpecialDiscountMul(wand, a, 2)))
                            / 2f;
                } else {
                    out = (CapManager.getDiscount(nbt.getString("cap1"), a.getTag())
                            + CapManager.getDiscount(nbt.getString("cap2"), a.getTag())) / 2f;
                }
            }
        }
        return out;
    }

    public static int capCraftCost(ItemStack cap1, ItemStack cap2) {
        int out = 999;
        if (cap1 != null && cap2 != null) {
            int c1 = CapManager.getCraftCost(cap1.getItemDamage());
            int c2 = CapManager.getCraftCost(cap2.getItemDamage());
            out = (int) (c1 * ModEventManager.capCostModifiers(new CapInfoEvent.CapCraftCost(cap1))
                    + c2 * ModEventManager.capCostModifiers(new CapInfoEvent.CapCraftCost(cap2))) / 2;
        }

        return Math.max(out, 1);
    }

    private static void checkOldWands(ItemStack wand) {
        if (wand.getTagCompound().hasKey("cap")) {
            String tag = wand.getTagCompound().getString("cap");
            wand.setTagInfo("cap1", new NBTTagString(tag));
            wand.setTagInfo("cap2", new NBTTagString(tag));
            wand.getTagCompound().removeTag("cap");
        }
        if (!wand.getTagCompound().hasKey("cap") && !wand.getTagCompound().hasKey("cap1")) {
            wand.setTagInfo("cap1", new NBTTagString("iron"));
            wand.setTagInfo("cap2", new NBTTagString("iron"));
        }
    }

    public static WandCap getWandCapFromWand(ItemStack wand, int index) {
        if (wand.hasTagCompound()) {
            checkOldWands(wand);
            String s = wand.getTagCompound().getString(index == 1 ? "cap1" : "cap2");
            if (s != null && WandCap.caps.containsKey(s)) return WandCap.caps.get(s);
        }
        return null;
    }

    public static ItemStack getCapItemFromWand(ItemStack wand, int index) {
        ItemStack out = null;
        if (wand != null) {
            WandCap cap = getWandCapFromWand(wand, index);
            if (cap != null) {
                out = cap.getItem().copy();
                out.setTagInfo("tag", new NBTTagString(cap.getTag()));
                String key = index == 1 ? "cap1#" : "cap2#";
                if (wand.hasTagCompound() && wand.getTagCompound().hasKey(key)) {
                    NBTTagCompound nbt = wand.getTagCompound().getCompoundTag(key);
                    out.setTagInfo(key, nbt);
                }
            }
        }
        return out;
    }

    public static void init() {
        NodeCapAdapter.init();
        NodeRodAdapter.init();
        AspectAdapter.init();
    }
}
