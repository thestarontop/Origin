/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package com.heypixel.heypixel.origin.main.utils;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Rotation{

    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Set rotations to [player]
     */
    public void toPlayer(Player player) {
        if (Float.isNaN(yaw) || Float.isNaN(pitch))
            return;



        player.setYRot(yaw);
        player.setXRot(pitch);
    }

    /**
     * Patch gcd exploit in aim
     *
     * @se
     */


    /**
     * Apply strafe to player
     *
     * @author bestnub
     */
    /*public void applyStrafeToPlayer(StrafeEvent event) {
        IEntityPlayer player = mc.getThePlayer();

        int dif = ((WMathHelper.wrapAngleTo180_float(player.getRotationYaw() - this.yaw - 23.5f - 135) + 180) / 45);

        float yaw = this.yaw;

        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();

        float calcForward = 0f;
        float calcStrafe = 0f;

        switch (dif) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
        }

        if (calcForward > 1f || (calcForward < 0.9f && calcForward > 0.3f) || calcForward < -1f || (calcForward > -0.9f && calcForward < -0.3f)) {
            calcForward *= 0.5f;
        }

        if (calcStrafe > 1f || (calcStrafe < 0.9f && calcStrafe > 0.3f) || calcStrafe < -1f || (calcStrafe > -0.9f && calcStrafe < -0.3f)) {
            calcStrafe *= 0.5f;
        }

        float d = calcStrafe * calcStrafe + calcForward * calcForward;

        if (d >= 1.0E-4f) {
            d = (float) Math.sqrt(d);
            if (d < 1.0f) d = 1.0f;
            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            float yawSin = (float) Math.sin(yaw * Math.PI / 180f);
            float yawCos = (float) Math.cos(yaw * Math.PI / 180f);
            player.setMotionX(player.getMotionX() + calcStrafe * yawCos - calcForward * yawSin);
            player.setMotionZ(player.getMotionZ() + calcForward * yawCos + calcStrafe * yawSin);
        }
    }*/

    public static class VecRotation {
        private final Vec3 vec;
        private final Rotation rotation;

        public VecRotation(Vec3 vec, Rotation rotation) {
            this.vec = vec;
            this.rotation = rotation;
        }

        public Vec3 getVec() {
            return vec;
        }

        public Rotation getRotation() {
            return rotation;
        }
    }
    public static class PlaceRotation{
        private final PlaceInfo placeInfo;
        private final Rotation rotation;
        public PlaceRotation(PlaceInfo placeInfo, Rotation rotation){
            this.placeInfo = placeInfo;
            this.rotation = rotation;
        }
        public PlaceInfo getPlaceInfo(){return placeInfo;}
        public Rotation getRotation(){return rotation;}
    }



}