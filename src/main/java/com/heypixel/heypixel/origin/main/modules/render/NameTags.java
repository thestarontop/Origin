package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.event.events.Render3DEvent;
import com.heypixel.heypixel.origin.main.event.events.RenderNamePlateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.modules.misc.MidClick;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class NameTags extends Module {
    public NameTags() {
        super("NameTags","Render player nametags", Category.RENDER);
    }



    @EventTarget
    public void onRenderNamePlate(RenderNamePlateEvent event){
        this.renderPlayerName(event.getEntity(), event.getPosestack(),event.getBufferSource(),15728880);
    }




    private void renderPlayerName(Entity player, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();

        // Push the current state onto the stack
        poseStack.pushPose();

        // Translate to the player's position
        poseStack.translate(0.0D, player.getBbHeight() + 0.5F, 0.0D);

        // Rotate to face the camera
        poseStack.mulPose(renderManager.cameraOrientation());

        // Scale down the text
        float scale = 0.05F;  
        poseStack.scale(-scale, -scale, scale);
        //poseStack.scale(-0.025F, -0.025F, 0.025F);

        // Get the player's name and compute the width
        String playerName = player.getDisplayName().getString();
        int color = new Color(255,255,255).getRGB();
        int nameWidth = mc.font.width(playerName) / 2;

        // Translate the text to be centered
        poseStack.translate(-nameWidth, 0, 0);

        // Render the name
        if (!(player instanceof Player entity)) {
            mc.font.drawInBatch(playerName, 0, 0, color, true, poseStack.last().pose(), buffer, true, 0, packedLight);
        }else {
            if (MidClick.isFriend(entity)) {
                playerName = "[Friend]" + playerName;
                color = new Color(39, 250, 31, 255).getRGB();
            }

            mc.font.drawInBatch(playerName + "  Health：" + ((Player) player).getHealth(), 0, 0, color, true, poseStack.last().pose(), buffer, true, 0, packedLight);
        }
        // Pop the current state off the stack
        poseStack.popPose();
    }
}

