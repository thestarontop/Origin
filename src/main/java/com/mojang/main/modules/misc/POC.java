package com.mojang.main.modules.misc;

import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

public class POC extends Module {

    public POC() {
        super("POC","idk", Module.Category.MISC);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if(event.getPacket() instanceof ServerboundChatPacket packet) {
            event.cancelEvent();
            mc.player.sendMessage(Component.nullToEmpty(packet.getMessage()), mc.player.getUUID());
        }
    }


}
