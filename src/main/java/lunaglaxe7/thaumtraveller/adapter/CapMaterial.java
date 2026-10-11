package lunaglaxe7.thaumtraveller.adapter;

import lunaglaxe7.thaumtraveller.core.IItemAccessor;
import lunaglaxe7.thaumtraveller.core.IMaterialAccessor;
import net.minecraftforge.oredict.OreDictionary;

public class CapMaterial implements IItemAccessor, IMaterialAccessor {
    private String tag;

    public CapMaterial(String tag) {
        this.tag = tag;
    }

    public Object getItem() {
        return tag;
    }

    @Override
    public String getMaterialName() {
        return tag;
    }
}
