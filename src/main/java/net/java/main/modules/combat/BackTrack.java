package net.java.main.modules.combat;

import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class BackTrack extends Module {
    public BackTrack(){super("BackTrack","bzd",Category.COMBAT);}
    LinkedBlockingQueue<Packet<?>> packets = new LinkedBlockingQueue<>();
    @EventTarget
    public void onPacket(PacketEvent event){
            Packet<?> packet = event.getPacket();
            if (PacketUtils.isUseFulPacket(packet)) {
                if (packet instanceof ServerboundInteractPacket || packet instanceof ServerboundSwingPacket || packet instanceof ServerboundMovePlayerPacket || packet instanceof ServerboundPlayerCommandPacket) {
                    return;
                }
                event.cancelEvent();
                packets.add(packet);
                if (packet instanceof ClientboundPlayerLookAtPacket || packet instanceof ClientboundPlayerPositionPacket) {
                    this.setEnable(false);
                }
            }



    }
    @Override
    public void onDisable() {
        try {
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.take());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.onDisable();
    }
}
