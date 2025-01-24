package net.java.main.protocol.heypixel.check.s2c;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.EncryptDataC2SPacket;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.msgpack.core.MessagePack;

import java.io.IOException;
import java.util.UUID;


public class UnknownS2CPacket extends HeypixelCheckPacket {


    public UUID uuid;
    public Long field_9461;

    public UnknownS2CPacket(ByteBuf buf) {
        try(var unpacker = MessagePack.newDefaultUnpacker(helper.readByteArray(buf))) {
            var obj = unpacker.unpackValue();
            var obj1 = unpacker.unpackValue();
            this.uuid = UUID.fromString(obj.asRawValue().asString());
            this.field_9461 = obj1.asIntegerValue().asLong();
            new EncryptDataC2SPacket(getPacketId()).m(manager).sendCheckPacket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
