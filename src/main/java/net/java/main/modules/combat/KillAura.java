package net.java.main.modules.combat;

import net.java.main.madebystarontopandfml;
import net.java.main.event.events.AttackEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.misc.Teams;
import net.java.main.modules.misc.AntiBot;
import net.java.main.event.events.JumpEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.misc.MidClick;
import net.java.main.modules.world.Breaker;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.IntValue;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.LinkedList;


public class KillAura extends Module {
    public KillAura() {
        super("KillAura","KillAura", Module.Category.COMBAT);
        addValues(range,silentrotation,combatdelay);
    }


    public FloatValue range = new FloatValue("Range", 3.2f, 0.0f, 6.0f);
    public BooleanValue silentrotation = new BooleanValue("SilentRotation",true);
    public static BooleanValue combatdelay = new BooleanValue("1.9+CombatDelay",true);

    private LinkedList<Entity> CanReachEntities = new LinkedList<>();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();
    private int ticks = 0;
    public static Entity target;

    @EventTarget
    public void onJump(JumpEvent event){
        if (target != null) {
            mc.player.setSprinting(false);
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int delay = 2;
        ticks += 1;
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("delayvelocity").isEnabled()) {
            if (DelayVelocity.target != null) {
                if (DelayVelocity.target instanceof Player target) {
                    if (distanceTo(DelayVelocity.box) <= 3.2) {
                        var boundingBox = DelayVelocity.box;
                        boundingBox = boundingBox.expandTowards(0.0, 2.14, 0.0);

                        if(mc.player.getY() - DelayVelocity.box.minY <= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,-3.0,0.0);

                        Rotation.VecRotation prevrotation = RotationUtils.lockView(boundingBox, false, true, true, false, 4F);
                        if (prevrotation == null) return;
                        Rotation rotation = prevrotation.getRotation();
                        if (silentrotation.getValue()) {
                            RotationUtils.setTargetRotation(rotation);
                        } else {
                            rotation.toPlayer(mc.player);
                        }
                        if (target.hurtTime <= 10) {
                            if ((!combatdelay.getValue() && ticks >= delay) || (combatdelay.getValue() && mc.player.getAttackStrengthScale(0.5f) == 1f)) {
                                attackEntity(target);
                                ticks = 0;
                            }
                        }
                    }
                }
            }
            return;
        }
        Iterable<Entity> entitylist = mc.level.entitiesForRendering();
        for (Entity entity1 : entitylist){
            if(entity1 instanceof EndCrystal){
                boolean canReach = mc.player.distanceTo(entity1) <= range.getValue();
                if (canReach){
                    attackEntity(entity1);
                }
            }
            if (entity1 instanceof AbstractClientPlayer entity) {;
                boolean canReach = mc.player.distanceTo(entity) <= range.getValue();

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
                        var boundingBox = entity.getBoundingBox();
                        boundingBox = boundingBox.expandTowards(0.0,2.14,0.0);

                        if(mc.player.getY() - entity.getY() <= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,-3.0,0.0);


                        if(mc.player.getY() - entity.getY() >= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,0.1,0.0);

                        target = entity;
                        Rotation.VecRotation prevrotation = RotationUtils.lockView(boundingBox,false,true,true,false,4F);
                        if(prevrotation == null) return;
                        Rotation rotation = prevrotation.getRotation();
                        if (silentrotation.getValue()) {
                            RotationUtils.setTargetRotation(rotation);
                        }else{
                            rotation.toPlayer(mc.player);
                        }
                        if((!combatdelay.getValue() && ticks >= delay) || (combatdelay.getValue() && mc.player.getAttackStrengthScale(0.5f) == 1f)) {
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
        if (CanReachEntities.isEmpty()) {
            target = null;
        }
    }







    /**
     * Check if [entity] is selected as enemy with current target options and other modules
     */
    private static boolean isEnemy(AbstractClientPlayer entity) {
        return (entity.getHealth() > 0 && !Teams.isTeammate(entity)) && !AntiBot.isBot(entity) && !MidClick.isFriend(entity);
    }

    /**
     * Attack [entity]
     */
    private static void attackEntity(Entity entity) {
        madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(entity));
        mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, false));
        if(combatdelay.getValue()){
            mc.player.resetAttackStrengthTicker();
        }
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
    @Override
    public void onDisable(){
        super.onDisable();
        target = null;
    }


    public float distanceTo(AABB arg) {
        float f = (float)(mc.player.getX() - arg.minX);
        float f1 = (float)(mc.player.getY() - arg.minY);
        float f2 = (float)(mc.player.getZ() - arg.minZ);
        return Mth.sqrt(f * f + f1 * f1 + f2 * f2);
    }











}
