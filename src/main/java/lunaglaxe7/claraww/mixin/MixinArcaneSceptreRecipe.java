package lunaglaxe7.claraww.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cpw.mods.fml.common.registry.GameRegistry;
import lunaglaxe7.claraww.adapter.AspectHelper;
import lunaglaxe7.claraww.adapter.WandAdapter;
import lunaglaxe7.claraww.common.ModContents;
import lunaglaxe7.claraww.core.node.CapManager;
import lunaglaxe7.claraww.core.node.RodManager;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ArcaneSceptreRecipe;

@Mixin(value = ArcaneSceptreRecipe.class, remap = false)
public class MixinArcaneSceptreRecipe {

    @Inject(method = "getCraftingResult", cancellable = true, at = @At("HEAD"))
    private void diffOut(IInventory inv, CallbackInfoReturnable<ItemStack> c) {
        ItemStack out = null;
        if (checkTemplate(inv)) {
            ItemStack cap = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0);
            ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
            String tagC = CapManager.getTagById(cap.getItemDamage());
            String tagR = RodManager.getById(rod.getItemDamage()).getTag();

            int costC = CapManager.getCraftCost(cap.getItemDamage()),
                    costR = RodManager.getById(rod.getItemDamage()).getCraftCost();

            out = new ItemStack(
                    GameRegistry.findItem(ModContents.TCID, "WandCasting"),
                    1,
                    (int) ((costC * costR) * 1.5f));
            out.setTagInfo("cap", new NBTTagString(tagC));
            out.setTagInfo("rod", new NBTTagString(tagR));
            out.setTagInfo("sceptre", new NBTTagByte((byte) 1));
        }
        c.setReturnValue(out);
        c.cancel();
    }

    @Inject(method = "getAspects", cancellable = true, at = @At("HEAD"))
    private void aspects(IInventory inv, CallbackInfoReturnable<AspectList> c) {
        AspectList out = new AspectList();
        if (checkTemplate(inv)) {
            ItemStack cap = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0);
            ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
            int costC = CapManager.getCraftCost(cap.getItemDamage()),
                    costR = RodManager.getById(rod.getItemDamage()).getCraftCost();
            int cost = (int) ((costC * costR) * 1.5f);
            for (Aspect a : AspectHelper.getPrimalAspects()) {
                out.add(a, cost);
            }
        }
        c.setReturnValue(out);
        c.cancel();
    }

    @Inject(method = "matches", cancellable = true, at = @At("HEAD"))
    private void check(IInventory inv, World world, EntityPlayer player, CallbackInfoReturnable<Boolean> c) {
        boolean out = ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "SCEPTRE")
                && checkTemplate(inv);
        c.setReturnValue(out);
        c.cancel();
    }

    private boolean checkTemplate(IInventory inv) {
        boolean nullTemplate = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null
                && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null
                && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null
                && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null;
        boolean sceptreParts = false;
        ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0);
        ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1);
        ItemStack cap3 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
        ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
        ItemStack focus = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
        if (cap1 != null && cap2 != null && cap3 != null && rod != null && focus != null) {
            if (WandAdapter.isCap(cap1) && WandAdapter.isCap(cap2)
                    && WandAdapter.isCap(cap3)
                    && WandAdapter.isRod(rod)) {
                if (focus.getItem() != null
                        && focus.getItem() == GameRegistry.findItem(ModContents.TCID, "ItemResource")
                        && focus.getItemDamage() == 15) {
                    if (cap1.getItemDamage() == cap2.getItemDamage() && cap1.getItemDamage() == cap3.getItemDamage()) {
                        sceptreParts = true;
                    }
                }
            }
        }
        return nullTemplate && sceptreParts;
    }
}
