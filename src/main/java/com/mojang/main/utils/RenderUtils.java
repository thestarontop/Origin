package com.mojang.main.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RenderUtils extends MinecraftInstance{


    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();

    public static void draw3DBox(Matrix4f matrix4f, AABB box, Color color) {
        RenderSystem.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Tesselator tessellator = RenderSystem.renderThreadTesselator();
        RenderSystem.setShader(GameRenderer::getPositionShader);

        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.minZ);

        BufferUploader.end(bufferBuilder);

        RenderSystem.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        RenderSystem.setShader(GameRenderer::getPositionShader);

        bufferBuilder = tessellator.getBuilder();
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.minZ);

        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.maxZ);

        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.minZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.minY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.maxZ);
        bufferBuilder.vertex(matrix4f, (float) box.minX, (float) box.maxY, (float) box.minZ);
        BufferUploader.end(bufferBuilder);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static int getRainbowOpaque(float count) {
        float hue = ((System.currentTimeMillis()) % 2000) /  2000f;
        int color = Color.HSBtoRGB(hue, count, 1);
        return color;
    }

    public static void renderPlayerHitbox(PoseStack poseStack, Entity player, float partialTicks, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        try {
            double x = player.xOld + (player.getX() - player.xOld) * partialTicks;
            double y = player.yOld + (player.getY() - player.yOld) * partialTicks;
            double z = player.zOld + (player.getZ() - player.zOld) * partialTicks;
            poseStack.translate(-x, -y, -z);

            AABB hitbox = player.getBoundingBox().move(-player.getX(), -player.getY(), -player.getZ());

            float red = 1.0F;
            float green = 0.0F;
            float blue = 0.0F;
            float alpha = 1.0F;

            VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());

            addLine(buffer, poseStack, hitbox.minX, hitbox.minY, hitbox.minZ, hitbox.maxX, hitbox.minY, hitbox.minZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.maxX, hitbox.minY, hitbox.minZ, hitbox.maxX, hitbox.minY, hitbox.maxZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.maxX, hitbox.minY, hitbox.maxZ, hitbox.minX, hitbox.minY, hitbox.maxZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.minX, hitbox.minY, hitbox.maxZ, hitbox.minX, hitbox.minY, hitbox.minZ, red, green, blue, alpha);

            addLine(buffer, poseStack, hitbox.minX, hitbox.maxY, hitbox.minZ, hitbox.maxX, hitbox.maxY, hitbox.minZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.maxX, hitbox.maxY, hitbox.minZ, hitbox.maxX, hitbox.maxY, hitbox.maxZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.maxX, hitbox.maxY, hitbox.maxZ, hitbox.minX, hitbox.maxY, hitbox.maxZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.minX, hitbox.maxY, hitbox.maxZ, hitbox.minX, hitbox.maxY, hitbox.minZ, red, green, blue, alpha);

            addLine(buffer, poseStack, hitbox.minX, hitbox.minY, hitbox.minZ, hitbox.minX, hitbox.maxY, hitbox.minZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.maxX, hitbox.minY, hitbox.minZ, hitbox.maxX, hitbox.maxY, hitbox.minZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.maxX, hitbox.minY, hitbox.maxZ, hitbox.maxX, hitbox.maxY, hitbox.maxZ, red, green, blue, alpha);
            addLine(buffer, poseStack, hitbox.minX, hitbox.minY, hitbox.maxZ, hitbox.minX, hitbox.maxY, hitbox.maxZ, red, green, blue, alpha);
        } finally {
            poseStack.popPose();
        }
    }

    private static void addLine(VertexConsumer buffer, PoseStack poseStack, double startX, double startY, double startZ, double endX, double endY, double endZ, float red, float green, float blue, float alpha) {
        buffer.vertex(poseStack.last().pose(), (float) startX, (float) startY, (float) startZ).color(red, green, blue, alpha).normal(0, 1, 0).endVertex();
        buffer.vertex(poseStack.last().pose(), (float) endX, (float) endY, (float) endZ).color(red, green, blue, alpha).normal(0, 1, 0).endVertex();
    }
    public static void renderEntityBoundingBox(PoseStack poseStack, Entity entity) {
        AABB boundingBox = entity.getBoundingBox();
        LevelRenderer.renderLineBox(
                poseStack,
                Tesselator.getInstance().getBuilder(),
                boundingBox.minX - entity.getX(),
                boundingBox.minY - entity.getY(),
                boundingBox.minZ - entity.getZ(),
                boundingBox.maxX - entity.getX(),
                boundingBox.maxY - entity.getY(),
                boundingBox.maxZ - entity.getZ(),
                1.0f, 1.0f, 1.0f, 1.0f
        );
    }
    public static void renderEntityRectangle(PoseStack poseStack, Entity entity, Entity cameraEntity, float partialTicks) {
        Vec3 entityPos = entity.getPosition(partialTicks);
        Vec3 cameraPos = cameraEntity.getPosition(partialTicks);
        double dx = entityPos.x - cameraPos.x;
        double dy = entityPos.y - cameraPos.y;
        double dz = entityPos.z - cameraPos.z;

        poseStack.pushPose();
        poseStack.translate(dx, dy, dz);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tesselator tesselator = Tesselator.getInstance();
        var buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // Define rectangle coordinates
        float width = 1.0f;
        float height = 2.0f;

        buffer.vertex(poseStack.last().pose(), -width / 2, height, 0).color(1.0f, 0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex(poseStack.last().pose(), width / 2, height, 0).color(1.0f, 0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex(poseStack.last().pose(), width / 2, 0, 0).color(1.0f, 0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex(poseStack.last().pose(), -width / 2, 0, 0).color(1.0f, 0.0f, 0.0f, 1.0f).endVertex();

        tesselator.end();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
    public static void renderBoundingBox(PoseStack poseStack, Entity entity, float red, float green, float blue) {
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
        RenderSystem.setShaderColor(red, green, blue, 1.0F);
        LevelRenderer.renderLineBox(poseStack, vertexConsumer, boundingBox.minX, boundingBox.minY, boundingBox.minZ,
                boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        bufferSource.endBatch();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        GL11.glPolygonOffset(1f, 1000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
    }

}
