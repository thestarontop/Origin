package net.java.main.modules.movement;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.RotationUtils;
import net.java.main.value.FloatValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.java.main.utils.MovementUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class EntitySpeed extends Module {
    public EntitySpeed(){super("EntitySpeed","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (!MovementUtils.isMoving()) { return;}
        var collisions = 0;
        AABB box = expand(encompass(getBoundingBoxFromPosAndSize(mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0.6f, 1.8f)),0.2);
        for (Entity entity:mc.level.entitiesForRendering()) {
            AABB entityBox = entity.getBoundingBox();
            if (canCauseSpeed(entity) && isCollided(box,entityBox)) {
                collisions++;
            }
        }

        // Grim gives 0.05 leniency per entity which is customizable by speed.
        var yaw = Math.toRadians(RotationUtils.getMovementDirectionOfInput(RotationUtils.serverRotation.getYaw()));
        var boost = 0.05 * collisions;
        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(-Math.sin(yaw) * boost, 0.0, Math.cos(yaw) * boost));
    }
    private boolean canCauseSpeed(Entity entity){
    return entity != mc.player && entity instanceof LivingEntity && !(entity instanceof ArmorStand);
    }

    public static AABB getBoundingBoxFromPosAndSizeRaw(double centerX, double minY, double centerZ, float width, float height) {
        double minX = centerX - (width / 2f);
        double maxX = centerX + (width / 2f);
        double maxY = minY + height;
        double minZ = centerZ - (width / 2f);
        double maxZ = centerZ + (width / 2f);

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    public static AABB getBoundingBoxFromPosAndSize(double centerX, double minY, double centerZ, float width, float height) {
        return getBoundingBoxFromPosAndSizeRaw(centerX, minY, centerZ, width , height);
    }
    public AABB encompass(AABB other) {
        AABB firstbox = getBoundingBoxFromPosAndSize(mc.player.xOld,mc.player.yOld,mc.player.zOld,0.6f,1.8f);
        double minX = Math.min(firstbox.minX, other.minX);
        double minY = Math.min(firstbox.minY, other.minY);
        double minZ = Math.min(firstbox.minZ, other.minZ);
        double maxX = Math.max(firstbox.maxX, other.maxX);
        double maxY = Math.max(firstbox.maxY, other.maxY);
        double maxZ = Math.max(firstbox.maxZ, other.maxZ);
        return new AABB(minX,minY,minZ,maxX,maxY,maxZ);
    }
    public AABB expand(AABB aabb,double value) {
        double minX = aabb.minX - value;
        double minY = aabb.minY - value;
        double minZ = aabb.minZ - value;
        double maxX = aabb.maxX + value;
        double maxY = aabb.maxY + value;
        double maxZ = aabb.maxZ + value;
        return new AABB(minX,minY,minZ,maxX,maxY,maxZ);
    }
    public boolean isCollided(AABB now,AABB other) {
        return other.maxX >= now.minX && other.minX <= now.maxX
                && other.maxY >= now.minY && other.minY <= now.maxY
                && other.maxZ >= now.minZ && other.minZ <= now.maxZ;
    }
}
