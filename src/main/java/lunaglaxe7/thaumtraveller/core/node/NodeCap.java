package lunaglaxe7.thaumtraveller.core.node;

import lunaglaxe7.thaumtraveller.core.DefaultTemplateMatching;
import lunaglaxe7.thaumtraveller.core.ICraftTemplateMatching;
import lunaglaxe7.thaumtraveller.core.IItemAccessor;

import java.util.Map;

public class NodeCap {

    private String tag;
    private int craftCost;
    private float baseDiscount;
    private Map<String, Float> discount;
    private IItemAccessor material;
    private ICraftTemplateMatching template;
    private boolean isMagical;

    NodeCap(String tag, int craftCost, float baseDiscount, Map<String, Float> discount, boolean isMagical, IItemAccessor material) {
        this(tag,craftCost,baseDiscount,discount,isMagical,material,isMagical ? DefaultTemplateMatching.defaultMagicalMatching : DefaultTemplateMatching.defaultNaturalMatching);
    }

    NodeCap(String tag, int craftCost, float baseDiscount, Map<String, Float> discount, boolean isMagical, IItemAccessor material, ICraftTemplateMatching template) {
        this.tag = tag;
        this.craftCost = craftCost;
        this.baseDiscount = baseDiscount;
        this.discount = discount;
        this.isMagical = isMagical;
        this.material = material;
        this.template = template;
    }

    public NodeCap() {}

    public String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    public int getCraftCost() {
        return craftCost;
    }

    void setCraftCost(int craftCost) {
        this.craftCost = craftCost;
    }

    public float getBaseDiscount() {
        return baseDiscount;
    }

    void setBaseDiscount(float baseDiscount) {
        this.baseDiscount = baseDiscount;
    }

    public Map<String, Float> getDiscount() {
        return discount;
    }

    public float getDiscount(String aspect) {
        if (aspect != null && discount.containsKey(aspect)) {
            return discount.get(aspect);
        }
        return 999f;
    }

    void setDiscount(Map<String, Float> discount) {
        this.discount = discount;
    }

    public Object getMaterialItem(){
        return material.getItem();
    }

    public ICraftTemplateMatching getTemplate(){
        return this.template;
    }
}
