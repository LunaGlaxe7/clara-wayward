package lunaglaxe7.claraww.mixin;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import lunaglaxe7.claraww.accessor.InfusionRecipeAccessor;
import thaumcraft.api.crafting.InfusionRecipe;

@Mixin(value = InfusionRecipe.class, remap = false)
public class MixinInfusionRecipe implements InfusionRecipeAccessor {

    @Shadow
    protected Object recipeOutput;

    @Shadow
    private ItemStack[] components;

    public void ttr$setOutput(Object out) {
        this.recipeOutput = out;
    }

}
