package net.java.main.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render2DEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.modules.Module;
import net.java.main.utils.RenderUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
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
    private static final ResourceLocation TRAJECTORY_TEXTURE = new ResourceLocation("textures/misc/trajectory.png");

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        Player thePlayer = mc.player;
        Level theWorld = mc.level;
        if (thePlayer == null || theWorld == null) return;

        ItemStack heldItem = thePlayer.getMainHandItem();
        if (heldItem == null) return;

        Item item = heldItem.getItem();
        EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();
        boolean isBow = false;
        float motionFactor = 1.5F;
        float motionSlowdown = 0.99F;
        float gravity;
        float size;

        // Check items
        if (item instanceof BowItem) {
            if (!thePlayer.isUsingItem())
                return;

            isBow = true;
            gravity = 0.05F;
            size = 0.3F;

            // Calculate power of bow
            float power = thePlayer.getTicksUsingItem() / 20f;
            power = (power * power + power * 2F) / 3F;
            if (power < 0.1F)
                return;

            if (power > 1F)
                power = 1F;

            motionFactor = power * 3F;
        } else if (item instanceof FishingRodItem) {
            gravity = 0.04F;
            size = 0.25F;
            motionSlowdown = 0.92F;
        } else if (item instanceof SplashPotionItem) {
            gravity = 0.05F;
            size = 0.25F;
            motionFactor = 0.5F;
        } else {
            if (!(item instanceof SnowballItem) && !(item instanceof EnderpearlItem) && !(item instanceof EggItem))
                return;

            gravity = 0.03F;
            size = 0.25F;
        }

        // Yaw and pitch of player
        float yaw = thePlayer.getYRot();
        float pitch = thePlayer.getXRot();

        float yawRadians = yaw / 180f * (float)Math.PI;
        float pitchRadians = pitch / 180f * (float)Math.PI;

        // Positions
        double posX = renderManager.camera.getPosition().x - Math.cos(yawRadians) * 0.16F;
        double posY = renderManager.camera.getPosition().y + thePlayer.getEyeHeight() - 0.10000000149011612;
        double posZ = renderManager.camera.getPosition().z - Math.sin(yawRadians) * 0.16F;

        // Motions
        double motionX = (-Math.sin(yawRadians) * Math.cos(pitchRadians)
                * (isBow ? 1.0 : 0.4));
        double motionY = -Math.sin((pitch +
                (item instanceof SplashPotionItem ? -20 : 0))
                / 180f * Math.PI) * (isBow ? 1.0 : 0.4);
        double motionZ = (Math.cos(yawRadians) * Math.cos(pitchRadians)
                * (isBow ? 1.0 : 0.4));
        double distance = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);

        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= motionFactor;
        motionY *= motionFactor;
        motionZ *= motionFactor;

        // Landing
        HitResult landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuilder();

        // Start drawing of path
        GL21.glDepthMask(false);
        RenderUtils.enableGlCap(GL21.GL_BLEND, GL21.GL_LINE_SMOOTH);
        RenderUtils.disableGlCap(GL21.GL_DEPTH_TEST, GL21.GL_ALPHA_TEST, GL21.GL_TEXTURE_2D);
        GL21.glBlendFunc(GL21.GL_SRC_ALPHA, GL21.GL_ONE_MINUS_SRC_ALPHA);
        GL21.glHint(GL21.GL_LINE_SMOOTH_HINT, GL21.GL_NICEST);
                RenderUtils.glColor(new Color(255, 119, 194, 255));
        GL21.glLineWidth(2f);

        worldRenderer.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION);

        while (!hasLanded && posY > 0.0) {
            // Set pos before and after
            Vec3 posBefore = new Vec3(posX, posY, posZ);
            Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

            // Get landing position
            landingPosition = theWorld.clip(new ClipContext(posBefore, posAfter, ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE, thePlayer));

            // Set pos before and after
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

            // Check if arrow is landing
            if (landingPosition != null) {
                hasLanded = true;
                posAfter = landingPosition.getLocation();
            }

            // Set arrow box
            AABB arrowBox = new AABB(posX - size, posY - size, posZ - size, posX + size,
                    posY + size, posZ + size).inflate(motionX, motionY, motionZ).inflate(1.0, 1.0, 1.0);

            int chunkMinX = (int)Math.floor((arrowBox.minX - 2.0) / 16.0);
            int chunkMaxX = (int)Math.floor((arrowBox.maxX + 2.0) / 16.0);
            int chunkMinZ = (int)Math.floor((arrowBox.minZ - 2.0) / 16.0);
            int chunkMaxZ = (int)Math.floor((arrowBox.maxZ + 2.0) / 16.0);

            // Check which entities colliding with the arrow
            List<Entity> collidedEntities = new ArrayList<>();

            for (int x = chunkMinX; x <= chunkMaxX; x++)
                for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
                    LevelChunk chunk = theWorld.getChunk(x, z);
                    for (Entity entity : chunk.getLevel().getEntities(thePlayer, arrowBox)) {
                        collidedEntities.add(entity);
                    }
                }

            // Check all possible entities
            for (Entity possibleEntity : collidedEntities) {
                if (possibleEntity.isPickable() && possibleEntity != thePlayer) {
                    AABB possibleEntityBoundingBox = possibleEntity.getBoundingBox()
                            .inflate(size, size, size);

                    Optional<Vec3> possibleEntityLanding = possibleEntityBoundingBox
                            .clip(posBefore, posAfter);

                    if (!possibleEntityLanding.isPresent()) continue;

                    hitEntity = true;
                    hasLanded = true;
                    landingPosition = new BlockHitResult(possibleEntityLanding.get(), Direction.UP,
                            new BlockPos(possibleEntityLanding.get()), false);
                }
            }

            // Affect motions of arrow
            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            BlockState blockState = theWorld.getBlockState(new BlockPos(posX, posY, posZ));

            // Check is next position water
            if (blockState.getMaterial() == Material.WATER) {
                // Update motion
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            } else { // Update motion
                motionX *= motionSlowdown;
                motionY *= motionSlowdown;
                motionZ *= motionSlowdown;
            }

            motionY -= gravity;

            // Draw path
            worldRenderer.vertex(posX - renderManager.camera.getPosition().x,
                    posY - renderManager.camera.getPosition().y,
                    posZ - renderManager.camera.getPosition().z).endVertex();
        }

        // End the rendering of the path
        tessellator.end();
        PoseStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushPose();
