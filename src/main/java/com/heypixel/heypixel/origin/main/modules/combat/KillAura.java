package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.AttackEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.misc.MidClick;
import com.heypixel.heypixel.origin.main.modules.misc.Teams;
import com.heypixel.heypixel.origin.main.utils.Rotation;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import com.heypixel.heypixel.origin.main.Origin;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;

import java.util.Iterator;
import java.util.LinkedList;

import static com.heypixel.heypixel.origin.main.modules.misc.AntiBot.isBot;


public class KillAura extends Module {
    public KillAura() {
        super("KillAura","KillAura", Module.Category.COMBAT);
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
            if(entity1 instanceof EndCrystal){
                double range = 3.2;
                boolean canReach = mc.player.distanceTo(entity1) <= range;
                if (canReach){
                    attackEntity(entity1);
                }
            }
            if (entity1 instanceof AbstractClientPlayer entity) {
                double range = 3.2;
                boolean canReach = mc.player.distanceTo(entity) <= range;

                if (canReach && isEnemy(entity) && entity.getId() != mc.player.getId()) {
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
                            boundingBox = boundingBox.expandTowards(0.0,0.1,0.0);


                        Rotation rotation = RotationUtils.lockView(boundingBox,false,true,true,false,4F).getRotation();
                        RotationUtils.setTargetRotation(rotation);
                        if(ticks >= delay) {
                            attackEntity(entity);
                            if (CanReachEntities.size() >= 2) {
                                AttackedEntities.add(entity);
                            }
                            ticks = 0;
                        }
                        break;
                    }
                }
            }



        if(AttackedEntities.size() >= CanReachEntities.size()){
            AttackedEntities.clear();
        }

    }







    /**
     * Check if [entity] is selected as enemy with current target options and other modules
     */
    private static boolean isEnemy(AbstractClientPlayer entity) {
        return (entity.getHealth() > 0 && !Teams.isTeammate(entity)) && !isBot(entity) && !MidClick.isFriend(entity);
    }

    /**
     * Attack [entity]
     */
    private static void attackEntity(Entity entity) {
        Origin.getInstance().getEventManager().call(new AttackEvent(entity));
        mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,false));
        mc.player.swing(InteractionHand.MAIN_HAND);

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
