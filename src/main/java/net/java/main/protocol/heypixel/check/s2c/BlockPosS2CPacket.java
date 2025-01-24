package net.java.main.protocol.heypixel.check.s2c;

import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.protocol.heypixel.check.EncryptDataC2SPacket;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.check.c2s.BlockStateC2SPacket;
import net.java.main.protocol.heypixel.msgpack.core.MessagePack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;

import java.io.IOException;



public class BlockPosS2CPacket extends HeypixelCheckPacket {


    public BlockPos blockPos;

    public BlockPosS2CPacket(ByteBuf buf) {
        try(var unpacker = MessagePack.newDefaultUnpacker(helper.readByteArray(buf))) {
            var uuid = unpacker.unpackValue();
            var posX = unpacker.unpackValue();
            var posY = unpacker.unpackValue();
            var posZ = unpacker.unpackValue();
            if (uuid.asStringValue().equals(Heypixel.get().getPlayerUUID())) {
                this.blockPos = new BlockPos(
                    posX.asIntegerValue().toInt(),
                    posY.asIntegerValue().toInt(),
                    posZ.asIntegerValue().toInt()
                );
            } else {
                this.blockPos = new BlockPos(
                    unpacker.unpackValue().asIntegerValue().toInt(),
                    unpacker.unpackValue().asIntegerValue().toInt(),
                    unpacker.unpackValue().asIntegerValue().toInt()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void handleClientSide(AbstractClientPlayer player) {
        BlockStateC2SPacket.asyncCheck(manager, this.blockPos);
        new EncryptDataC2SPacket(getPacketId()).m(manager).sendCheckPacket();
    }
}
