/*
 * Not open src
 * LOL
 */
package com.mojang.main.modules.combat;


import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import com.mojang.main.utils.Rotation;
import com.mojang.main.utils.RotationUtils;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

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
            entity) <= 3.2 && entity.getId() != mc.player.getId()
            )) {
            if (KillAura.target != null){
                entity = KillAura.target;
            }
            for (int i = 0;i<5;i++){
            if (!mc.player.isSprinting() && !a) {
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
            a = true;
            }

                var boundingBox = entity.getBoundingBox().expandTowards(0.0,1.0,0.0);


                if(mc.player.getY() - entity.getY() <= 0.25)
                    boundingBox = boundingBox.expandTowards(0.0,-3.0,0.0);


                if(mc.player.getY() - entity.getY() >= 0.25)
                    boundingBox = boundingBox.expandTowards(0.0,0.1,0.0);

                Rotation rotation = RotationUtils.lockView(boundingBox,false,true,true,false,4F).getRotation();
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