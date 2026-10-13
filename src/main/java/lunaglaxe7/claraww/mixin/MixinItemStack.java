package lunaglaxe7.claraww.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lunaglaxe7.claraww.adapter.ItemWandCap;
import lunaglaxe7.claraww.adapter.ItemWandRod;
import lunaglaxe7.claraww.adapter.WandAdapter;
import lunaglaxe7.claraww.core.node.CapManager;
import lunaglaxe7.claraww.core.node.ItemId;
import lunaglaxe7.claraww.core.node.RodManager;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemStack.class)
public class MixinItemStack {

    @Shadow
    int itemDamage;

    @Shadow
    private Item field_151002_e;

    @Shadow
    private void func_150996_a(Item item) {}

    @Inject(method = "writeToNBT", at = @At(value = "HEAD"))
    private void writeTag(NBTTagCompound nbt, CallbackInfoReturnable<NBTTagCompound> c) {
        Item item = field_151002_e;
        int id = Item.getIdFromItem(item);
        if (WandAdapter.claimId(new ItemId(id, itemDamage))) {
            return;
        }
        if (item instanceof ItemWandCap) {
            nbt.setString("WandCapTag", CapManager.getTagById(itemDamage));
        }
        if (item instanceof ItemWandRod) {
            nbt.setString("WandRodTag", RodManager.getById(itemDamage).getTag());
        }
    }

    @Inject(method = "readFromNBT", at = @At(value = "TAIL"))
    private void readTag(NBTTagCompound nbt, CallbackInfo c) {
        ItemStack stack = (ItemStack) (Object) this;
        if (WandAdapter.claimId(new ItemId(Item.getIdFromItem(field_151002_e), stack.getItemDamage()))) return;
        // if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG)
        // && stack.getTagCompound().getLong(TAG) == value)
        // return;
        // make sure item correct to its tag
        if (nbt.hasKey("WandCapTag")) {
            String tag = nbt.getString("WandCapTag");
            int id = CapManager.getId(tag);
            if (id >= 0) {
                itemDamage = id;
            } else {
                // when tag is lost (cap removed)
                func_150996_a((Item) null);
            }
            return;
        }

        if (nbt.hasKey("WandRodTag")) {
            String tag = nbt.getString("WandRodTag");
            int id = RodManager.getId(tag);
            if (id >= 0) {
                itemDamage = id;
            } else {
                func_150996_a((Item) null);
            }
            return;
        }

        if (field_151002_e instanceof ItemWandCasting) {
            // check old wands when reading
            checkOldWands(stack);
            return;
        }

        // universalize caps and rods
        // int id;
        // id = NodeCapAdapter.updateCaps(stack);
        // if (CapManager.checkId(id)) {
        // func_150996_a(ItemWandCap.instance);
        // itemDamage = id;
        // stack.setTagInfo(TAG, new NBTTagLong(value));
        // return;
        // }
        //
        // id = NodeRodAdapter.updateRods(stack);
        // if (RodManager.checkId(id)) {
        // func_150996_a(ItemWandRod.instance);
        // itemDamage = id;
        // stack.setTagInfo(TAG, new NBTTagLong(value));
        // return;
        // }
        //
        // stack.setTagInfo(TAG, new NBTTagLong(value));
    }

    private void checkOldWands(ItemStack wand) {
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

}
