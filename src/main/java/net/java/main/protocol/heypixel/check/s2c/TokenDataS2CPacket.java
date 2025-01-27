package net.java.main.protocol.heypixel.check.s2c;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.EncryptDataC2SPacket;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;

import java.nio.charset.StandardCharsets;



public class TokenDataS2CPacket extends HeypixelCheckPacket {
    public byte[] keyA;
    public byte[] ketC;
    public byte[] keyB;

    public TokenDataS2CPacket(FriendlyByteBuf buf) {
        this.keyA = helper.readByteArray(buf);
        this.keyB = helper.readByteArray(buf);
        this.ketC = helper.readByteArray(buf);
    }

    @Override
    public void handleClientSide(AbstractClientPlayer player) {
        var r0 = new String(this.keyA, StandardCharsets.UTF_8);
        var r1 = new String(this.keyB, StandardCharsets.UTF_8);
        var r2 = new String(this.ketC, StandardCharsets.UTF_8);

        var s1 = new String(decodeBase64(r0), StandardCharsets.UTF_8);
        var s2 = new String(decodeBase64(r1), StandardCharsets.UTF_8);
        var s3 = new String(decodeBase64(r2), StandardCharsets.UTF_8);

        var splitStr = " ";
        for (char c : s1.toCharArray()) {
            try {
                Integer.valueOf(c + "");
            } catch (NumberFormatException e) {
                splitStr = String.valueOf(c);
                break;
            }
        }

        String[] split = s1.split(splitStr);
        String[] split2 = s2.split(splitStr);
        String[] split3 = s3.split(splitStr);

        var decoder = manager.messageDecoder;
        manager.tokenMap.put(decoder.decode(split[0]), decoder.decode(split[1]));
        manager.tokenMap.put(decoder.decode(split2[0]), decoder.decode(split2[1]));
        manager.tokenMap.put(decoder.decode(split3[0]), decoder.decode(split3[1]));
        new EncryptDataC2SPacket(getPacketId()).m(manager).sendCheckPacketVanilla();
    }

    private byte[] decodeBase64(String s) {
        return java.util.Base64.getDecoder().decode(s);
    }
}
