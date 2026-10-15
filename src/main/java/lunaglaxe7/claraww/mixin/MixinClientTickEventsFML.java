package lunaglaxe7.claraww.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.common.gameevent.TickEvent;
import lunaglaxe7.claraww.common.entity.EntityWandFly;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ClientTickEventsFML.class, remap = false)
public class MixinClientTickEventsFML {

    @Shadow
    private void renderCastingWandHud(Float f, EntityPlayer player, long time, ItemStack stack) {}

    @Inject(
            method = "renderTick",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;getCurrentItem()Lnet/minecraft/item/ItemStack;",
                    ordinal = 0))
    private void alsoRender(TickEvent.RenderTickEvent event, CallbackInfo c) {
        if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer player) {
            if (player.ridingEntity instanceof EntityWandFly f) {
                ItemStack current = player.inventory.getCurrentItem();
                if (current == null || !(current.getItem() instanceof ItemWandCasting)) {
                    ItemStack wand = f.getWand();
                    if (wand != null) {
                        renderCastingWandHud(event.renderTickTime, player, System.currentTimeMillis(), wand);
                    }
                }
            }
        }
    }
}
