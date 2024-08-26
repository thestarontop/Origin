package com.mojang.main.modules.movement;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.utils.PacketUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;

public class Stuck extends Module {
    public Stuck(){super("Stuck","bzd", Category.MOVEMENT);}
    private Vec3 velocity;
    private LinkedList<Packet<?>> packets = new LinkedList<>();
    @Override
    public void onEnable(){
        madebystarontopandfml.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Stuck Was Enabled");
        this.velocity = mc.player.getDeltaMovement();
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.options.keyUp.setDown(false);
        mc.options.keyDown.setDown(false);
        mc.options.keyLeft.setDown(false);
        mc.options.keyRight.setDown(false);
        mc.player.setDeltaMovement(0,0,0);
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundMovePlayerPacket){
            event.cancelEvent();
        }
        if (event.getPacket() instanceof ServerboundPongPacket){
            event.cancelEvent();
            packets.add(event.getPacket());
        }
        if (event.getPacket() instanceof ClientboundPlayerPositionPacket || event.getPacket() instanceof ClientboundPlayerLookAtPacket){
            velocity = new Vec3(0,0,0);
            this.disable();
        }
        if (event.getPacket() instanceof ServerboundUseItemPacket || event.getPacket() instanceof ServerboundUseItemOnPacket){
            event.cancelEvent();
            PacketUtils.sendPacketNoEvent(new ServerboundMovePlayerPacket.Rot(mc.player.getYRot(),mc.player.getXRot(),mc.player.isOnGround()));
            PacketUtils.sendPacketNoEvent(event.getPacket());
        }
        if (event.getPacket() instanceof ClientboundSetEntityMotionPacket packet){
            if (packet.getId() == mc.player.getId()) {
                velocity = new Vec3(packet.getXa() / 8000, packet.getYa() / 8000, packet.getZa() / 8000);
            }
        }
    }
    @Override
    public void onDisable(){
        try {
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.get(0));
                packets.remove(0);
            }
        }catch (Error e){
            e.printStackTrace();
        }
        mc.player.setDeltaMovement(velocity);
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + "Stuck Was Disabled");
    }
}
