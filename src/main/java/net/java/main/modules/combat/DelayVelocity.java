package net.java.main.modules.combat;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.java.main.utils.RenderUtils;
import net.java.main.value.BooleanValue;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.concurrent.LinkedBlockingQueue;

public class DelayVelocity extends Module {
    public DelayVelocity(){super("DelayVelocity","bzd",Category.COMBAT);addValues(explode);}
    private LinkedBlockingQueue<ServerboundPongPacket> packets = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundSetEntityMotionPacket> packets2 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundExplodePacket> packets3 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Packet> packets4 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundEntityEventPacket> packets5 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Packet> packets6 = new LinkedBlockingQueue<>();
    public BooleanValue explode = new BooleanValue("VelocityBeforeExplode",false);
    public static Entity target = null;
    public static AABB box = null;
    @EventTarget
    public void onPacket(PacketEvent event){
        Packet<?> packet = event.getPacket();
        if (packet instanceof ServerboundPongPacket packet1){
            event.cancelEvent();
            packets.add(packet1);
        }
        if (packet instanceof ServerboundInteractPacket && target == null){
            event.cancelEvent();
        }
        if (packet instanceof ClientboundSetEntityMotionPacket packet1 && packet1.getId() == mc.player.getId()){
            event.cancelEvent();
            packets2.add(packet1);
        }
        if (packet instanceof ClientboundExplodePacket packet1){
            event.cancelEvent();
            packets3.add(packet1);
        }
        if (packet instanceof ClientboundSectionBlocksUpdatePacket packet1){
            event.cancelEvent();
            packets4.add(packet1);
        }

        if (packet instanceof ClientboundBlockUpdatePacket || packet instanceof ClientboundBlockBreakAckPacket || packet instanceof ClientboundBlockEventPacket || packet instanceof ClientboundBlockDestructionPacket || packet instanceof ClientboundSetEntityDataPacket){
            event.cancelEvent();
            packets4.add(packet);
        }
        if (packet instanceof ClientboundEntityEventPacket packet1 && packet1.getEntity(mc.level) == mc.player){
            event.cancelEvent();
            packets5.add(packet1);
        }
        if (packet instanceof ClientboundPlayerPositionPacket || packet instanceof ClientboundPlayerLookAtPacket){
            packets6.add(packet);
        }

    }
    @EventTarget
    public void onRender3D(Render3DEvent event){
        if (box == null){
            return;
        }
        RenderUtils.renderBoundingBox(event.getPoseStack(), box, 1F, 0.7529F, 0.7961F);
    }
    public void onDisable() {
        try {
            if (explode.getValue()) {
                while (!packets2.isEmpty()) {
                    PacketUtils.sendPacketNoEvent(packets2.take());
                }
            }
            while (!packets3.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packets3.take());
            }
            if (!explode.getValue()){
                while (!packets2.isEmpty()) {
                    PacketUtils.sendPacketNoEvent(packets2.take());
                }
            }
            while (!packets5.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets5.take());
            }
            while (!packets4.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packets4.take());
            }
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.take());
            }
            while (!packets6.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets6.take());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        target = null;
        super.onDisable();
    }
    public void onEnable() {
        super.onEnable();
        if (KillAura.target != null){
            target = KillAura.target;
            box = target.getBoundingBox();
        }else{
            target = null;
            box = null;
        }
    }
}
