package lunaglaxe7.thaumtraveller.core.node;

public class NodeAspect {

    private String tag;

    public NodeAspect() {}

    public NodeAspect(String tag) {
        this.tag = tag;
        if (!Aspect.aspects.containsKey(tag)) {
            Aspect.aspects.put(tag, this);
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
