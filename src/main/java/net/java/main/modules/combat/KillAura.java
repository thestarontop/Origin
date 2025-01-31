package net.java.main.modules.combat;

import net.java.main.event.events.Render3DEvent;
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
import net.java.main.utils.MSTimer;
import net.java.main.utils.RenderUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.IntValue;
import net.java.main.value.ListValue;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
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
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.LinkedList;


public class KillAura extends Module {
    public KillAura() {
        super("KillAura","KillAura", Module.Category.COMBAT);
        addValues(range,silentrotation,combatdelay,player,mob,animal,mode,sb,throughwalls);
    }


    public FloatValue range = new FloatValue("Range", 3.2f, 0.0f, 6.0f);
    public BooleanValue silentrotation = new BooleanValue("SilentRotation",true);
    public static BooleanValue player = new BooleanValue("AttackPlayer",true);
    public static BooleanValue mob = new BooleanValue("AttackMob",true);
    public static BooleanValue animal = new BooleanValue("AttackAnimal",true);
    public static BooleanValue combatdelay = new BooleanValue("1.9+CombatDelay",true);
    public static BooleanValue throughwalls = new BooleanValue("ThroughWalls",true);

    public static ListValue mode = new ListValue("type", new String[]{"Interact", "Attack"},"Attack");
    public static BooleanValue sb = new BooleanValue("sb",false);
    public static boolean shouldchange = true;

    private LinkedList<Entity> CanReachEntities = new LinkedList<>();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();
    private int ticks = 0;
    public static Entity target;
    private MSTimer timer = new MSTimer();
    public static LinkedList<AABB>targetaabb= new LinkedList<>();
    public static LinkedList<AABB>attackccbb= new LinkedList<>();

    @EventTarget
    public void onJump(JumpEvent event){
        if (target != null && madebystarontopandfml.getInstance().getModuleManager().getModule("strafefix").isEnabled()) {
            mc.player.setSprinting(false);
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int delay = 2;
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("armorbreaker").isEnabled()){
            delay = 4;
        }
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

        if (!madebystarontopandfml.getInstance().getModuleManager().getModule("backtrack").isEnabled()) {
            Iterable<Entity> entitylist = mc.level.entitiesForRendering();

            for (Entity entity1 : entitylist) {
                if (entity1 instanceof EndCrystal) {
                    boolean canReach = mc.player.distanceTo(entity1) <= range.getValue();
                    if (canReach) {
                        attackEntity(entity1);
                    }
                }

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

                if (entity instanceof LivingEntity entity1) {
                    if (entity1.hurtTime <= 10) {
                        var boundingBox = entity.getBoundingBox();
                        boundingBox = boundingBox.expandTowards(0.0, 2.14, 0.0);

                        if (mc.player.getY() - entity.getY() <= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0, -3.0, 0.0);


                        if (mc.player.getY() - entity.getY() >= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0, 0.1, 0.0);

                        target = entity;
                        Rotation.VecRotation prevrotation = RotationUtils.lockView(boundingBox, false, false, true, throughwalls.getValue(), 4F);
                        if (prevrotation == null) return;
                        Rotation rotation = prevrotation.getRotation();
                        if (silentrotation.getValue()) {
                            RotationUtils.setTargetRotation(rotation);
                        } else {
                            rotation.toPlayer(mc.player);
                        }
                        if ((!combatdelay.getValue() && ticks >= delay) || (combatdelay.getValue() && mc.player.getAttackStrengthScale(0.5f) == 1f)) {
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
        }else {
            Iterable<Entity> entitylist = mc.level.entitiesForRendering();
            for (AABB entity : BackTrack.aabblist){
                Entity entity1 = BackTrack.entitymap.get(entity);
                boolean canReach = distanceTo(entity) <= range.getValue();
                if(entity1 instanceof EndCrystal){
                    if (canReach){
                        attackEntity(entity1);
                    }
                }


                if (canReach && isEnemy(entity1) && entity1.getId() != mc.player.getId()) {
                    if (!targetaabb.contains(entity)) {
                        targetaabb.add(entity);
                    }
                } else {
                    if (targetaabb.contains(entity)) {
                        targetaabb.remove(entity);
                    }
                }
                if (entity1.isRemoved()) {
                    if (targetaabb.contains(entity)) {
                        targetaabb.remove(entity);
                    }
                }


            }
            targetaabb.removeIf(entity -> !isInIterable(BackTrack.entitymap.get(entity), entitylist));


            if (!targetaabb.isEmpty()){
                targetaabb.sort((e1, e2) -> {
                    double dist1 = distanceTo(e1);
                    double dist2 = distanceTo(e2);
                    return Double.compare(dist1, dist2);
                });
            }

            for (AABB entity2: targetaabb) {

                if(targetaabb.size() >= 2)
                    if (attackccbb.contains(entity2))
                        continue;

              Entity entity = BackTrack.entitymap.get(entity2);
                if (entity instanceof LivingEntity entity1) {
                    if (entity1.hurtTime <= 10) {
                        var boundingBox = entity2;
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
                            if (targetaabb.size() >= 2) {
                                attackccbb.add(entity2);
                            }
                            ticks = 0;
                        }
                        break;
                    }
                }
            }

        }
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("backtrack").isEnabled()) {
            if (attackccbb.size() >= targetaabb.size()) {
                attackccbb.clear();

            }
            if (targetaabb.isEmpty()) {
                target = null;
            }
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
            RenderUtils.renderBoundingBox(event.getPoseStack(), target.getBoundingBox(), 1F, 0.1F, 0.1F);
        }
    }
    /**
     * Attack [entity]
     */
    private static void attackEntity(Entity entity) {
        if (mode.getValue() == "Attack")  {
            madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(entity));
            if (!sb.getValue()) {
                mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, mc.player.isShiftKeyDown()));
            }else{
                    mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, shouldchange));
                    shouldchange = !shouldchange;
            }
        }else{
            mc.getConnection().send(ServerboundInteractPacket.createInteractionPacket(entity,mc.player.isShiftKeyDown(),InteractionHand.MAIN_HAND));
        }
        if(combatdelay.getValue()){
            mc.player.resetAttackStrengthTicker();
        }
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
        if (!shouldchange && sb.getValue()){
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
        }
    }
    @Override
    public void onEnable(){
        super.onEnable();
        shouldchange = true;
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
