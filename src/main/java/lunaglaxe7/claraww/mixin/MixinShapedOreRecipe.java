package lunaglaxe7.claraww.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import lunaglaxe7.claraww.accessor.ShapedOreRecipeAccessor;

@Mixin(value = ShapedOreRecipe.class, remap = false)
public class MixinShapedOreRecipe implements ShapedOreRecipeAccessor {

    @Shadow
    private ItemStack output;

    @Override
    public void ttr$setOutput(ItemStack out) {
        this.output = out;
    }
}
