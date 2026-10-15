package lunaglaxe7.claraww.client.entity;

import lunaglaxe7.claraww.LogHandler;
import lunaglaxe7.claraww.common.entity.EntityWandFly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.gear.ModelWand;

public class EntityWandRender extends Render {
    private ModelWand model = new ModelWand();
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float f) {
        if (entity instanceof EntityWandFly wand){
            ItemStack s = wand.getWand();
            if (s == null) return;
            GL11.glPushMatrix();
            GL11.glTranslated(x, y+1.5D, z);
            GL11.glRotated(-yaw-90,0,1,0);

            GL11.glTranslated(2,0,0);
            GL11.glRotated(90,0,0,1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            model.render(s);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
