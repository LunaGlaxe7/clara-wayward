package lunaglaxe7.thaumtraveller.mixin;

import lunaglaxe7.thaumtraveller.accessor.InfusionRecipeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.crafting.InfusionRecipe;

@Mixin(value = InfusionRecipe.class, remap = false)
public class MixinInfusionRecipe implements InfusionRecipeAccessor {
    @Shadow
    protected Object recipeOutput;


    public void ttr$setOutput(Object out){
        this.recipeOutput = out;
    }
}
