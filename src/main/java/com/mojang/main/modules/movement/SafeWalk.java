package com.mojang.main.modules.movement;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class SafeWalk extends Module {
    public SafeWalk(){super("SafeWalk","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));
        mc.player.input.forwardImpulse = 1f;
    }
    @Override
    public void onDisable(){
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + this.getName() + " Was Disabled");
    }
}
