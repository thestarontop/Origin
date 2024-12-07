package net.java.main.modules.render;

import com.mojang.math.Matrix4f;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.RenderNamePlateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.modules.misc.MidClick;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.utils.RenderUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class NameTags extends Module {

    public NameTags() {
        super("NameTags", "Render player nametags", Category.RENDER);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (mc.level != null && mc.player != null) {
            for (Entity entity : mc.level.entitiesForRendering()) {

                if (entity instanceof Player || mc.player.distanceTo(entity) <= 5.0) {
                    boolean isItem = entity instanceof ItemEntity;
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(
                            entity.xOld + (entity.getX() - entity.xOld) * event.getTickcounter() - mc.gameRenderer.getMainCamera().getPosition().x,
                            entity.yOld + (entity.getY() - entity.yOld) * event.getTickcounter() - mc.gameRenderer.getMainCamera().getPosition().y + entity.getEyeHeight() + 1,
                            entity.zOld + (entity.getZ() - entity.zOld) * event.getTickcounter() - mc.gameRenderer.getMainCamera().getPosition().z
                    );

                    event.getPoseStack().mulPose(mc.gameRenderer.getMainCamera().rotation());
                    if (isItem) {
                        event.getPoseStack().scale(-0.02F, -0.02F, 0.02F);
                    } else {
                        event.getPoseStack().scale(-0.04F, -0.04F, 0.04F);
                    }

                    Component s = entity.getDisplayName();
                    String s1;
                    if (isItem) {
                        s1 = " × " + ((ItemEntity) entity).getItem().getCount();
                    } else {
                        s1 = " Health: " + ((LivingEntity) entity).getHealth();
                    }

                    float width = mc.font.width(s.getString() + s1);

                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPolygonOffset(1f, -1000000F);

                    int color;
                    if (isItem) {
                        color = new Color(0,0,0,100).getRGB();
                    } else {
                        color = new Color(0,0,0,150).getRGB();
                    }

                    RenderUtils.drawRect(event.getPoseStack(),-((width + 8) / 2f),0,(width + 8) / 2f,mc.font.lineHeight + 8,color);
                    mc.font.drawShadow(event.getPoseStack(),s,4 -((width + 8) / 2f),4,Color.white.getRGB());
                    mc.font.drawShadow(event.getPoseStack(),s1,4 -((width + 8) / 2f) + mc.font.width(s),4,Color.white.getRGB());

                    GL11.glPolygonOffset(1f, 1000000F);
                    GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);


                    event.getPoseStack().popPose();
                }
            }
        }
    }



    public void renderNameTag(Entity p_114498_, Component p_114499_, PoseStack p_114500_, MultiBufferSource p_114501_, int p_114502_) {
        double d0 = distanceToSqr(p_114498_);
        if (!(d0 > 4096.0D)) {
            boolean flag = !p_114498_.isDiscrete();
            float f = p_114498_.getBbHeight() + 0.5F;
            int i = "deadmau5".equals(p_114499_.getString()) ? -10 : 0;
            p_114500_.pushPose();
            p_114500_.translate(0.0D, (double)f, 0.0D);
            p_114500_.mulPose(mc.gameRenderer.getMainCamera().rotation());
            p_114500_.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = p_114500_.last().pose();
            float f1 = mc.options.getBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            Font font = mc.font;
            float f2 = (float)(-font.width(p_114499_) / 2);
            font.drawInBatch(p_114499_, f2, (float)i, 553648127, false, matrix4f, p_114501_, flag, j, p_114502_);
            if (flag) {
                font.drawInBatch(p_114499_, f2, (float)i, -1, false, matrix4f, p_114501_, false, 0, p_114502_);
            }

            p_114500_.popPose();
        }
    }

    public double distanceToSqr(Entity p_114472_) {
        return mc.gameRenderer.getMainCamera().getPosition().distanceToSqr(p_114472_.position());
    }


}
