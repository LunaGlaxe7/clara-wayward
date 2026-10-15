package lunaglaxe7.claraww.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;

public class EntityWandFly extends Entity {

    private float speedFactor;
    private ItemStack wand;
    private final int WANDKEY = 20;
    private int visConsume = 1;

    public EntityWandFly(World world) {
        super(world);
        this.speedFactor = 0.7f;
    }

    public ItemStack getWand() {
        if (this.wand != null) {
            return this.wand;
        }
        return this.dataWatcher.getWatchableObjectItemStack(WANDKEY);
    }

    public void setWand(ItemStack wand) {
        this.wand = wand;
        this.dataWatcher.updateObject(WANDKEY, wand);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        recordPrevPos(posX, posY, posZ);
        if (!worldObj.isRemote) {
            if (riddenByEntity != null && !riddenByEntity.isSneaking()
                    && riddenByEntity instanceof EntityPlayer player) {
                double horizontal_direction = Math.toRadians(player.rotationYaw + -player.moveStrafing * 90D);
                this.motionX += -Math.sin(horizontal_direction) * speedFactor * player.moveForward * 0.05D;
                this.motionZ += Math.cos(horizontal_direction) * speedFactor * player.moveForward * 0.05D;

                double vertical_direction = Math.toRadians(player.rotationPitch);
                this.motionY += -Math.sin(vertical_direction) * speedFactor * 0.5D * player.moveForward * 0.05D;

                this.rotationYaw = player.rotationYaw;

                player.fallDistance = 0;
            }
            double speedCount = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            // 限速
            if (speedCount > 2) {
                double keep = 2 / speedCount;
                this.motionX *= keep;
                this.motionY *= keep;
                this.motionZ *= keep;
            }

            this.moveEntity(motionX, motionY, motionZ);
            // 阻力
            this.motionX *= 0.95D;
            this.motionY *= 0.95D;
            this.motionZ *= 0.95D;

        }

        if (!worldObj.isRemote) {
            if (riddenByEntity == null) {
                this.wand = null;
                this.setDead();
            }

            if (riddenByEntity != null) {
                if (riddenByEntity instanceof EntityPlayerMP player) {
                    if (this.wand != null && wand.getItem() instanceof ItemWandCasting w) {
                        if (!w.consumeAllVis(
                                this.wand,
                                player,
                                new AspectList().add(Aspect.AIR, visConsume),
                                true,
                                false) || player.isSneaking()) {
                            // give wand back to player
                            EntityItem wand = player.dropPlayerItemWithRandomChoice(this.wand.copy(), false);
                            wand.delayBeforeCanPickup = 0;
                            wand.func_145797_a(player.getCommandSenderName());// set owner

                            this.wand = null;
                            riddenByEntity.mountEntity(null);
                            this.setDead();
                        }
                    }
                }
            }
        }
    }

    private void recordPrevPos(double x, double y, double z) {
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObjectByDataType(WANDKEY, 5);// 5 -> ItemStack
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = nbt.getCompoundTag("entityWandItem");
        ItemStack wand = new ItemStack((Item) null);
        wand.readFromNBT(tag);
        this.setWand(wand);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        wand.writeToNBT(tag);
        nbt.setTag("entityWandItem", tag);
    }
}
