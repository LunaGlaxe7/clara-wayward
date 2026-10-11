package lunaglaxe7.claraww.adapter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;

// matches -> getCraftingResult
// matches -> getAspects() -> getAspects(inv)
// others never used
public class ArcaneCapRecipe implements IArcaneRecipe {

    @Override
    public boolean matches(IInventory inv, World world, EntityPlayer player) {
        ItemStack item;
        int[] a = new int[] { 0, 1, 2, 7 };
        int[] b = new int[] { 4, 6, 7, 8 };
        boolean typeA = true, typeB = true;
        for (int i : a) {
            if (inv.getStackInSlot(transposeIndex(i)) != null) {
                typeA = false;
                break;
            }
        }
        if (!typeA) {
            for (int i : b) {
                if (inv.getStackInSlot(transposeIndex(i)) != null) {
                    typeB = false;
                    break;
                }
            }
        }
        if (typeA || typeB) {
            item = inv.getStackInSlot(transposeIndex(3));
            // item seems not to be able to a nugget name
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory var1) {
        return null;
    }

    private int transposeIndex(int i) {
        int row = i % 3;
        row = 2 - row;
        int col = i / 3;
        col = 2 - col;
        return col + row * 3;
    }

    @Override
    public AspectList getAspects(IInventory var1) {
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public AspectList getAspects() {
        return null;
    }

    @Override
    public String getResearch() {
        return "";
    }
}
