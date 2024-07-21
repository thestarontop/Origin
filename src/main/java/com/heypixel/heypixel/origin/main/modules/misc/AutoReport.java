package com.heypixel.heypixel.origin.main.modules.misc;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoReport extends Module {
    public AutoReport(){super("AutoL","bzd", Module.Category.COMBAT);}
    boolean isReportUI;

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
                isReportUI = true;
                ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Has reported player >" + player + "<");
            }

        }
        if (packet instanceof ClientboundOpenScreenPacket){
            if (isReportUI) {
                event.cancelEvent();
                PacketUtils.sendPacketNoEvent(new ServerboundContainerClickPacket(((ClientboundOpenScreenPacket) packet).getContainerId(), -114514, 0, 0, ClickType.PICKUP, new ItemStack(Items.AIR), Int2ObjectMaps.emptyMap()));
                isReportUI = false;
            }
        }

    }
}
