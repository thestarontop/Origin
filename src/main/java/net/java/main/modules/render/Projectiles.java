package net.java.main.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render2DEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.utils.Projectiles.ModRenderType;
import net.java.main.utils.RenderUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL21;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Projectiles extends Module {
    public Projectiles(){super("Projectiles","bzd",Category.RENDER);}
    private static final RenderType BUFFS = ModRenderType.cube(create("textures/point/0.png"));
    private static final int FULL_LIGHT = 15728880;
    @EventTarget
    public void rendersWorldEvent(Render3DEvent event) {
        Player player = mc.player;
        ItemStack itemStack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
        PoseStack matrix = event.getPoseStack();

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(BUFFS);

        Vec3 viewPos = mc.getEntityRenderDispatcher().camera.getPosition();
        Vec3 originPos = new Vec3(player.getX(), player.getEyeY() - (double) 0.1F, player.getZ());


        if (itemStack.getItem() instanceof SnowballItem) {
            float pVelocity = 1.5F;
            float pInaccuracy = 0.0F;
            float gravity = 0.03F;
            Vec3 vec3 = calculateShootVec(player, pVelocity, pInaccuracy);
            ChatManager.sendChat("步骤2");
            renderTracePoint(vec3, originPos, viewPos, matrix, player, builder, gravity, itemStack,event.getTickcounter());
        }
        if (itemStack.getItem() instanceof EggItem) {
            float pVelocity = 1.5F;
            float pInaccuracy = 0.0F;
            float gravity = 0.03F;
            Vec3 vec3 = calculateShootVec(player, pVelocity, pInaccuracy);
            renderTracePoint(vec3, originPos, viewPos, matrix, player, builder, gravity, itemStack,event.getTickcounter());
        }
        if (itemStack.getItem() instanceof ExperienceBottleItem) {
            float pVelocity = 0.7F;
            float pInaccuracy = 0.0F;
            float gravity = 0.07F;
            Vec3 vec3 = calculateShootVec(player, pVelocity, pInaccuracy, -20F);
            renderTracePoint(vec3, originPos, viewPos, matrix, player, builder, gravity, itemStack,event.getTickcounter());
        }
        if (itemStack.getItem() instanceof ThrowablePotionItem) {
            float pVelocity = 0.5F;
            float pInaccuracy = 0.0F;
            float gravity = 0.03F;
            Vec3 vec3 = calculateShootVec(player, pVelocity, pInaccuracy);
            renderTracePoint(vec3, originPos, viewPos, matrix, player, builder, gravity, itemStack,event.getTickcounter());
        }
        if (itemStack.getItem() instanceof EnderpearlItem) {
            float pVelocity = 1.5F;
            float pInaccuracy = 0.0F;
            float gravity = 0.03F;
            Vec3 vec3 = calculateShootVec(player, pVelocity, pInaccuracy);
            renderTracePoint(vec3, originPos, viewPos, matrix, player, builder, gravity, itemStack,event.getTickcounter());
        }
    }
        public static Vec3 calculateShootVec(Player player, double pVelocity, double pInaccuracy) {
            float f = -Mth.sin(player.getYRot() * Mth.DEG_TO_RAD) * Mth.cos(player.getXRot() * Mth.DEG_TO_RAD);
            float f1 = -Mth.sin((player.getXRot()) * Mth.DEG_TO_RAD);
            float f2 = Mth.cos(player.getYRot() * Mth.DEG_TO_RAD) * Mth.cos(player.getXRot() * Mth.DEG_TO_RAD);
            return calculateVec(player, pVelocity, pInaccuracy, new Vec3(f, f1, f2));
        }

        public static Vec3 calculateShootVec(Player player, double pVelocity, double pInaccuracy, float offset) {
            float f = -Mth.sin(player.getYRot() * Mth.DEG_TO_RAD) * Mth.cos(player.getXRot() * Mth.DEG_TO_RAD);
            float f1 = -Mth.sin((player.getXRot() + offset) * Mth.DEG_TO_RAD);
            float f2 = Mth.cos(player.getYRot() * Mth.DEG_TO_RAD) * Mth.cos(player.getXRot() * Mth.DEG_TO_RAD);
            return calculateVec(player, pVelocity, pInaccuracy, new Vec3(f, f1, f2));
        }

        public static Vec3 calculateVec(Player player, double pVelocity, double pInaccuracy, Vec3 base) {

            double f = base.x;
            double f1 = base.y;
            double f2 = base.z;
            return (new Vec3(f, f1, f2)).normalize().add(
                    player.getRandom().nextDouble(0.0D, 0.0172275D * pInaccuracy),
                    player.getRandom().nextDouble(0.0D, 0.0172275D * pInaccuracy),
                    player.getRandom().nextDouble(0.0D, 0.0172275D * pInaccuracy)).scale(pVelocity);
        }
    private void renderTracePoint(Vec3 vec3, Vec3 origin, Vec3 view, PoseStack matrix, Player player, VertexConsumer builder, float gravity, ItemStack stack,float tickcount) {
        ChatManager.sendChat("步骤3");
        boolean renderPlane = true;
        float step = 0.7F, begin = 1, end = 500;


        double xo = 0, yo = 0, zo = 0;
        int count = 0, stp = 4;
        xo = origin.x + vec3.x * begin;
        yo = origin.y + vec3.y * begin - 0.5 * gravity * Math.pow(begin, 2);
        zo = origin.z + vec3.z * begin;

        for (float t = begin; t < end; t += step) {
            int r = 255, g = 255, b = 255, a = 40;
            float hw = (float) 0.1D, h = (float) 0.1D;

            int lr = 255, lg = 255, lb = 255, la = 255;
            float lw = (float) 4D;

            double x = origin.x + vec3.x * t;
            double y = origin.y + vec3.y * t - 0.5 * gravity * Math.pow(t, 2);
            double z = origin.z + vec3.z * t;

            Vec3 pos = new Vec3(x, y, z);
            matrix.pushPose();
            matrix.translate(-view.x, -view.y, -view.z);
            float minU = 0, maxU = 1, minV = 0, maxV = 1;
            matrix.translate(x, y, z);
            drawLineFullLight(matrix, player, xo, yo, zo, x, y, z, count, stp, lr, lg, lb, la, lw,tickcount);
            matrix.popPose();
            if (renderPlane) {
                if (!(player.level.getBlockState(getCorrectPos(player.level, pos)).getBlock() instanceof AirBlock || player.level.getBlockState(getCorrectPos(player.level, pos)).getBlock() instanceof LiquidBlock)) {
                    matrix.pushPose();
                    h = Mth.clamp(h * 0.8F, 0.01F, 10F);
                    hw = Mth.clamp(hw * 4F, 0.01F, 20F);
                    r = 255;
                    g = 0;
                    b = 0;
                    a = 60;
                    y += 0.3F;
                    matrix.translate(-view.x, -view.y, -view.z);
                    matrix.translate(x, y, z);
                    drawCubeFullLight(builder, matrix, 0, 0, 0, hw, h, minU, maxU, minV, maxV, r, g, b, a);
                    matrix.popPose();
                    renderPlane = false;
                }
            }

            if (count % stp == 0) {
                xo = x;
                yo = y;
                zo = z;
            }
            count++;

        }
        ChatManager.sendChat("步骤4");

    }
    public static BlockPos getCorrectPos(Level level, Vec3 vec) {
        int i = Mth.floor(vec.x);
        int j = Mth.floor(vec.y - (double) (1.0E-5F));
        int k = Mth.floor(vec.z);
        BlockPos blockpos = new BlockPos(i, j, k);
        if (level.isEmptyBlock(blockpos)) {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.collisionExtendsVertically(level, blockpos1, null)) {
                return blockpos1;
            }
        }
        return blockpos;
    }
    public static void drawLineFullLight(PoseStack matrix, Player player, double xo, double yo, double zo, double x, double y, double z, int count, int stp, int lr, int lg, int lb, int la, float lw,float tickcount) {
        PoseStack.Pose entry = matrix.last();
        float changeX = (float) (xo - x);
        float changeY = (float) (yo - y);
        float changeZ = (float) (zo - z);
        if (!(changeX == 0 && changeY == 0 && changeZ == 0) && count % stp == 0) {
            float factor = 2F;
            float amplitude = Mth.clamp((factor - player.tickCount -tickcount ) / factor, 0F, 1F);
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
            Tesselator tesselator = RenderSystem.renderThreadTesselator();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.lineWidth(lw);
            bufferbuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
            float rd = 2F;
            for (int p = 0; p < rd; ++p)
                drawLineVertex(changeX, changeY, changeZ, bufferbuilder, entry, p / rd, (p + 1) / rd, amplitude, lr, lg, lb, la);
            tesselator.end();
            RenderSystem.lineWidth(1.0F);
            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
        }
    }
    private static void drawLineVertex(float x, float y, float z, VertexConsumer buffer, PoseStack.Pose normal, float t, float t1, float amplitude, int r, int g, int b, int a) {
        float px = (float) (x * t + amplitude * Math.sin(t * 2F * Math.PI));
        float py = y * t + 0.25F;
        float pz = z * t;
        float nx = (float) (x * t1 + amplitude * Math.sin(t1 * 2F * Math.PI)) - px;
        float ny = y * t1 + 0.25F - py;
        float nz = z * t1 - pz;
        float s = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= s;
        ny /= s;
        nz /= s;
        buffer.vertex(normal.pose(), px, py, pz).color(r, g, b, a).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal.normal(), nx, ny, nz).endVertex();
    }
    public static void drawCubeFullLight(VertexConsumer builder, PoseStack matrix, double x, double y, double z, float hw, float h, float minU, float maxU, float minV, float maxV, int r, int g, int b, int a) {
        drawCubeFlat(builder, matrix, -hw + (float) x, h + (float) y, hw + (float) z, hw + (float) x, h + (float) y, -hw + (float) z, minU, minV, maxU, maxV, r, g, b, a, FULL_LIGHT);
        drawCubeFlat(builder, matrix, -hw + (float) x, -h + (float) y, -hw + (float) z, hw + (float) x, -h + (float) y, hw + (float) z, minU, minV, maxU, maxV, r, g, b, a, FULL_LIGHT);
        drawCubeFace(builder, matrix, hw + (float) x, -h + (float) y, -hw + (float) z, -hw + (float) x, h + (float) y, -hw + (float) z, minU, minV, maxU, maxV, r, g, b, a, FULL_LIGHT);
        drawCubeFace(builder, matrix, -hw + (float) x, -h + (float) y, -hw + (float) z, -hw + (float) x, h + (float) y, hw + (float) z, minU, minV, maxU, maxV, r, g, b, a, FULL_LIGHT);
        drawCubeFace(builder, matrix, -hw + (float) x, -h + (float) y, hw + (float) z, hw + (float) x, h + (float) y, hw + (float) z, minU, minV, maxU, maxV, r, g, b, a, FULL_LIGHT);
        drawCubeFace(builder, matrix, hw + (float) x, -h + (float) y, hw + (float) z, hw + (float) x, h + (float) y, -hw + (float) z, minU, minV, maxU, maxV, r, g, b, a, FULL_LIGHT);
    }

    public static void drawCubeFlat(VertexConsumer builder, PoseStack matrixStackIn, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int r, int g, int b, int a, int packedLight) {
        buildVertex(builder, matrixStackIn, x0, y0, z0, u0, v1, r, g, b, a, packedLight);
        buildVertex(builder, matrixStackIn, x1, y0, z0, u1, v1, r, g, b, a, packedLight);
        buildVertex(builder, matrixStackIn, x1, y1, z1, u1, v0, r, g, b, a, packedLight);
        buildVertex(builder, matrixStackIn, x0, y1, z1, u0, v0, r, g, b, a, packedLight);
    }

    public static void drawCubeFace(VertexConsumer builder, PoseStack matrixStackIn, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int r, int g, int b, int a, int packedLight) {
        buildVertex(builder, matrixStackIn, x0, y0, z0, u0, v1, r, g, b, a, packedLight);
        buildVertex(builder, matrixStackIn, x1, y0, z1, u1, v1, r, g, b, a, packedLight);
        buildVertex(builder, matrixStackIn, x1, y1, z1, u1, v0, r, g, b, a, packedLight);
        buildVertex(builder, matrixStackIn, x0, y1, z0, u0, v0, r, g, b, a, packedLight);
    }
    public static void buildVertex(VertexConsumer builder, PoseStack matrixStackIn, float x, float y, float z, float u, float v, int r, int g, int b, int a, int packedLight) {
        builder.vertex(matrixStackIn.last().pose(), x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixStackIn.last().normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }
    public static ResourceLocation create(String target) {
        return new ResourceLocation("madebystarontopandfml", target);
    }


}
