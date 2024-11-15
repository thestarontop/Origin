package net.java.main.modules.misc;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.event.events.PacketEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraftforge.network.ICustomPacket;

import java.util.Arrays;

public class POC extends Module {

    public POC() {
        super("POC","idk", Module.Category.MISC);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if(event.getPacket() instanceof ServerboundContainerClickPacket packet){
            ChatManager.sendChat("windowid----"+packet.getContainerId()+"----buttonid----"+packet.getButtonNum()+"----slotid----"+packet.getSlotNum());
        }
    }


}
