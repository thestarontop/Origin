package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.RenderNamePlateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.modules.misc.MidClick;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NameTags extends Module {
    private final Minecraft mc = Minecraft.getInstance();
    private static final float NAME_TAG_SCALE = 0.05F;
    private static final int DEFAULT_COLOR = new Color(255, 255, 255).getRGB();
    private static final int FRIEND_COLOR = new Color(39, 250, 31, 255).getRGB();

    public NameTags() {
        super("NameTags", "Render player nametags", Category.RENDER);
    }

    @EventTarget
    public void onRenderNamePlate(RenderNamePlateEvent event) {
        Entity entity = event.getEntity();
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1f, -1000000F);
        renderPlayerName(entity, event.getPosestack(), event.getBufferSource(), 15728880);
        GL11.glPolygonOffset(1f, 1000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
    }



    private void renderPlayerName(Entity player, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();

        poseStack.pushPose();
        poseStack.translate(0.0D, player.getBbHeight() + 0.5F, 0.0D);
        poseStack.mulPose(renderManager.cameraOrientation());
        poseStack.scale(-NAME_TAG_SCALE, -NAME_TAG_SCALE, NAME_TAG_SCALE);

        String playerName = player.getDisplayName().getString();
        int color = DEFAULT_COLOR;
        int nameWidth = mc.font.width(playerName) / 2;
        poseStack.translate(-nameWidth, 0, 0);

        if (player instanceof Player entity) {
            if (MidClick.isFriend(entity)) {
                color = FRIEND_COLOR;
            }
            playerName += "§a" + entity.getHealth() + "§c❤";
        }

        mc.font.drawInBatch(playerName, 0, 0, color, true, poseStack.last().pose(), buffer, true, 0, packedLight);
        poseStack.popPose();
    }
}
