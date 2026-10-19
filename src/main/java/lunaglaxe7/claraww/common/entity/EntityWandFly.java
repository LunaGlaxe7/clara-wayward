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
    private int visConsume = 20;
    private int tickCounting = 0;

    public EntityWandFly(World world) {
        super(world);
        this.speedFactor = 0.7f;
    }

    public ItemStack getWand() {
        if (this.wand == null) {
            this.wand = this.dataWatcher.getWatchableObjectItemStack(WANDKEY);
        }
        return this.wand;
    }

    public void setWand(ItemStack wand) {
        this.wand = wand;
        this.dataWatcher.updateObject(WANDKEY, wand);
    }

    public void addTickCounting() {
        this.tickCounting = this.dataWatcher.getWatchableObjectInt(21);
        this.tickCounting++;
        this.dataWatcher.updateObject(21, this.tickCounting);
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

                double speedCount = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                // 限速
                if (speedCount > 2) {
                    double keep = 2 / speedCount;
                    this.motionX *= keep;
                    this.motionY *= keep;
                    this.motionZ *= keep;
                }

                this.moveEntity(motionX, motionY, motionZ);

                player.fallDistance = 0;

                // 阻力
                if (player.moveForward == 0 && player.moveStrafing == 0) {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }
            }

            if (riddenByEntity == null) {
                addTickCounting();
                this.wand = null;
                // 延迟2s清除
                if (this.tickCounting > 40) {
                    this.setDead();
                }
            }

            if (riddenByEntity != null) {
                if (riddenByEntity instanceof EntityPlayerMP player) {
                    if (getWand() != null && wand.getItem() instanceof ItemWandCasting w) {
                        boolean consume = true;
                        // 每秒消耗一次
                        if (player.ticksExisted % 20 == 0) {
                            consume = w.consumeAllVis(
                                    this.wand,
                                    player,
                                    new AspectList().add(Aspect.AIR, visConsume),
                                    true,
                                    false);
                            if (consume) {
                                this.dataWatcher.updateObject(WANDKEY, this.wand);
                            }
                        }
                        if (!consume || player.isSneaking()) {
                            riddenByEntity.mountEntity(null);

                            // give wand back to player
                            if (!player.inventory.addItemStackToInventory(getWand().copy())) {
                                EntityItem wand = new EntityItem(
                                        worldObj,
                                        player.posX,
                                        player.posY,
                                        player.posZ,
                                        getWand().copy());
                                wand.delayBeforeCanPickup = 0;
                                wand.func_145797_a(player.getCommandSenderName());// set owner
                                worldObj.spawnEntityInWorld(wand);
                            }
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
        this.dataWatcher.addObject(21, 0);
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
