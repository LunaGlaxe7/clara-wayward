package lunaglaxe7.claraww.core.node;

public class NodeRod {

    private String tag;
    private int capacity;
    private int craftCost;
    private boolean glow;
    private boolean runes;

    private boolean staff;

    public NodeRod() {}

    NodeRod(String tag, int capacity, int craftCost, boolean glow) {
        this(tag, capacity, craftCost, glow, false);
    }

    NodeRod(String tag, int capacity, int craftCost, boolean glow, boolean runes) {
        this.tag = tag;
        this.capacity = capacity;
        this.craftCost = craftCost;
        this.glow = glow;
        this.calculateStaff();
    }

    public String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    public int getCapacity() {
        return capacity;
    }

    void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCraftCost() {
        return craftCost;
    }

    void setCraftCost(int craftCost) {
        this.craftCost = craftCost;
    }

    public boolean isGlow() {
        return glow;
    }

    void setGlow(boolean glow) {
        this.glow = glow;
    }

    private void calculateStaff() {
        staff = this.tag.contains("_staff");
    }

    public boolean isStaff() {
        return staff;
    }
}
