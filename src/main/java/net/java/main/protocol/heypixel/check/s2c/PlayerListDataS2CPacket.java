package net.java.main.protocol.heypixel.check.s2c;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.EncryptDataC2SPacket;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.utils.HeypixelVarUtils;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.ArrayList;
import java.util.List;



public class PlayerListDataS2CPacket extends HeypixelCheckPacket {


    public List<byte[]> playerList = new ArrayList<>();


    public int playerCount;

    public PlayerListDataS2CPacket(ByteBuf buf) {
        int readVarInt = HeypixelVarUtils.readVarInt(buf);
        for (int i = 0; i < readVarInt; i++) {
            this.playerList.add(helper.readByteArray(buf));
        }
        this.playerCount = HeypixelVarUtils.readVarInt(buf);
    }


    @Override
    public void handleClientSide(AbstractClientPlayer player) {
        manager.setSessionData(this);
        new EncryptDataC2SPacket(getPacketId()).m(manager).sendCheckPacketVanilla();
    }
}
