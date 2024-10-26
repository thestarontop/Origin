package net.java.main.modules.movement;

import net.java.main.event.events.PacketEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.UpdateEvent;
import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class SafeWalk extends Module {
    public SafeWalk(){super("SafeWalk","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));
    }
    @Override
    public void onDisable(){
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
        super.onDisable();
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundPlayerCommandPacket packet){
            if(packet.getAction() == ServerboundPlayerCommandPacket.Action.START_SPRINTING || packet.getAction() == ServerboundPlayerCommandPacket.Action.STOP_SPRINTING){
                event.cancelEvent();
            }
        }
    }
}
