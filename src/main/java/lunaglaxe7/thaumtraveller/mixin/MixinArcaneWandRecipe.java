package lunaglaxe7.thaumtraveller.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cpw.mods.fml.common.registry.GameRegistry;
import lunaglaxe7.thaumtraveller.adapter.WandAdapter;
import lunaglaxe7.thaumtraveller.common.TTRContents;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.lib.crafting.ArcaneWandRecipe;

@Mixin(value = ArcaneWandRecipe.class, remap = false)
abstract class MixinArcaneWandRecipe {

    @Inject(method = "getCraftingResult", cancellable = true, at = @At("HEAD"))
    // at = @At(value = "INVOKE", target =
    // "thaumcraft/api/ThaumcraftApiHelper.getStackInRowAndColumn(Ljava/lang/Object;II)Lnet/minecraft/item/ItemStack;",
    // shift = At.Shift.AFTER,ordinal = 2))
    private void diffWand(IInventory inv, CallbackInfoReturnable<ItemStack> cb) {
        ItemStack out = null;
        ItemStack c1 = getStack(inv, 0, 2);
        ItemStack rod = getStack(inv, 1, 1);
        ItemStack c2 = getStack(inv, 2, 0);
        if (checkWandTemplate(inv)) {
            int costC = WandAdapter.capCraftCost(c1, c2);
            int costR = WandAdapter.rodCraftCost(rod);
            out = new ItemStack(GameRegistry.findItem(TTRContents.TCID, "WandCasting"), 1, costC * costR);
            WandAdapter.setCaps(out, c1, c2);
            WandAdapter.setCapsNbt(out, c1, c2);
            WandAdapter.setRod(out, rod);

            cb.setReturnValue(out);
            cb.cancel();
        }

    }

    @Inject(method = "getAspects", at = @At("HEAD"), cancellable = true)
    private void diffAspects(IInventory inv, CallbackInfoReturnable<AspectList> c) {
        AspectList out = new AspectList();
        ItemStack c1 = getStack(inv, 0, 2);
        ItemStack rod = getStack(inv, 1, 1);
        ItemStack c2 = getStack(inv, 2, 0);
        if (checkWandTemplate(inv)) {
            int costC = WandAdapter.capCraftCost(c1, c2);
            int costR = WandAdapter.rodCraftCost(rod);

            int cost = costC * costR;
            for (Aspect a : Aspect.getPrimalAspects()) {
                out.add(a, cost);
            }
            c.setReturnValue(out);
            c.cancel();
        }
    }

    @Inject(method = "checkMatch", cancellable = true, at = @At(value = "HEAD"))
    // now every two caps can make a wand
    private void diffCheck(ItemStack cap1, ItemStack cap2, ItemStack rod, EntityPlayer player,
            CallbackInfoReturnable<Boolean> c) {
        boolean out = false;
        if (WandAdapter.isCap(cap1) && WandAdapter.isCap(cap2) && WandAdapter.isRod(rod)) {
            String[] tag = new String[] { WandAdapter.getTagFromCapNoCheck(cap1), WandAdapter.getTagFromCapNoCheck(cap2),
                    WandAdapter.getTagFromRod(rod) };
            if (ThaumcraftApiHelper
                    .isResearchComplete(player.getCommandSenderName(), WandCap.caps.get(tag[0]).getResearch())
                    && ThaumcraftApiHelper
                            .isResearchComplete(player.getCommandSenderName(), WandCap.caps.get(tag[1]).getResearch())
                    && ThaumcraftApiHelper.isResearchComplete(
                            player.getCommandSenderName(),
                            WandRod.rods.get(tag[2]).getResearch())) {
                out = tag[0].equals(tag[1]) || ThaumcraftApiHelper
                        .isResearchComplete(player.getCommandSenderName(), TTRContents.MISMATCHEDCAPKEY);
            }
        }
        c.setReturnValue(out);
        c.cancel();
    }

    // 这一大截判断也太雷霆
    // 现在check之后不用再判断是不是cap了
    private boolean checkWandTemplate(IInventory inv) {
        ItemStack cap1 = getStack(inv, 0, 2);
        ItemStack rod = getStack(inv, 1, 1);
        ItemStack cap2 = getStack(inv, 2, 0);
        return WandAdapter.isCap(cap1) && WandAdapter.isCap(cap2)
                && WandAdapter.isRod(rod)
                && getStack(inv, 0, 0) == null
                && getStack(inv, 0, 1) == null
                && getStack(inv, 1, 0) == null
                && getStack(inv, 1, 2) == null
                && getStack(inv, 2, 1) == null
                && getStack(inv, 2, 2) == null;
    }

    private ItemStack getStack(IInventory inv, int row, int col) {
        return ThaumcraftApiHelper.getStackInRowAndColumn(inv, row, col);
    }

}
