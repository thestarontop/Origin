package net.java.main.modules.combat;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;

import java.util.concurrent.LinkedBlockingQueue;

public class DelayVelocity extends Module {
    public DelayVelocity(){super("DelayVelocity","bzd",Category.COMBAT);}
    private LinkedBlockingQueue<ClientboundPingPacket> packets = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundSetEntityMotionPacket> packets2 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundExplodePacket> packets3 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundBlockUpdatePacket> packets4 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundStatusResponsePacket> packets5 = new LinkedBlockingQueue<>();
    @EventTarget
    public void onPacket(PacketEvent event){
        Packet<?> packet = event.getPacket();
        if (packet instanceof ClientboundPingPacket packet1 && packet1.getId() < 0){
            event.cancelEvent();
            packets.add(packet1);
            PacketUtils.sendPacketNoEvent(new ServerboundPongPacket(0));
        }
        if (packet instanceof ServerboundInteractPacket){
            event.cancelEvent();
        }
        if (packet instanceof ClientboundSetEntityMotionPacket packet1){
            event.cancelEvent();
            packets2.add(packet1);
        }
        if (packet instanceof ClientboundExplodePacket packet1){
            event.cancelEvent();
            packets3.add(packet1);
        }
        if (packet instanceof ClientboundBlockUpdatePacket packet1){
            event.cancelEvent();
            packets4.add(packet1);
        }
        if (packet instanceof ClientboundStatusResponsePacket packet1){
            event.cancelEvent();
            packets5.add(packet1);
        }
    }
    public void onDisable() {
        try {
            while (!packets2.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets2.take());
            }
            while (!packets3.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packets3.take());
            }
            //mc2.player.handleStatusUpdate(2.toByte())
            while (!packets4.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packets4.take());
            }
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.take());
            }
            while (!packets5.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets5.take());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.onDisable();
    }
    public void onEnable() {
        super.onEnable();
    }
}
