package com.heypixel.heypixel.origin.main.modules.world;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;

public class FastBreak extends Module {
    public FastBreak(){super("FastBreak","Allows you to break blocks faster",Category.WORLD);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundPlayerActionPacket packet){
            if (packet.getAction() == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK){
                mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,packet.getPos(),packet.getDirection()));
            }
        }
    }

}
