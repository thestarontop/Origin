package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;

import java.util.LinkedList;

public class BackTrack extends Module {
    public BackTrack(){super("BackTrack","bzd",Category.COMBAT);}
    LinkedList<Packet<?>> packets = new LinkedList<>();
    LinkedList<Packet<?>> packets2 = new LinkedList<>();
    @EventTarget
    public void onPacket(PacketEvent event){
            Packet<?> packet = event.getPacket();
            if (packet instanceof ServerboundMovePlayerPacket || packet instanceof ServerboundPongPacket) {
                event.cancelEvent();
                packets.add(packet);
            }
            if (packet instanceof ClientboundMoveEntityPacket || packet instanceof ClientboundSetEntityMotionPacket){
                event.cancelEvent();
                packets2.add(packet);
            }
            if (packet instanceof ClientboundPlayerLookAtPacket || packet instanceof ClientboundPlayerPositionPacket) {
                this.disable();
            }



    }
    @Override
    public void onDisable() {
        ChatManager.sendHotBarChat(ChatFormatting.RED + "BackTrack Was Disabled");
        try {
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.get(0));
                packets.remove(0);
            }
            while (!packets2.isEmpty()){
                Packet<?> packet = packets2.get(0);
                if (packet instanceof ClientboundMoveEntityPacket){
                    mc.getConnection().handleMoveEntity((ClientboundMoveEntityPacket) packet);
                }
                if (packet instanceof ClientboundSetEntityMotionPacket){
                    mc.getConnection().handleSetEntityMotion((ClientboundSetEntityMotionPacket) packet);
                }
                packets.remove(0);
            }
        }catch (Error e){
            e.printStackTrace();
        }
        Origin.getInstance().getEventManager().unregister(this);
    }
}
