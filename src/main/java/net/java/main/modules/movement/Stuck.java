package net.java.main.modules.movement;

import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.java.main.utils.RotationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;

public class Stuck extends Module {
    public Stuck(){super("Stuck","bzd", Category.MOVEMENT);}
    private Vec3 velocity;
    private LinkedList<Packet<?>> packets = new LinkedList<>();
    private float lastyaw = -1;
    private float lastpitch = -1;
    @Override
    public void onEnable(){
        super.onEnable();
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
            this.setEnable(false);
        }
        if (event.getPacket() instanceof ServerboundUseItemPacket || event.getPacket() instanceof ServerboundUseItemOnPacket){
            event.cancelEvent();
            if (lastpitch != mc.player.getXRot() || lastyaw != mc.player.getYRot()) {
                PacketUtils.sendPacketNoEvent(new ServerboundMovePlayerPacket.Rot(mc.player.getYRot(), mc.player.getXRot(), mc.player.isOnGround()));
            }
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
        lastpitch = -1;
        lastyaw = -1;
        super.onDisable();
    }
}
