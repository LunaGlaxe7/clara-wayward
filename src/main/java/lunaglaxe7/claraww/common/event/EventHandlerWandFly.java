package lunaglaxe7.claraww.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lunaglaxe7.claraww.common.entity.EntityWandFly;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.items.wands.ItemWandCasting;

public class EventHandlerWandFly {

    @SubscribeEvent
    public void setWandFly(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR
                || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (event.entityPlayer != null && event.world != null) {
                EntityPlayer player = event.entityPlayer;
                ItemStack stack = player.getHeldItem();
                if (stack != null && stack.getItem() instanceof ItemWandCasting wand) {
                    var focus = wand.getFocus(stack);
                    if (focus == null && wand.isStaff(stack)
                            && ThaumcraftApiHelper
                                    .isResearchComplete(player.getCommandSenderName(), "CWW.WANDFLIGHT")) {
                        if (player.ridingEntity == null && !event.world.isRemote) {
                            World world = event.world;
                            EntityWandFly fly = new EntityWandFly(world);
                            fly.setWand(stack.copy());
                            fly.setPosition(player.posX, player.posY, player.posZ);
                            world.spawnEntityInWorld(fly);
                            player.mountEntity(fly);

                            stack.stackSize = 0;
                        }
                    }
                }
            }
        }
    }
}
