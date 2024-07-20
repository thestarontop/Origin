package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.MSTimer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;

public class BoatJump extends Module {
    public BoatJump(){super("BoatJump","bzd",Category.MOVEMENT);}
    int jumpState = 1;
    private MSTimer timer = new MSTimer();
    @Override
    public void onEnable() {
        jumpState = 1;
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "BoatJump Was Enabled");
        Origin.getInstance().getEventManager().register(this);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (jumpState == 1) {
            if (timer.hasTimePassed(200)) {
                jumpState = 2;
            }
        } else if (jumpState == 2) {
            var hasBoat = false;
            for (Entity entity:mc.level.entitiesForRendering()) {
                if (entity instanceof Boat && mc.player.distanceTo(entity) < 2) {
                    double radiansYaw = mc.player.getYRot() * Math.PI / 180;
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0.7 * -Math.sin(radiansYaw),0.3,0.3 * Math.cos(radiansYaw)));
                    hasBoat = true;
                    break;
                }
            }
            if (!hasBoat) {
                jumpState = 1;
            }

            timer.reset();
        }
    }
}
