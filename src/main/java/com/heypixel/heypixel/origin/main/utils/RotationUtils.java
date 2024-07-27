/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package com.heypixel.heypixel.origin.main.utils;


import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.TickEvent;
import com.heypixel.heypixel.origin.mixins.MixinServerboundMovePlayerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class RotationUtils extends MinecraftInstance{
    public RotationUtils() {
        Origin.getInstance().getEventManager().register(this);
    }
    private static final Random random = new Random();

    private static int keepLength;
    private static int revTick;

    public static Rotation targetRotation;
    public static Rotation serverRotation = new Rotation(0F, 0F);


    private static double x = random.nextDouble();
    private static double y = random.nextDouble();
    private static double z = random.nextDouble();

    public static Rotation OtherRotation(final AABB bb, final Vec3 vec, final boolean predict, final boolean throughWalls, final float distance) {
        final Vec3 eyesPos = new Vec3(Minecraft.getInstance().player.getX(), Minecraft.getInstance().player.getBoundingBox().minY +
                Minecraft.getInstance().player.getEyeHeight(), Minecraft.getInstance().player.getZ());
        final Vec3 eyes = Minecraft.getInstance().player.getEyePosition(1F);
        Rotation.VecRotation vecRotation = null;
        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if(throughWalls || isVisible(vec3)) {
                        final Rotation.VecRotation currentVec = new Rotation.VecRotation(vec3, rotation);

                        if (vecRotation == null)
                            vecRotation = currentVec;
                    }
                }
            }
        }

        if(predict) eyesPos.add(Minecraft.getInstance().player.getDeltaMovement().x, Minecraft.getInstance().player.getDeltaMovement().y, Minecraft.getInstance().player.getDeltaMovement().z);

        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        return new Rotation(Mth.wrapDegrees(
                (float) Math.toDegrees(atan2(diffZ, diffX)) - 90F
        ), Mth.wrapDegrees(
                (float) (-Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ))))
        ));


    }
    public static Rotation.VecRotation faceBlock(final BlockPos blockPos) {
        if (blockPos == null)
            return null;

        Rotation.VecRotation vecRotation = null;

        for (double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
            for (double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                    final Vec3 eyesPos = new Vec3(Minecraft.getInstance().player.getX(), Minecraft.getInstance().player.getBoundingBox().minY + Minecraft.getInstance().player.getEyeHeight(), Minecraft.getInstance().player.getZ());
                    final Vec3 posVec = new Vec3(blockPos.getX(),blockPos.getY(),blockPos.getZ()).add(xSearch, ySearch, zSearch);
                    final double dist = eyesPos.distanceTo(posVec);

                    final double diffX = posVec.x - eyesPos.x;
                    final double diffY = posVec.y - eyesPos.y;
                    final double diffZ = posVec.z - eyesPos.z;

                    final double diffXZ = sqrt(diffX * diffX + diffZ * diffZ);

                    final Rotation rotation = new Rotation(
                            Mth.wrapDegrees((float) Math.toDegrees(atan2(diffZ, diffX)) - 90F),
                            Mth.wrapDegrees((float) -Math.toDegrees(atan2(diffY, diffXZ)))
                    );

                    final Vec3 rotationVector = getVectorForRotation(rotation);
                    final Vec3 vector = eyesPos.add(rotationVector.x * dist, rotationVector.y * dist,
                            rotationVector.z * dist);
                    BlockHitResult obj = Minecraft.getInstance().level.clip(new ClipContext(eyesPos,vector, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE,null));

                    if (obj != null && obj.getType() == BlockHitResult.Type.BLOCK) {
                        final Rotation.VecRotation currentVec = new Rotation.VecRotation(posVec, rotation);

                        if (vecRotation == null || getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }
    public static float getYawToEntity(final LivingEntity entity) {
        final LocalPlayer player = Minecraft.getInstance().player;
        return getYawBetween(player.getYRot(), player.getX(), player.getZ(), entity.getX(), entity.getZ());
    }
    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return yaw + Mth.wrapDegrees(var1 - yaw);
    }
    public static Rotation.VecRotation lockView(final AABB bb, final boolean outborder, final boolean random,
                                                final boolean predict, final boolean throughWalls, final float distance) {
        if (outborder) {
            final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return new Rotation.VecRotation(vec3, toRotation(vec3, predict));
        }

        final Vec3 randomVec = new Vec3(bb.minX + (bb.maxX - bb.minX) * x * 0.8, bb.minY + (bb.maxY - bb.minY) * y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * z * 0.8);
        final Rotation randomRotation = toRotation(randomVec, predict);

        final Vec3 eyes = Minecraft.getInstance().player.getEyePosition(1F);

        double xMin = 0.0D;
        double yMin = 0.0D;
        double zMin = 0.0D;
        double xMax = 0.0D;
        double yMax = 0.0D;
        double zMax = 0.0D;
        double xDist = 0.0D;
        double yDist = 0.0D;
        double zDist = 0.0D;
        Rotation.VecRotation vecRotation = null;
        xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
        yMin = 0.65D; yMax = 0.75D; yDist = 0.0125D;
        zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
        for(double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);

                    final Rotation rotation = toRotation(vec3, predict);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if (throughWalls || isVisible(vec3)) {
                        final Rotation.VecRotation currentVec = new Rotation.VecRotation(vec3, rotation);

                        if (vecRotation == null || (random ? getRotationDifference(currentVec.getRotation(), randomRotation) < getRotationDifference(vecRotation.getRotation(), randomRotation) : getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation())))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    public static Rotation.VecRotation calculateCenter(final String calMode, final String randMode, final double randomRange, final AABB bb, final boolean predict, final boolean throughWalls) {

        /*if(outborder) {
            final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return new net.ccbluex.liquidbounce.utils.VecRotation(vec3, toRotation(vec3, predict));
        }*/

        //final net.ccbluex.liquidbounce.utils.Rotation randomRotation = toRotation(randomVec, predict);

        Rotation.VecRotation vecRotation = null;

        double xMin = 0.0D;
        double yMin = 0.0D;
        double zMin = 0.0D;
        double xMax = 0.0D;
        double yMax = 0.0D;
        double zMax = 0.0D;
        double xDist = 0.0D;
        double yDist = 0.0D;
        double zDist = 0.0D;

        xMin = 0.15D; xMax = 0.85D; xDist = 0.1D;
        yMin = 0.15D; yMax = 1.00D; yDist = 0.1D;
        zMin = 0.15D; zMax = 0.85D; zDist = 0.1D;

        Vec3 curVec3 = null;

        switch(calMode) {
            case "LiquidBounce":
                xMin = 0.15D; xMax = 0.85D; xDist = 0.1D;
                yMin = 0.15D; yMax = 1.00D; yDist = 0.1D;
                zMin = 0.15D; zMax = 0.85D; zDist = 0.1D;
                break;
            case "Full":
                xMin = 0.00D; xMax = 1.00D; xDist = 0.1D;
                yMin = 0.00D; yMax = 1.00D; yDist = 0.1D;
                zMin = 0.00D; zMax = 1.00D; zDist = 0.1D;
                break;
            case "HalfUp":
                xMin = 0.10D; xMax = 0.90D; xDist = 0.1D;
                yMin = 0.50D; yMax = 0.90D; yDist = 0.1D;
                zMin = 0.10D; zMax = 0.90D; zDist = 0.1D;
                break;
            case "HalfDown":
                xMin = 0.10D; xMax = 0.90D; xDist = 0.1D;
                yMin = 0.10D; yMax = 0.50D; yDist = 0.1D;
                zMin = 0.10D; zMax = 0.90D; zDist = 0.1D;
                break;
            case "CenterSimple":
                xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
                yMin = 0.65D; yMax = 0.75D; yDist = 0.0125D;
                zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
                break;
            case "CenterLine":
                xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
                yMin = 0.10D; yMax = 0.90D; yDist = 0.1D;
                zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
                break;
            case "HeadRange":
                xMin = 0.20;xMax = 0.80;xDist = 0.1;
                yMin = 0.55;yMax = 0.80;yDist = 0.0125;
                zMin = 0.20;zMax = 0.80;zDist = 0.1;
                break;
        }

        for(double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);

                    if(throughWalls) {
                        final Rotation.VecRotation currentVec = new Rotation.VecRotation(vec3, rotation);

                        if (vecRotation == null || (getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))) {
                            vecRotation = currentVec;
                            curVec3 = vec3;
                        }
                    }
                }
            }
        }

        if(vecRotation == null || randMode == "Off")
            return vecRotation;

        double rand1 = random.nextDouble();
        double rand2 = random.nextDouble();
        double rand3 = random.nextDouble();

        final double xRange = bb.maxX - bb.minX;
        final double yRange = bb.maxY - bb.minY;
        final double zRange = bb.maxZ - bb.minZ;
        double minRange = 999999.0D;

        if(xRange<=minRange) minRange = xRange;
        if(yRange<=minRange) minRange = yRange;
        if(zRange<=minRange) minRange = zRange;

        rand1 = rand1 * minRange * randomRange;
        rand2 = rand2 * minRange * randomRange;
        rand3 = rand3 * minRange * randomRange;

        final double xPrecent = minRange * randomRange / xRange;
        final double yPrecent = minRange * randomRange / yRange;
        final double zPrecent = minRange * randomRange / zRange;

        Vec3 randomVec3 = new Vec3(
                curVec3.x - xPrecent * (curVec3.x - bb.minX) + rand1,
                curVec3.y - yPrecent * (curVec3.y - bb.minY) + rand2,
                curVec3.z - zPrecent * (curVec3.z - bb.minZ) + rand3
        );
        switch(randMode) {
            case "Horizonal":
                randomVec3 = new Vec3(
                        curVec3.x - xPrecent * (curVec3.x - bb.minX) + rand1,
                        curVec3.y,
                        curVec3.z - zPrecent * (curVec3.z - bb.minZ) + rand3
                );
                break;
            case "Vertical":
                randomVec3 = new Vec3(
                        curVec3.x,
                        curVec3.y - yPrecent * (curVec3.y - bb.minY) + rand2,
                        curVec3.z
                );
                break;
        }

        final Rotation randomRotation = toRotation(randomVec3, predict);

        vecRotation =  new Rotation.VecRotation(randomVec3, randomRotation);

        return vecRotation;
    }

    public static void setTargetRotationReverse(final Rotation rotation, final int keepLength, final int revTick) {
        if(Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch())
                || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;


        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
        RotationUtils.revTick = revTick+1;
    }
    public static Rotation getRotationsNonLivingEntity(Entity entity) {
        return RotationUtils.getRotations(entity.getX(), entity.getY() + (entity.getBoundingBox().maxY-entity.getBoundingBox().minY)*0.5, entity.getZ());
    }

    public static Rotation getRotations(double posX, double posY, double posZ) {
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        double x = posX - player.getX();
        double y = posY - (player.getY() + (double)player.getEyeHeight());
        double z = posZ - player.getZ();
        double dist = Mth.sqrt((float) (x * x + z * z));
        float yaw = (float)(atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotation(yaw,pitch);
    }




    /**
     * Translate vec to rotation
     *
     * @param vec     target vec
     * @param predict predict new location of your body
     * @return rotation
     */
    public static Rotation toRotation(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(Minecraft.getInstance().player.getX(), Minecraft.getInstance().player.getBoundingBox().minY +
                Minecraft.getInstance().player.getEyeHeight(), Minecraft.getInstance().player.getZ());

        if (predict)
            eyesPos.add(Minecraft.getInstance().player.getDeltaMovement().x, Minecraft.getInstance().player.getDeltaMovement().y, Minecraft.getInstance().player.getDeltaMovement().z);

        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        return new Rotation(Mth.wrapDegrees(
                (float) Math.toDegrees(atan2(diffZ, diffX)) - 90F
        ), Mth.wrapDegrees(
                (float) (-Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }

    /**
     * Get the center of a box
     *
     * @param bb your box
     * @return center of box
     */
    public static Vec3 getCenter(final AABB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    /**
     * Search good center
     *
     * @param bb           enemy box
     * @param outborder    outborder option
     * @param random       random option
     * @param predict      predict option
     * @param throughWalls throughWalls option
     * @return center
     */
    public static Rotation.VecRotation searchCenter(final AABB bb, final boolean outborder, final boolean random, final boolean predict, final boolean throughWalls, final float distance) {
        if (outborder) {
            final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return new Rotation.VecRotation(vec3, toRotation(vec3, predict));
        }

        final Vec3 randomVec = new Vec3(bb.minX + (bb.maxX - bb.minX) * x * 0.8, bb.minY + (bb.maxY - bb.minY) * y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * z * 0.8);
        final Rotation randomRotation = toRotation(randomVec, predict);

        final Vec3 eyes = Minecraft.getInstance().player.getEyePosition(1F);

        Rotation.VecRotation vecRotation = null;

        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if (throughWalls || isVisible(vec3)) {
                        final Rotation.VecRotation currentVec = new Rotation.VecRotation(vec3, rotation);

                        if (vecRotation == null || (random ? getRotationDifference(currentVec.getRotation(), randomRotation) < getRotationDifference(vecRotation.getRotation(), randomRotation) : getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation())))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    /**
     * Calculate difference between the client rotation and your entity
     *
     * @param entity your entity
     * @return difference between rotation
     */
    public static double getRotationDifference(final Entity entity) {
        final Rotation rotation = toRotation(getCenter(entity.getBoundingBox()), true);

        return getRotationDifference(rotation, new Rotation(Minecraft.getInstance().player.getYRot(), Minecraft.getInstance().player.getXRot()));
    }

    /**
     * Calculate difference between the server rotation and your rotation
     *
     * @param rotation your rotation
     * @return difference between rotation
     */
    public static double getRotationDifference(final Rotation rotation) {
        return serverRotation == null ? 0D : getRotationDifference(rotation, serverRotation);
    }

    /**
     * Calculate difference between two rotations
     *
     * @param a rotation
     * @param b rotation
     * @return difference between rotation
     */


    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    /**
     * Limit your rotation using a turn speed
     *
     * @param currentRotation your current rotation
     * @param targetRotation your goal rotation
     * @param turnSpeed your turn speed
     * @return limited rotation
     */
    @NotNull
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)
                ));
    }

    /**
     * Calculate difference between two angle points
     *
     * @param a angle point
     * @param b angle point
     * @return difference between angle points
     */
    public static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    /**
     * Calculate rotation to vector
     *
     * @param rotation your rotation
     * @return target vector
     */
    public static Vec3 getVectorForRotation(final Rotation rotation) {
        float yawCos = (float) Math.cos(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float yawSin = (float) Math.sin(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float pitchCos = (float) -Math.cos(-rotation.getPitch() * 0.017453292F);
        float pitchSin = (float) Math.sin(-rotation.getPitch() * 0.017453292F);
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }


    @EventTarget
    public void onTick(TickEvent event) {
        if(targetRotation != null) {
            keepLength--;

            if (keepLength <= 0) {
                reset();
            }
        }

        if(random.nextGaussian() > 0.8D) x = Math.random();
        if(random.nextGaussian() > 0.8D) y = Math.random();
        if(random.nextGaussian() > 0.8D) z = Math.random();
    }



    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public static void setTargetRotation(final Rotation rotation, final int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch())
                || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        Rotation.fixedSensitivity(rotation,mc.options.sensitivity);
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public static void setTargetRotation(final Rotation rotation) {
        setTargetRotation(rotation, 0);
    }

    /**
     * Handle packet
     *
     * @param event Packet Event
     */

    /**
     * Reset your target rotation
     */
    public static void reset() {
        keepLength = 0;
        targetRotation = null;
    }
    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof ServerboundMovePlayerPacket packet1) {
            if (targetRotation != null  && (targetRotation.getYaw() != serverRotation.getYaw() || targetRotation.getPitch() != serverRotation.getPitch())) {
                MixinServerboundMovePlayerPacket packet = (MixinServerboundMovePlayerPacket) packet1;
                packet.setHasRot(true);
                packet.setYaw(targetRotation.getYaw());
                packet.setPitch(targetRotation.getPitch());
            }

            if (packet1.hasRotation()) {
                serverRotation = new Rotation(packet1.getYRot(0), packet1.getXRot(0));
            }
        }
    }
    public static boolean isVisible(final Vec3 vec3) {
        Minecraft mc = Minecraft.getInstance();
        Level world = mc.level;

        Vec3 eyesPos = new Vec3(mc.player.getX(), mc.player.getBoundingBox().minY + mc.player.getEyeHeight(), mc.player.getZ());
        ClipContext context = new ClipContext(eyesPos, vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, mc.player);
        BlockHitResult result = world.clip(context);
        return result.getType() == BlockHitResult.Type.MISS;
    }
    public static Vec3 fixVelocity(Vec3 currVelocity, Vec3 movementInput, float speed) {
        if (targetRotation != null) {
            float yaw = targetRotation.getYaw();
            double d = movementInput.lengthSqr();

            if (d < 1.0E-7) {
                return Vec3.ZERO;
            } else {
                Vec3 vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).scale(speed);

                float f = Mth.sin(yaw * 0.017453292F);
                float g = Mth.cos(yaw * 0.017453292F);

                return new Vec3(
                        vec3d.x * g - vec3d.z * f,
                        vec3d.y,
                        vec3d.z * g + vec3d.x * f
                );
            }
        }

        return currVelocity;
    }

    public static float movingYaw() {
        return (float) (getMovementDirectionOfInput(mc.player.getYRot()) * 180f / Math.PI);
    }
    public static double getMovementDirectionOfInput(double facingYaw){
        var actualYaw = facingYaw;
        var forward = 1f;

        // Check if client-user tries to walk backwards (+180 to turn around)
        if (mc.player.input.down) {
            actualYaw += 180f;
            forward = -0.5f;
        } else if (mc.player.input.up) {
            forward = 0.5f;
        }

        // Check which direction the client-user tries to walk sideways
        if (mc.player.input.left) {
            actualYaw -= 90f * forward;
        }
        if (mc.player.input.right) {
            actualYaw += 90f * forward;
        }

        return actualYaw;
    }
    public static Direction getBlockPlacementDirection(BlockPos pos) {
        Vec3 playerPosition = mc.player.position();
        double dx = pos.getX() + 0.5 - playerPosition.x;
        double dy = pos.getY() + 0.5 - playerPosition.y;
        double dz = pos.getZ() + 0.5 - playerPosition.z;

        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        double absDz = Math.abs(dz);

        if (absDx > absDy && absDx > absDz) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        } else if (absDz > absDx && absDz > absDy) {
            return dz > 0 ? Direction.NORTH : Direction.SOUTH;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
    public static Rotation getBlockPlacementRotation(BlockPos targetPos) {
        var playerPos = mc.player.position();
        double dx = targetPos.getX() + 0.5 - playerPos.x;
        double dy = targetPos.getY() + 0.5 - (playerPos.y + mc.player.getEyeHeight()); // player eye height is approximately 1.62 blocks above their position
        double dz = targetPos.getZ() + 0.5 - playerPos.z;

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);

        float preferredYaw = 180.0f;
        float actualYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90;

        // Calculate the pitch needed for preferred yaw
        double preferredYawRadians = Math.toRadians(preferredYaw);
        double adjustedDx = Math.cos(preferredYawRadians) * distanceXZ;
        double adjustedDz = Math.sin(preferredYawRadians) * distanceXZ;
        double adjustedDy = targetPos.getY() + 0.5 - (playerPos.y + 1.62);

        float preferredPitch = (float) -(Math.atan2(adjustedDy, distanceXZ) * (180 / Math.PI));

        // Calculate the pitch for actual yaw
        float actualPitch = (float) -(Math.atan2(dy, distanceXZ) * (180 / Math.PI));

        // Decide which yaw to use based on the proximity of actual yaw to preferred yaw
        float yaw = Math.abs(preferredYaw - actualYaw) < 30 ? preferredYaw : actualYaw;
        float pitch = yaw == preferredYaw ? preferredPitch : actualPitch;

        return new Rotation(yaw, pitch);
    }


}

