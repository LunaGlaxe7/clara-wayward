package lunaglaxe7.thaumtraveller.core.node;

import java.util.Objects;

public class ItemId {
    int id;
    int meta;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemId itemId)) return false;
        return id == itemId.id && meta == itemId.meta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, meta);
    }

    public ItemId(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    public int getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }
}
