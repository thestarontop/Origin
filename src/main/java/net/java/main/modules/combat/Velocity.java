/*
 * Not open src
 * LOL
 */
package net.java.main.modules.combat;


import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.movement.NoSlow;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.utils.MSTimer;
import net.java.main.utils.PacketUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity","bzd",Module.Category.COMBAT);
        this.addValues(mode);
    }
    public ListValue mode = new ListValue("mode",new String[]{"noxz","cancel"},"noxz");
    private boolean a = false;
    int grimTCancel = 0;
    int updates = 0;
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("delayvelocity").isEnabled()) return;


        if((!NoSlow.shouldnoslow && mc.player.isUsingItem())){
            return;
        }
        if (mode.getValue() == "noxz") {
            boolean reduced = false;
            if (mc.player.hurtTime == 9) {
                if (KillAura.target != null) {
                    reduce(KillAura.target);
                    reduced = true;
                }
            }
            if (!reduced) {
                for (Entity entity : mc.level.entitiesForRendering()) {
                    if (entity instanceof Projectile && mc.player.hurtTime == 9 && mc.player.distanceTo(entity) >= 8.0) {
                        reduce(entity);
                        reduced = true;
                        break;
                    }
                }
            }
            if (!reduced) {
                for (Entity entity : mc.level.entitiesForRendering()) {
                    if ((mc.player.hurtTime == 9 && entity instanceof Player && mc.player.distanceTo(entity) <= 3.2 && entity.getId() != mc.player.getId())) {
                        reduce(entity);
                        reduced = true;
                        break;
                    }
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof ServerboundPongPacket){
            if (!mc.options.keyUp.isDown() && a) {
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                a = false;
            }
        }
        if (event.getPacket() instanceof ServerboundPlayerCommandPacket packet && packet.getAction().equals(ServerboundPlayerCommandPacket.Action.STOP_SPRINTING)) {
            a = false;
        }

    }
    public void reduce(Entity entity){
        for (int i = 0; i < 5; i++) {
            if (!mc.player.isSprinting() && !a) {
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                a = true;
            }
            var boundingBox = entity.getBoundingBox().expandTowards(0.0,1.0,0.0);


            if(mc.player.getY() - entity.getY() <= 0.25)
                boundingBox = boundingBox.expandTowards(0.0,-3.0,0.0);


            if(mc.player.getY() - entity.getY() >= 0.25)
                boundingBox = boundingBox.expandTowards(0.0,0.1,0.0);

            if (entity != KillAura.target || entity instanceof Player) {
                Rotation.VecRotation prevrotation = RotationUtils.lockView(boundingBox, false, true, true, false, 4F);
                if (prevrotation == null) return;
                Rotation rotation = prevrotation.getRotation();
                RotationUtils.setTargetRotation(rotation);
            }
            boolean bl = mc.player.isShiftKeyDown();
            if (madebystarontopandfml.getInstance().getModuleManager().getModule("forcesneak").isEnabled())
                bl = true;
            PacketUtils.sendPacketNoEvent(ServerboundInteractPacket.createAttackPacket(entity, bl));
            PacketUtils.sendPacketNoEvent(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x*0.6,mc.player.getDeltaMovement().y,mc.player.getDeltaMovement().z*0.6);
        }
    }

    @Override
    public void onEnable(){
        super.onEnable();
        grimTCancel = 0;
    }
}
