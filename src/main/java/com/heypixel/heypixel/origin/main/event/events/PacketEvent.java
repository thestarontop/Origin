package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;
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
}
