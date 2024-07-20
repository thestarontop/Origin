package com.heypixel.heypixel.origin.main;

import com.google.common.base.Strings;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

public class Commands {
    public Commands() {
        Origin.getInstance().getEventManager().register(this);
    }
    @EventTarget
    public void checkChat(PacketEvent event) {
        if(event.getPacket() instanceof ServerboundChatPacket packet)
            //every chat messsage sent by the client is checked to see if it matches the clients prefix
            if (packet.getMessage().startsWith(ChatManager.prefix)) {
                event.cancelEvent();
                String OriginalMessage = Strings.nullToEmpty(packet.getMessage());
                // we do this so the player can up arrow to last sent commands because canceling sending the message does not add it to chat history
                Minecraft.getInstance().gui.getChat().addRecentChat(OriginalMessage);
                String[] messageInPieces = OriginalMessage.split(" ");
                ChatManager.sentCommand(messageInPieces);
            }
    }
}
