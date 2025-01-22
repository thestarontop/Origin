package net.java.main.event.events;

import net.java.main.event.impl.Event;
import net.minecraft.network.protocol.Packet;

public class PacketEvent implements Event {
    public boolean isCancelled = false;
    private Packet<?> packet;
    public PacketEvent(Packet<?> packet) {
        this.packet=packet;
    }
    public Packet<?> getPacket(){
        return packet;
    }
    public void cancelEvent() {
        isCancelled = true;
    }
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
}
