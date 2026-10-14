package lunaglaxe7.claraww.common.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import lunaglaxe7.claraww.core.node.CapManager;
import lunaglaxe7.claraww.core.node.RodManager;

public class DataWandParts extends WorldSavedData {

    public static final String KEY = "cww.capsrods";
    public Map<Integer, String> prevCapList = new ConcurrentHashMap<>();
    public Map<String, Integer> prevCapIdMap = new ConcurrentHashMap<>();
    public Map<Integer, String> prevRodList = new ConcurrentHashMap<>();
    public Map<String, Integer> prevRodIdMap = new ConcurrentHashMap<>();

    public DataWandParts(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound data = nbt.getCompoundTag("caps");
        int i = 0;
        NBTTagCompound n;
        while (data.hasKey("cap" + i)) {
            n = data.getCompoundTag("cap" + i);
            String tag = n.getString("tag");
            int id = n.getInteger("id");
            prevCapIdMap.put(tag, id);
            prevCapList.put(id, tag);
            i++;
        }

        data = nbt.getCompoundTag("rods");
        i = 0;
        while (data.hasKey("rod" + i)) {
            n = data.getCompoundTag("rod" + i);
            String tag = n.getString("tag");
            int id = n.getInteger("id");
            prevRodIdMap.put(tag, id);
            prevRodList.put(id, tag);
            i++;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound data = new NBTTagCompound();
        for (int i = 0; i < CapManager.getCapCount(); i++) {
            String tag = CapManager.getTagById(i);
            NBTTagCompound cap = new NBTTagCompound();
            cap.setString("tag", tag);
            cap.setInteger("id", i);
            data.setTag("cap" + i, cap);
        }
        nbt.setTag("caps", data);

        data = new NBTTagCompound();
        for (int i = 0; i < RodManager.getRodCount(); i++) {
            String tag = RodManager.getById(i).getTag();
            NBTTagCompound rod = new NBTTagCompound();
            rod.setString("tag", tag);
            rod.setInteger("id", i);
            data.setTag("rod" + i, rod);
        }
        nbt.setTag("rods", data);
    }
}
