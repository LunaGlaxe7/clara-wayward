package lunaglaxe7.claraww.common.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lunaglaxe7.claraww.common.entity.EntityWandFly;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.wands.IWandable;
import thaumcraft.api.wands.WandTriggerRegistry;
import thaumcraft.common.items.wands.ItemWandCasting;

public class EventHandlerWandFly {

    private final Map<String, Integer> playerCheck = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void setWandFly(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR
                || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (event.entityPlayer != null && event.world != null && !event.world.isRemote) {
                World world = event.world;
                EntityPlayer player = event.entityPlayer;
                // 不可以同时进入这个逻辑两次
                if (playerCheck.containsKey(player.getCommandSenderName())
                        && playerCheck.get(player.getCommandSenderName()) == player.ticksExisted) {
                    playerCheck.remove(player.getCommandSenderName());
                    return;
                }
                ItemStack stack = player.getHeldItem();
                if (stack != null && stack.getItem() instanceof ItemWandCasting wand) {
                    // 不与法杖的其他交互同时发生
                    if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                        int x = event.x;
                        int y = event.y;
                        int z = event.z;
                        Block b = world.getBlock(x, y, z);
                        int meta = world.getBlockMetadata(x, y, z);
                        if (b instanceof BlockBookshelf || b instanceof IWandable
                                || WandTriggerRegistry.hasTrigger(b, meta)) {
                            setPlayerCheck(player);
                            return;
                        }
                        TileEntity tile = world.getTileEntity(x, y, z);
                        if (tile instanceof IWandable) {
                            setPlayerCheck(player);
                            return;
                        }
                    }

                    var focus = wand.getFocus(stack);
                    if (focus == null && wand.isStaff(stack)
                            && ThaumcraftApiHelper
                                    .isResearchComplete(player.getCommandSenderName(), "CWW.WANDFLIGHT")) {
                        if (player.ridingEntity == null) {
                            EntityWandFly fly = new EntityWandFly(world);
                            fly.setWand(stack.copy());
                            fly.setPosition(player.posX, player.posY, player.posZ);
                            world.spawnEntityInWorld(fly);
                            player.mountEntity(fly);

                            setPlayerCheck(player);

                            stack.stackSize = 0;
                        }
                    }
                }
            }
        }
    }

    private void setPlayerCheck(EntityPlayer player) {
        String tag = player.getCommandSenderName();
        int c = player.ticksExisted;
        playerCheck.put(tag, c);
    }
}
