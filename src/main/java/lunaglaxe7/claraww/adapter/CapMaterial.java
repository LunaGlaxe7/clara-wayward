package lunaglaxe7.claraww.adapter;

import lunaglaxe7.claraww.core.IItemAccessor;
import lunaglaxe7.claraww.core.IMaterialAccessor;

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
