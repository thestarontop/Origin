package net.java.main.protocol.heypixel.check.c2s;


import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;

public class EmptyC2SPacket extends HeypixelCheckPacket {
    public EmptyC2SPacket(ByteBuf friendlyByteBuf) {
    }
}
