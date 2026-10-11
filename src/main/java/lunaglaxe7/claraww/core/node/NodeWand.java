package lunaglaxe7.claraww.core.node;

import java.util.List;

public class NodeWand {

    private List<NodeCap> caps;
    private List<NodeRod> rods;

    public NodeWand() {}

    public NodeWand(List<NodeRod> rods, List<NodeCap> caps) {
        this.rods = rods;
        this.caps = caps;
    }

    public List<NodeCap> getCaps() {
        return caps;
    }

    public void setCaps(List<NodeCap> caps) {
        this.caps = caps;
    }

    public List<NodeRod> getRods() {
        return rods;
    }

    public void setRods(List<NodeRod> rods) {
        this.rods = rods;
    }
}
