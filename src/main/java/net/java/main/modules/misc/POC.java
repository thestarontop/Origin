package net.java.main.modules.misc;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.event.events.PacketEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;

public class POC extends Module {

    public POC() {
        super("POC","idk", Module.Category.MISC);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if(event.getPacket() instanceof ClientboundContainerSetSlotPacket packet){
            if (packet.getSlot() == mc.player.getInventory().selected +36 && packet.getContainerId() == 0){
                ChatManager.sendChat("ojbk");
            }
        }
    }


}
