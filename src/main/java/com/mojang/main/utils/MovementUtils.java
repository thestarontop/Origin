package com.mojang.main.utils;



public class MovementUtils extends MinecraftInstance{
    public static void strafe(float speed) {
        if (!mc.player.input.down || !mc.player.input.up || !mc.player.input.left || !mc.player.input.right) return;
        double yaw = RotationUtils.movingYaw();
        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(-Math.sin(yaw) * speed,0,Math.cos(yaw) * speed));
    }

}
