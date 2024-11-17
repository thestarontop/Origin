package net.java.main.utils;



public class MovementUtils extends MinecraftInstance{
    public static void strafe(float speed) {
        if (!isMoving()) return;
        double yaw = RotationUtils.movingYaw();
        mc.player.setDeltaMovement(-Math.sin(yaw) * speed,mc.player.getDeltaMovement().y,Math.cos(yaw) * speed);
    }
    public static boolean isMoving(){
        return mc.player.input.up || mc.player.input.down || mc.player.input.left || mc.player.input.right;
    }

}
