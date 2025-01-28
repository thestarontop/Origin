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
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
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

import java.util.LinkedList;


public class LegitAura extends Module {
    public LegitAura() {
        super("LegitAura", "LegitAura", Module.Category.COMBAT);
        addValues(player,mob,animal,cps,range,Legit,silentrotation);

    }
    public FloatValue cps = new FloatValue("CPS", 7f, 0.0f, 20f);
    public FloatValue range = new FloatValue("Range", 3.2f, 0.0f, 6.0f);
    public static BooleanValue Legit = new BooleanValue("Legit",false);
    public BooleanValue silentrotation = new BooleanValue("SilentRotation",true);

    public static BooleanValue player = new BooleanValue("AttackPlayer",true);
    public static BooleanValue mob = new BooleanValue("AttackMob",true);
    public static BooleanValue animal = new BooleanValue("AttackAnimal",true);
    public static LivingEntity target;
    private MSTimer timer = new MSTimer();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();

    @EventTarget
    public void onUpdate2(UpdateEvent event) {
        if (Legit.getValue()) {
            if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) mc.hitResult).getEntity();
                if (entity instanceof AbstractClientPlayer player && !Teams.isTeammate(player)) {
                    if (mc.player.distanceTo(player) <= 3.000) {
                        mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, mc.player.isShiftKeyDown()));
                        mc.player.swing(InteractionHand.MAIN_HAND);
                    }
                }
            }
        }
    }


    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player.isUsingItem() && mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.GOLDEN_APPLE)
            return;
        if (Legit.getValue())
            return;
        Iterable<Entity> entitylist = mc.level.entitiesForRendering();
        if (AttackedEntities.size() == 0) {
            AttackedEntities.clear();
            updatetarges();
        }
        if (AttackedEntities.isEmpty())
            target = null;
        Entity entity =  AttackedEntities.get(0);
        AttackedEntities.remove(0);
        for (Entity entity1 : entitylist) {
            if (entity1 instanceof EndCrystal) {
                if (mc.player.distanceTo(entity) <= range.getValue() && shouldAttack()) {
                    mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, mc.player.isShiftKeyDown()));
                    mc.player.swing(InteractionHand.MAIN_HAND);
                    timer.reset();
                }
            }
        }
        if (target!= null && mc.player.distanceTo(entity) <= range.getValue() && shouldAttack()) {
                madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(entity));
                mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, mc.player.isShiftKeyDown()));
                mc.player.swing(InteractionHand.MAIN_HAND);
                timer.reset();

        }

        if (target != null && mc.player.distanceTo(entity) <= range.getValue()) {
            Rotation rotations = RotationUtils.getHVHRotation(entity);

          //  Rotation rotation = new Rotation(MathUtil.interpolateFloat(RotationUtils.serverRotation.getYaw(), RotationUtils.targetRotation.getYaw(), 0.9), MathUtil.interpolateFloat(RotationUtils.serverRotation.getPitch(), RotationUtils.targetRotation.getPitch(), 0.9));
            if (silentrotation.getValue()) {
                RotationUtils.setTargetRotation(rotations);
            }else {
                rotations.toPlayer(mc.player);
            }
        }

        if (AttackedEntities.isEmpty()){
            target = null;
        }

    }


    private boolean shouldAttack() {
        return timer.hasTimePassed((long) (1000.0D / cps.getValue()));
    }





    public void updatetarges(){
        for (Entity entity : mc.level.entitiesForRendering()){
            if (isEnemy(entity) && entity.getId() != mc.player.getId() && mc.player.distanceTo(entity) <= range.getValue()) {
                if (!AttackedEntities.contains(entity)) {
                    AttackedEntities.add(entity);
                }
                target = (LivingEntity) entity;
            }
        }
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
        return isliving && (
                (mob.getValue() && isHostileMob(entity)) ||
                        (player.getValue() && entity instanceof AbstractClientPlayer player && !Teams.isTeammate(player)) && !AntiBot.isBot(player) && !MidClick.isFriend(player) ||
                        (animal.getValue() && isPassiveMob(entity))
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