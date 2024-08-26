package com.mojang.main.modules.movement;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.main.utils.RotationUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class EntitySpeed extends Module {
    public EntitySpeed(){super("EntitySpeed","bzd",Category.MOVEMENT);}
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
        var yaw = Math.toRadians(RotationUtils.getMovementDirectionOfInput(RotationUtils.serverRotation.getYaw()));
        var boost = 0.08 * collisions;

        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(-Math.sin(yaw) * boost, 0.0, Math.cos(yaw) * boost));
    }
    private boolean canCauseSpeed(Entity entity){
    return entity != mc.player && entity instanceof LivingEntity && !(entity instanceof ArmorStand);
    }


}
