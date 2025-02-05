package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityType;

public class AntiCrash extends Module {
    public AntiCrash(){super("AntiCrash","bzd",Category.MISC);}
    private int maxArrowSpawn = 100;

    private int tick = 0;
    private int arrowMax = 0;
    private boolean guardianEffect = false;
    @EventTarget
    public void onPacket(PacketEvent event){
        Packet packet1 = event.getPacket();
                if(packet1 instanceof ClientboundPlayerPositionPacket packet){
                    if (Math.abs(packet.getX()) > 3.0E7 || Math.abs(packet.getY()) > 3.0E7 || Math.abs(packet.getZ()) > 3.0E7) {
                        event.cancelEvent();
                    }
                }
        if (packet1 instanceof ClientboundLevelParticlesPacket packet) {
            if (packet.getCount() > 1024) {
                event.cancelEvent();
            }
        }
        if (packet1 instanceof ClientboundExplodePacket packet) {
            if (Math.abs(packet.getKnockbackX()) > 100.0 || Math.abs(packet.getKnockbackY())> 100.0 || Math.abs(packet.getKnockbackZ()) > 100.0) {
                event.cancelEvent();
            }
        }
        if (packet1 instanceof ClientboundGameEventPacket packet) {
            ClientboundGameEventPacket.Type packetEvent = packet.getEvent();
            if (ClientboundGameEventPacket.DEMO_EVENT.equals(packetEvent)) {
                if (!mc.isDemo()) {
                    event.cancelEvent();
                }
            } else if (ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT.equals(packetEvent)) {
                if (!guardianEffect) {
                    guardianEffect = true;
                } else {
                    event.cancelEvent();
                }
            }

        }

        if (packet1 instanceof ClientboundAddEntityPacket packet && packet.getType() == EntityType.ARROW) {
            if (arrowMax++ > 100) {
                event.cancelEvent();
            }
        }

    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (tick++ >= 20) {
            tick = 0;
            arrowMax = 0;
            guardianEffect = false;
        }
    }
}
