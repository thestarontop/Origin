/*
 * Not open src
 * LOL
 */
package com.heypixel.heypixel.origin.main.modules.combat;


import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.utils.Rotation;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity","bzd",Module.Category.COMBAT);
    }
private boolean a = false;


    @EventTarget
    public void onUpdate(UpdateEvent event){
            Iterable<Entity> entitylist = mc.level.entitiesForRendering();
            for (Entity entity : entitylist) {
            if (mc.player.hurtTime == 9) {
            if ((entity instanceof Player && mc.player.distanceTo(
            entity) <= 3.1 && entity.getId() != mc.player.getId()
            )) {

            for (var i = 0;i<5;i++){
            if (!mc.player.isSprinting() && !a) {
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
            a = true;
            }


            Rotation rotation =
            RotationUtils.toRotation(RotationUtils.getCenter(entity.getBoundingBox().expandTowards(0.0, 1.0, 0.0)), false);
                    RotationUtils.setTargetRotation(rotation);


            mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,false));
            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x*0.6,mc.player.getDeltaMovement().y,mc.player.getDeltaMovement().z*0.6);
            }
            break;
            }


            }
            }



            if(!mc.options.keyUp.isDown() && a){
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
            a = false;
            }
            }
    @EventTarget
    public void onPacket(PacketEvent event) {


            if (event.getPacket() instanceof ServerboundPlayerCommandPacket packet && packet.getAction().equals(ServerboundPlayerCommandPacket.Action.STOP_SPRINTING)) {
            a = false;
            }
            }

            }