// 渲染代码
        matrixStack.popPose();
        RenderSystem.applyModelViewMatrix();
        GL21.glTranslated(posX - renderManager.camera.getPosition().x,
                posY - renderManager.camera.getPosition().y,
                posZ - renderManager.camera.getPosition().z);

        if (landingPosition != null) {
            // Switch rotation of hit cylinder of the hit axis
            switch (((BlockHitResult)landingPosition).getDirection().getAxis()) {
                case X:
                    GL21.glRotatef(90F, 0F, 0F, 1F);
                    break;
                case Z:
                    GL21.glRotatef(90F, 1F, 0F, 0F);
                    break;
            }

            // Check if hitting a entity
            if (hitEntity)
                RenderUtils.glColor(new Color(255, 0, 0, 150));
        }

        // Rendering hit cylinder
        GL21.glRotatef(-90F, 1F, 0F, 0F);

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

// Your existing tessellator code here
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

// When setting color, use vertex color instead of glColor
        float[] color = new float[]{1.0f, 0.0f, 0.0f, 1.0f}; // Example color (red)
        for (int i = 0; i < 360; i += 6) {
            float x1 = (float) (Math.cos(i * Math.PI / 180.0) * 0.2);
            float z1 = (float) (Math.sin(i * Math.PI / 180.0) * 0.2);

            float x2 = (float) (Math.cos((i + 6) * Math.PI / 180.0) * 0.2);
            float z2 = (float) (Math.sin((i + 6) * Math.PI / 180.0) * 0.2);

            bufferBuilder.vertex(x1, 0, z1).color(color[0], color[1], color[2], color[3]).endVertex();
            bufferBuilder.vertex(x2, 0, z2).color(color[0], color[1], color[2], color[3]).endVertex();

            bufferBuilder.vertex(x1, 0, z1).color(color[0], color[1], color[2], color[3]).endVertex();
            bufferBuilder.vertex(0, 0.3f, 0).color(color[0], color[1], color[2], color[3]).endVertex();
        }

        tessellator.end();

// After rendering, restore GL state
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        GL21.glPopMatrix();
        GL21.glDepthMask(true);
        RenderUtils.resetCaps();
        GL21.glColor4f(1F, 1F, 1F, 1F);
    }

}
