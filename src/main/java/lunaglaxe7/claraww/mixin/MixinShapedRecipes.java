package lunaglaxe7.claraww.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import lunaglaxe7.claraww.accessor.ShapedRecipesAccessor;

@Mixin(ShapedRecipes.class)
public class MixinShapedRecipes implements ShapedRecipesAccessor {

    @Shadow
    private ItemStack recipeOutput;

    @Override
    public void ttr$setOutput(ItemStack out) {
        this.recipeOutput = out;
    }
}
