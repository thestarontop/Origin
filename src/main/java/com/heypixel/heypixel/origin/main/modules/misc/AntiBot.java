package com.heypixel.heypixel.origin.main.modules.misc;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.world.entity.Entity;

import java.util.LinkedList;
import java.util.UUID;

public class AntiBot extends Module {
    public AntiBot(){super("AntiBot","bzd",Category.MISC);}
    private static LinkedList<UUID> duplicate = new LinkedList<>();
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundPlayerInfoPacket packet){
            if (packet.getAction() == ClientboundPlayerInfoPacket.Action.ADD_PLAYER){
                for (ClientboundPlayerInfoPacket.PlayerUpdate playerUpdate : packet.getEntries()) {
                    if (playerUpdate != null) {
                        if (mc.player.distanceTo(mc.level.getPlayerByUUID(playerUpdate.getProfile().getId())) <= 3) {
                            if (!duplicate.contains(playerUpdate.getProfile().getId())) {
                                duplicate.add(playerUpdate.getProfile().getId());
                            }
                        }else{
                            if (duplicate.contains(playerUpdate.getProfile().getId())){
                                duplicate.remove(playerUpdate.getProfile().getId());
                            }
                        }
                    }
                }
            }
        }
    }
    public static boolean isBot(AbstractClientPlayer entity){
        return duplicate.contains(entity.getGameProfile().getId());
    }
    public void onEnable() {
        duplicate.clear();
        Origin.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN +  "AntiBot Was Enabled");
    }

    public void onDisable() {
        duplicate.clear();
        Origin.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED  + "AntiBot Was Disabled");
    }
}
