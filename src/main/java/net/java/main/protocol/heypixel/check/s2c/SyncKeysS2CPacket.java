package net.java.main.protocol.heypixel.check.s2c;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.EncryptDataC2SPacket;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.Arrays;



public class SyncKeysS2CPacket extends HeypixelCheckPacket {


    public byte[] keyListB;


    public byte[] keyListC;


    public byte[] keyB;


    public byte[] keyA;


    public byte[] keyC;


    public byte[] keyListA;


    public SyncKeysS2CPacket(ByteBuf buf) {
        this.keyA = helper.readByteArray(buf);
        this.keyListA = helper.readByteArray(buf);
        this.keyB = helper.readByteArray(buf);
        this.keyListB = helper.readByteArray(buf);
        this.keyC = helper.readByteArray(buf);
        this.keyListC = helper.readByteArray(buf);
    }

    @Override
    public void handleClientSide(AbstractClientPlayer player) {
        manager.processSyncKeys(this);
        new EncryptDataC2SPacket(getPacketId()).m(manager).sendCheckPacketVanilla();
    }

    public String toString() {
        return "SyncKeysPacket(keyA=" +
            Arrays.toString(this.keyA) +
            ", keyListA=" + Arrays.toString(this.keyListA) + ", " +
            "keyB=" + Arrays.toString(this.keyB) +
            ", keyListB=" + Arrays.toString(this.keyListB) +
            ", keyC=" + Arrays.toString(this.keyC) +
            ", keyListC=" + Arrays.toString(this.keyListC) + ")";
    }
}
