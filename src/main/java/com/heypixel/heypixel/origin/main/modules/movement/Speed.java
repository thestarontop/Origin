package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.client.player.Input;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class Speed extends Module {
    public Speed(){super("Speed","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.player.input.forwardImpulse == 0.0f && mc.player.input.leftImpulse == 0.0f) { return;}
        var collisions = 0;
        var box = mc.player.getBoundingBox().expandTowards(1.0,1.0,1.0);
        for (Entity entity:mc.level.entitiesForRendering()) {
            var entityBox = entity.getBoundingBox();
            if (canCauseSpeed(entity) && box.intersects(entityBox)) {
                collisions++;
            }
        }

        // Grim gives 0.08 leniency per entity which is customizable by speed.
        var yaw = RotationUtils.movingYaw();
        var boost = 0.08 * collisions;

        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(-Math.sin(yaw) * boost, 0.0, Math.cos(yaw) * boost));
    }
    private boolean canCauseSpeed(Entity entity){
    return entity != mc.player && entity instanceof LivingEntity && !(entity instanceof ArmorStand);
    }



}
