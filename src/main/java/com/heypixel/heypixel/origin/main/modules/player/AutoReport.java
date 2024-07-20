package com.heypixel.heypixel.origin.main.modules.player;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoReport extends Module {
    public AutoReport(){super("AutoL","bzd", Module.Category.COMBAT);}

    @EventTarget
    public void onPacket(PacketEvent event){

        Packet packet = event.getPacket();

        if (packet instanceof ClientboundChatPacket){

            String selfName = mc.player.getDisplayName().getString();
            String msg = ((ClientboundChatPacket) packet).getMessage().getString();
            Matcher matcher = Pattern.compile("^(.*?) 被").matcher(msg);
            String player = matcher.group(1);

            if (matcher.find() && !selfName.contains(player)){
                PacketUtils.sendPacketNoEvent(new ServerboundChatPacket("/report " + player + " Hacker"));
                ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Has reported player >" + player + "<");
            }

        }

    }
}
