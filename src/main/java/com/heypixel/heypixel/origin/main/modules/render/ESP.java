package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.event.events.Render3DEvent;
import com.heypixel.heypixel.origin.main.event.events.RenderLivingEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.RenderUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.entity.PartEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class ESP extends Module {
    public ESP(){super("ESP","render entities",Category.RENDER);}


    @EventTarget
    public void onRender3D(Render3DEvent event){
        for (Entity entity : mc.level.entitiesForRendering()) {
            if(entity.getId() != mc.player.getId() && entity instanceof Player) {
                PoseStack poseStack = event.getPoseStack();
                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
                double camX = dispatcher.camera.getPosition().x();
                double camY = dispatcher.camera.getPosition().y();
                double camZ = dispatcher.camera.getPosition().z();
                AABB boundingBox = entity.getBoundingBox().move(-camX, -camY, -camZ);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1f, -1000000F);
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);
                poseStack.pushPose();
                RenderSystem.setShaderColor(1F, 1F, 1F, 1.0F); // 设置颜色为红色
                LevelRenderer.renderLineBox(poseStack, vertexConsumer, boundingBox.minX, boundingBox.minY, boundingBox.minZ,
                        boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, 1.0F, 1F, 1F, 1.0F);
                poseStack.popPose();
                bufferSource.endBatch();
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
                GL11.glPolygonOffset(1f, 1000000F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            }
        }
    }





}
