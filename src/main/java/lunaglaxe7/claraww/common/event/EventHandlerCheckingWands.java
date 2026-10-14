package lunaglaxe7.claraww.common.event;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lunaglaxe7.claraww.adapter.ItemWandCap;
import lunaglaxe7.claraww.adapter.ItemWandRod;
import lunaglaxe7.claraww.core.node.CapManager;
import lunaglaxe7.claraww.core.node.RodManager;

public class EventHandlerCheckingWands {

    public Map<Integer, String> diffCap = new ConcurrentHashMap<>();
    public Map<Integer, String> diffRod = new ConcurrentHashMap<>();
    public Set<String> chunkChecked = new HashSet<>();

    private boolean capDiffTag = false;
    private boolean rodDiffTag = false;

    @SubscribeEvent
    public void checkData(WorldEvent.Load event) {
        World world = event.world;
        if (!world.isRemote && world.provider.dimensionId == 0) {
            DataWandParts data = (DataWandParts) world.mapStorage.loadData(DataWandParts.class, DataWandParts.KEY);
            if (data == null) {
                data = new DataWandParts(DataWandParts.KEY);
                world.mapStorage.setData(DataWandParts.KEY, data);
                data.setDirty(true);
            } else {
                if (checkCaps(data)) {
                    capDiffTag = true;
                    data.setDirty(true);
                }
                if (checkRods(data)) {
                    rodDiffTag = true;
                    data.setDirty(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void checkPlayer(PlayerEvent.LoadFromFile event) {
        var player = event.entityPlayer;
        if (player != null && !player.getEntityWorld().isRemote) {
            if (capDiffTag) {
                changeCaps(player.inventory);
                changeCaps(player.getInventoryEnderChest());
            }
            if (rodDiffTag) {
                changeRods(player.inventory);
                changeRods(player.getInventoryEnderChest());
            }
        }
    }

    @SubscribeEvent
    public void checkChunk(ChunkEvent.Load event) {
        var chunk = event.getChunk();
        if (chunk != null && !event.world.isRemote) {
            int dim = chunk.worldObj.provider.dimensionId;
            int x = chunk.xPosition;
            int z = chunk.zPosition;
            String cor = dim + ":" + x + "," + z;
            if (chunkChecked.add(cor)) {
                var entities = chunk.entityLists;
                for (List l : entities) {
                    for (Object o : l) {
                        if (o instanceof EntityItem item) {
                            ItemStack s = item.getEntityItem();
                            if (s.getItem() instanceof ItemWandCap cap) {
                                String tag = diffCap.get(s.getItemDamage());
                                if (tag != null) {
                                    int id = CapManager.getId(tag);
                                    if (id < 0) {
                                        item.setEntityItemStack(null);
                                    } else {
                                        item.setEntityItemStack(new ItemStack(cap, s.stackSize, id));
                                    }
                                }
                            }

                            if (s.getItem() instanceof ItemWandRod rod) {
                                String tag = diffRod.get(s.getItemDamage());
                                if (tag != null) {
                                    int id = RodManager.getId(tag);
                                    if (id < 0) {
                                        item.setEntityItemStack(null);
                                    } else {
                                        item.setEntityItemStack(new ItemStack(rod, s.stackSize, id));
                                    }
                                }
                            }
                        }
                    }
                }

                var tiles = chunk.chunkTileEntityMap.values();
                for (TileEntity t : tiles) {
                    if (t instanceof IInventory inv) {
                        changeCaps(inv);
                        changeRods(inv);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void clearChunkChecked(WorldEvent.Unload event) {
        if (!event.world.isRemote) {
            chunkChecked.clear();
        }
    }

    private void changeCaps(IInventory inv) {
        if (inv != null) {
            int id;
            String tag;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                var s = inv.getStackInSlot(i);
                if (s != null && s.getItem() instanceof ItemWandCap cap) {
                    tag = diffCap.get(s.getItemDamage());
                    if (tag != null) {
                        id = CapManager.getId(tag);
                        if (id < 0) {
                            inv.setInventorySlotContents(i, null);
                        } else {
                            inv.setInventorySlotContents(i, new ItemStack(cap, s.stackSize, id));
                        }
                    }
                }
            }
        }
    }

    private void changeRods(IInventory inv) {
        if (inv != null) {
            int id;
            String tag;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                var s = inv.getStackInSlot(i);
                if (s != null && s.getItem() instanceof ItemWandRod rod) {
                    tag = diffRod.get(s.getItemDamage());
                    if (tag != null) {
                        id = RodManager.getId(tag);
                        if (id < 0) {
                            inv.setInventorySlotContents(i, null);
                        } else {
                            inv.setInventorySlotContents(i, new ItemStack(rod, s.stackSize, id));
                        }
                    }
                }
            }
        }
    }

    private boolean checkCaps(DataWandParts data) {
        boolean diffTag = false;
        // 新Map中是否有tag与旧Map不同
        for (int i = 0; i < CapManager.getCapCount(); i++) {
            String tag1 = CapManager.getTagById(i);
            String tag2 = data.prevCapList.get(i);
            if (!Objects.equals(tag1, tag2)) {
                diffTag = true;
                if (tag2 != null) {
                    diffCap.put(i, tag2);
                }
            }
        }
        // 新Map比旧Map容量小
        if (data.prevCapIdMap.size() > CapManager.getCapCount()) {
            diffTag = true;
            for (int i : data.prevCapIdMap.values()) {
                if (i >= CapManager.getCapCount()) {
                    String tag = data.prevCapList.get(i);
                    diffCap.put(i, tag);
                }
            }
        }
        return diffTag;
    }

    private boolean checkRods(DataWandParts data) {
        boolean diffTag = false;
        // 新Map中是否有tag与旧Map不同
        for (int i = 0; i < RodManager.getRodCount(); i++) {
            String tag1 = RodManager.getById(i).getTag();
            String tag2 = data.prevRodList.get(i);
            if (!Objects.equals(tag1, tag2)) {
                diffTag = true;
                if (tag2 != null) {
                    diffRod.put(i, tag2);
                }
            }
        }
        // 新Map比旧Map容量小
        if (data.prevRodIdMap.size() > RodManager.getRodCount()) {
            diffTag = true;
            for (int i : data.prevRodIdMap.values()) {
                if (i >= RodManager.getRodCount()) {
                    String tag = data.prevRodList.get(i);
                    diffRod.put(i, tag);
                }
            }
        }
        return diffTag;
    }
}
