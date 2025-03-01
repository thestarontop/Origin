package net.java.main.modules.movement;

import net.java.main.event.events.StrafeEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.utils.RotationUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;


public class Sprint extends Module {
    public Sprint() {
        super("Sprint","auto sprint", Module.Category.MOVEMENT);
    }
    @EventTarget
    public void onUpdate(StrafeEvent event) {
            mc.options.keySprint.setDown(true);
    }
    public static boolean canSprint() {
        return (mc.player.input.forwardImpulse >= 0.8F && !mc.player.horizontalCollision && (mc.player.getFoodData().getFoodLevel() > 6) && !mc.player.hasEffect(MobEffects.BLINDNESS) &&   !(Math.abs(Mth.wrapDegrees(mc.player.getYRot()) - Mth.wrapDegrees(RotationUtils.serverRotation.getYaw())) > 60) && !madebystarontopandfml.getInstance().getModuleManager().getModule("scaffold").isEnabled());
    }



}
