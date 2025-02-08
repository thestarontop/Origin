package net.java.main.modules.combat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.command.ChatManager;
import net.java.main.command.commands.Modules;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.misc.AntiBot;
import net.java.main.modules.misc.MidClick;
import net.java.main.modules.misc.Teams;
import net.java.main.utils.*;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LegitAura extends Module {
    public LegitAura() {
        super("LegitAura", "LegitAura", Module.Category.COMBAT);
        addValues(player,mob,animal,cps,range,silentrotation,keeplengh);

    }
    public FloatValue cps = new FloatValue("CPS", 7f, 0.0f, 20f);
    public FloatValue range = new FloatValue("Range", 3.2f, 0.0f, 6.0f);
    public FloatValue keeplengh = new FloatValue("KeepLengh", 0f, 0.0f, 10f);
    public BooleanValue silentrotation = new BooleanValue("SilentRotation",true);
    public static BooleanValue player = new BooleanValue("AttackPlayer",true);
    public static BooleanValue mob = new BooleanValue("AttackMob",true);
    public static BooleanValue animal = new BooleanValue("AttackAnimal",true);
    private MSTimer timer = new MSTimer();
    private LinkedList<Entity> CanReachEntities = new LinkedList<>();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();
    public static LivingEntity target;
    private int ticks = 0;

    /*@EventTarget
    public void onUpdate2(UpdateEvent event) {
            if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) mc.hitResult).getEntity();
                if (entity instanceof AbstractClientPlayer player && !Teams.isTeammate(player)) {
                    if (mc.player.distanceTo(player) <=range.getValue() && shouldAttack()) {
                        madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(entity));
                        if (!sb.getValue()) {
                            mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, false));
                        }else {
                            mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, shouldchange));
                            shouldchange = !shouldchange;
                        }
                        mc.player.swing(InteractionHand.MAIN_HAND);
                        timer.reset();
                    }
                }
            }
        }*/
    private boolean shouldAttack() {
        return timer.hasTimePassed((long) (1000.0D / cps.getValue()));
    }

    @EventTarget
    public void onJump(JumpEvent event){
        if (target != null && madebystarontopandfml.getInstance().getModuleManager().getModule("strafefix").isEnabled()) {
            mc.player.setSprinting(false);
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int delay = 4;
        ticks += 1;
        Iterable<Entity> entitylist = mc.level.entitiesForRendering();
        for (Entity entity1 : entitylist) {

            boolean canReach = mc.player.distanceTo(entity1) <= range.getValue();

            if (canReach && isEnemy(entity1) && entity1.getId() != mc.player.getId()) {
                if (!CanReachEntities.contains(entity1)) {
                    CanReachEntities.add(entity1);
                }
            } else {
                if (CanReachEntities.contains(entity1)) {
                    CanReachEntities.remove(entity1);
                }
            }
            if (entity1.isRemoved()) {
                if (CanReachEntities.contains(entity1)) {
                    CanReachEntities.remove(entity1);
                }
            }
        }
        CanReachEntities.removeIf(entity -> !isInIterable(entity, entitylist));
        if (!CanReachEntities.isEmpty()) {
            CanReachEntities.sort((e1, e2) -> {
                double dist1 = mc.player.distanceTo(e1);
                double dist2 = mc.player.distanceTo(e2);
                return Double.compare(dist1, dist2);
            });
        }
        for (Entity entity : CanReachEntities) {
            if (CanReachEntities.size() >= 2)
                if (AttackedEntities.contains(entity))
                    continue;
            target = (LivingEntity) entity;
            Rotation rotation = RotationUtils.getHVHRotation(target);
            if (silentrotation.getValue()) {
                RotationUtils.setTargetRotation(rotation, keeplengh.getValue().byteValue());
            } else {
                rotation.toPlayer(mc.player);
            }
            if (ticks >= delay) {
                attackEntity(entity);
                if (CanReachEntities.size() >= 2) {
                    AttackedEntities.add(entity);
                }
                ticks = 0;

            }
            break;
        }
        if(AttackedEntities.size() >= CanReachEntities.size()){
            AttackedEntities.clear();
        }
        if (CanReachEntities.isEmpty()) {
            target = null;
        }
    }


    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (target != null) {
            RenderUtils.renderBoundingBox(event.getPoseStack(), target.getBoundingBox(), 0,0,255);
        }
    }
    /**
     * Attack [entity]
     */
    private static void attackEntity(Entity entity) {
        madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(entity));
        mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, true));
        mc.player.swing(InteractionHand.MAIN_HAND);



    }
    public static boolean isInIterable(Entity targetEntity, Iterable<Entity> entityIterable) {
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


    /**
     * Check if [entity] is selected as enemy with current target options and other modules
     */
    public static boolean isEnemy(Entity entity) {
        boolean isliving = false;
        if (entity instanceof LivingEntity){
            if (!((LivingEntity) entity).isDeadOrDying()){
                isliving = true;
            }
        }
        return isliving && ((mob.getValue() && isHostileMob(entity)) || (player.getValue() && entity instanceof AbstractClientPlayer player && !Teams.isTeammate(player)) && !AntiBot.isBot(player) && !MidClick.isFriend(player) || (animal.getValue() && isPassiveMob(entity))
        );
    }

    public static boolean isHostileMob(Entity entity) {
        // Monster 接口包含了大多数敌对生物
        if (entity instanceof Monster) {
            return true;
        }

        // 某些特殊的敌对生物可能需要单独判断
        if (entity instanceof Slime) {
            return true;
        }

        if (entity instanceof Ghast) {
            return true;
        }

        if (entity instanceof Shulker) {
            return true;
        }

        return false;
    }
    public static boolean isPassiveMob(Entity entity) {
        // Animal 类包含了大多数被动生物（如牛、羊、猪等）
        if (entity instanceof Animal) {
            return true;
        }

        // 某些特殊的被动生物需要单独判断
        if (entity instanceof Squid) {
            return true;
        }


        if (entity instanceof Bat) {
            return true;
        }

        if (entity instanceof Dolphin) {
            return true;
        }

        return false;
    }


}