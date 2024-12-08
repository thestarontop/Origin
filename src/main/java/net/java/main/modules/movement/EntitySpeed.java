package net.java.main.modules.movement;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.RotationUtils;
import net.java.main.value.FloatValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.java.main.utils.MovementUtils;

public class EntitySpeed extends Module {
    public EntitySpeed(){super("EntitySpeed","bzd",Category.MOVEMENT);
    addValues(addmotion);}
    public FloatValue addmotion = new FloatValue("addmotion",0.01f,0.01f,1f);
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (!MovementUtils.isMoving()) { return;}
        var collisions = 0;
        var box = mc.player.getBoundingBox().expandTowards(0.23,0.23,0.23);
        for (Entity entity:mc.level.entitiesForRendering()) {
            var entityBox = entity.getBoundingBox();
            if (canCauseSpeed(entity) && box.intersects(entityBox)) {
                collisions++;
            }
        }

        // Grim gives 0.08 leniency per entity which is customizable by speed.
        var yaw = Math.toRadians(RotationUtils.getMovementDirectionOfInput(RotationUtils.serverRotation.getYaw()));
        var boost = addmotion.getValue() * collisions;
        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(-Math.sin(yaw) * boost, 0.0, Math.cos(yaw) * boost));
    }
    private boolean canCauseSpeed(Entity entity){
    return entity != mc.player && entity instanceof LivingEntity && !(entity instanceof ArmorStand);
    }


}
