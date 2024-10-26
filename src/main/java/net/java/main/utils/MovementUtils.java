package net.java.main.utils;



public class MovementUtils extends MinecraftInstance{
    public static void strafe(float speed) {
        if (!mc.player.input.down || !mc.player.input.up || !mc.player.input.left || !mc.player.input.right) return;
        double yaw = RotationUtils.movingYaw();
        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(-Math.sin(yaw) * speed,0,Math.cos(yaw) * speed));
    }
    public static boolean isMoving(){
        return mc.player.getDeltaMovement().x != 0.0 || mc.player.getDeltaMovement().z != 0.0;
    }

}
