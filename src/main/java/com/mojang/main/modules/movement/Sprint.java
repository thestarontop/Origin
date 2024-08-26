package com.mojang.main.modules.movement;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.utils.RotationUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;


public class Sprint extends Module {
    public Sprint() {
        super("Sprint","auto sprint", Module.Category.MOVEMENT);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
            mc.options.keySprint.setDown(true);
    }
    public static boolean canSprint() {
        return (mc.player.input.forwardImpulse >= 0.8F
                && !mc.player.horizontalCollision
                && (mc.player.getFoodData().getFoodLevel() > 6)
                && !mc.player.hasEffect(MobEffects.BLINDNESS)
                 &&   !(Math.abs(Mth.wrapDegrees(mc.player.getYRot()) - Mth.wrapDegrees(RotationUtils.serverRotation.getYaw())) > 60)
        && !madebystarontopandfml.getInstance().getModuleManager().getModule("scaffold").isEnabled());
    }



}
