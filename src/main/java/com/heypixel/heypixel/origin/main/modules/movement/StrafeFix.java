package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.JumpEvent;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.StrafeEvent;
import com.heypixel.heypixel.origin.main.utils.Rotation;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class StrafeFix extends Module {
    public StrafeFix(){super("StrafeFix","bzd",Category.MOVEMENT);}



    @EventTarget
    public void onStrafe(StrafeEvent event){
        event.setVelocity(RotationUtils.fixVelocity(event.getVelocity(), event.getMovementinput(), event.getSpeed()));
    }

    @EventTarget
    public void onJump(JumpEvent event){
        event.setYaw(RotationUtils.serverRotation.getYaw());
    }
    @Override
    public void onEnable(){
        Origin.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "StrafeFix Was Enabled");
    }
    @Override
    public void onDisable(){
        Origin.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + "StrafeFix Was Disabled");
    }
}
