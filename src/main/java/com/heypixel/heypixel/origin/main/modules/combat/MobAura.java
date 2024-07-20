package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.AttackEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.utils.Rotation;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import com.heypixel.heypixel.origin.main.Origin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;

import java.util.Iterator;
import java.util.LinkedList;


public class MobAura extends Module {
    public MobAura() {
        super("MobAura","MobAura", Module.Category.COMBAT);
    }



    private LinkedList<Entity> CanReachEntities = new LinkedList<>();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();
    private int ticks = 0;






    /**
     * Update event
     */
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int delay = 2;
        ticks += 1;

        Iterable<Entity> entitylist = mc.level.entitiesForRendering();
        for (Entity entity1 : entitylist){
            if (entity1 instanceof LivingEntity entity) {
                double range = 3.2;
                boolean canReach = mc.player.distanceTo(entity) <= range;
                if (canReach && isEnemy(entity)) {
                    if (!CanReachEntities.contains(entity)) {
                        CanReachEntities.add(entity);
                    }
                } else {
                    if (CanReachEntities.contains(entity)) {
                        CanReachEntities.remove(entity);
                    }
                }
                if (entity.isRemoved()) {
                    if (CanReachEntities.contains(entity)) {
                        CanReachEntities.remove(entity);
                    }
                }
            }

        }
            CanReachEntities.removeIf(entity -> !isInIterable(entity, entitylist));


        if(ticks >= delay){
            for (Entity entity: CanReachEntities) {
                if(CanReachEntities.size() >= 2)
                    if (AttackedEntities.contains(entity))
                        continue;

                if (entity instanceof LivingEntity entity1) {
                    if (entity1.hurtTime <= 10) {
                        var boundingBox = entity.getBoundingBox().expandTowards(0.0,1.0,0.0);


                        if(mc.player.getY() - entity.getY() <= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,-3.0,0.0);


                        if(mc.player.getY() - entity.getY() >= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,0.35,0.0);


                        Rotation rotation = RotationUtils.lockView(boundingBox,false,false,true,false,4F).getRotation();
                        RotationUtils.setTargetRotation(rotation);
                        attackEntity(entity);
                        if (CanReachEntities.size() >= 2) {
                            AttackedEntities.add(entity);
                        }
                        break;
                    }
                }
            }
            ticks -= delay;
        }

        if(AttackedEntities.size() >= CanReachEntities.size()){
            AttackedEntities.clear();
        }

    }







    /**
     * Check if [entity] is selected as enemy with current target options and other modules
     */
    private static boolean isEnemy(LivingEntity entity) {
        EntityType<?> type = entity.getType();
        return (type == EntityType.ZOMBIE ||
                type == EntityType.PILLAGER ||
                type == EntityType.SKELETON ||
                type == EntityType.RAVAGER ||
                type == EntityType.HOGLIN ||
                type == EntityType.ILLUSIONER) && entity.getHealth() > 0;
    }

    /**
     * Attack [entity]
     */
    private static void attackEntity(Entity entity) {
        Origin.getInstance().getEventManager().call(new AttackEvent(entity));
        mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,false));
        mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));

    }
    private static boolean isInIterable(Entity targetEntity, Iterable<Entity> entityIterable) {
        for (Entity entity : entityIterable) {
            if (entity == targetEntity) {
                return true;
            }
        }
        return false;
    }














}
