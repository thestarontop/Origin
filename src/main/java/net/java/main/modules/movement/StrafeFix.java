package net.java.main.modules.movement;

import net.java.main.event.events.MovementInputEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.StrafeEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.JumpEvent;
import net.java.main.utils.RotationUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

public class StrafeFix extends Module {
    public StrafeFix(){
        super("StrafeFix","bzd",Category.MOVEMENT);
        this.addValues(silent);
    }
    public BooleanValue silent = new BooleanValue("silent",true);

    @EventTarget
    public void onStrafe(StrafeEvent event){
        if (RotationUtils.targetRotation == null) return;

        event.setVelocity(RotationUtils.fixVelocity(event.getVelocity(), event.getMovementinput(), event.getSpeed()));
    }

    @EventTarget
    public void onJump(JumpEvent event){
        if (RotationUtils.targetRotation == null) return;

        event.setYaw(RotationUtils.targetRotation.getYaw());
    }

    @EventTarget
    public void onMovementInput(MovementInputEvent event){
        if (RotationUtils.targetRotation == null || !silent.getValue()) return;

        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double angle = Mth.wrapDegrees(Math.toDegrees(direction(mc.player.getYRot(), forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = Mth.wrapDegrees(Math.toDegrees(direction(RotationUtils.targetRotation.getYaw(), predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }
    public double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}
