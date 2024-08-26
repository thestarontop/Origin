package com.mojang.main.modules.movement;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.events.StrafeEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.JumpEvent;
import com.mojang.main.utils.RotationUtils;
import net.minecraft.ChatFormatting;

public class StrafeFix extends Module {
    public StrafeFix(){super("StrafeFix","bzd",Category.MOVEMENT);}



    @EventTarget
    public void onStrafe(StrafeEvent event){
        if (RotationUtils.targetRotation == null) return;

        event.setVelocity(RotationUtils.fixVelocity(event.getVelocity(), event.getMovementinput(), event.getSpeed()));
    }

    @EventTarget
    public void onJump(JumpEvent event){
        if (RotationUtils.targetRotation == null) return;

        event.setYaw(RotationUtils.targetRotation.getYaw());
    }
    @Override
    public void onEnable(){
        madebystarontopandfml.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "StrafeFix Was Enabled");
    }
    @Override
    public void onDisable(){
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + "StrafeFix Was Disabled");
    }
}
