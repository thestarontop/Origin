package net.java.main.event.events;

import net.java.main.event.impl.Event;
import net.minecraft.network.protocol.Packet;

public class BlockPushOutEvent implements Event {
    public boolean isCancelled = false;
    public BlockPushOutEvent() {

    }
    public void cancelEvent() {
        isCancelled = true;
    }
}
