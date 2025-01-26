package net.java.main.modules.combat;

import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.java.main.utils.PacketUtils;
import net.java.main.utils.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class BackTrack extends Module {
    public BackTrack() {
        super("BackTrack", "bzd", Category.COMBAT);
    }
    public static Map<AABB,Entity> entitymap = new HashMap<>();
    public static LinkedList<AABB>aabblist= new LinkedList<>();
    private LinkedBlockingQueue<ClientboundPingPacket> packets = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundSetEntityMotionPacket> packets2 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundExplodePacket> packets3 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Packet> packets4 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<ClientboundEntityEventPacket> packets5 = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Packet> packets6 = new LinkedBlockingQueue<>();
    private MSTimer timer = new MSTimer();

    @EventTarget
    public void onPacket(PacketEvent event){
        Packet<?> packet = event.getPacket();
        if (packet instanceof ClientboundPingPacket packet1){
            event.cancelEvent();
            packets.add(packet1);
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
        for (AABB entity : aabblist) {
            RenderUtils.renderBoundingBox(event.getPoseStack(), entity, 1F, 0.7529F, 0.7961F);
        }
    }
    public void onDisable() {
        try {
            while (!packets3.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packets3.take());
            }
                while (!packets2.isEmpty()) {
                    PacketUtils.sendPacketNoEvent(packets2.take());
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
        super.onDisable();
    }
    public void onEnable() {
            super.onEnable();
            entitymap.clear();
            aabblist.clear();
            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity == mc.player)
                    continue;
                entitymap.put(entity.getBoundingBox(), entity);
                aabblist.add(entity.getBoundingBox());
            }

    }







}


