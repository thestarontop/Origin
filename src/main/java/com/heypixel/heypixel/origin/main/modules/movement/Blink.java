package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;

import java.util.LinkedList;

public class Blink extends Module {
    public Blink(){super("Blink","bzd", Module.Category.COMBAT);}
    LinkedList<Packet<?>> packets = new LinkedList<>();
    private boolean disablelogger = true;
    public void onEnable() {
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Blink Was Enabled");
        disablelogger = false;
        Origin.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onPacket(PacketEvent event){
        if (mc.player == null || disablelogger)
            return;

        Packet<?> packet = event.getPacket();
        if (packet instanceof ServerboundInteractPacket ||
            packet instanceof ServerboundSwingPacket ||
            packet instanceof ServerboundPlayerCommandPacket ||
            packet instanceof ServerboundPlayerActionPacket ||
            packet instanceof ServerboundUseItemOnPacket ||
            packet instanceof ServerboundUseItemPacket ||
            packet instanceof ServerboundMovePlayerPacket ||
            packet instanceof ServerboundPongPacket ||
            packet instanceof ServerboundSetCarriedItemPacket ||
            packet instanceof ServerboundCustomPayloadPacket ||
            packet instanceof ServerboundPlayerAbilitiesPacket ||
            packet instanceof ServerboundPlayerInputPacket
        ){
            event.cancelEvent();
            packets.add(packet);
        }
    }
    public void onDisable() {
        ChatManager.sendHotBarChat(ChatFormatting.RED + "Blink Was Disabled");
        blink();
        Origin.getInstance().getEventManager().unregister(this);
    }
    public void blink(){
        try {
            disablelogger = true;
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.get(0));
                packets.remove(0);
            }
        }catch (Error e){
            e.printStackTrace();
        }
    }
}
