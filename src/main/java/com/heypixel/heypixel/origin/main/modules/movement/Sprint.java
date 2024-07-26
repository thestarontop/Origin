package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;


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
                 &&   !(Math.abs(Mth.wrapDegrees(mc.player.getYRot()) - Mth.wrapDegrees(RotationUtils.serverRotation.getYaw())) > 60));
    }



}
