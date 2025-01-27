package net.java.main.protocol.heypixel.check;


import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.msgpack.core.MessageBufferPacker;
import net.java.main.protocol.heypixel.msgpack.value.Variable;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.java.main.protocol.heypixel.utils.EncryptionUtils;
import net.java.main.protocol.heypixel.utils.HeypixelVarUtils;
import net.minecraft.network.FriendlyByteBuf;

public class EncryptDataC2SPacket extends HeypixelCheckPacket {


    public int packetId;


    public EncryptDataC2SPacket(int i) {
        this.packetId = i;
    }

    public EncryptDataC2SPacket(ByteBuf friendlyByteBuf) {
    }

    @Override
    public void writeData(MessageBufferPacker packer) {
        try {
            packer.packValue(new Variable().setStringValue(EncryptionUtils.encryptString(manager, String.valueOf(this.packetId))));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void processBuffer(FriendlyByteBuf friendlyByteBuf, BufferHelper bufferHelper) {
        try {
            bufferHelper.writeString(friendlyByteBuf, EncryptionUtils.encryptString(manager, String.valueOf(this.packetId)));
        } catch (Exception e) {
            HeypixelVarUtils.writeVarLong(friendlyByteBuf, this.packetId);
        }
    }
}
