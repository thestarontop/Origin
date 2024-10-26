package net.java.main.modules.movement;

import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.StrafeEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.JumpEvent;
import net.java.main.utils.RotationUtils;
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
