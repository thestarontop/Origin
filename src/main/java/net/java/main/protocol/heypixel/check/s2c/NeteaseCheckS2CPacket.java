package net.java.main.protocol.heypixel.check.s2c;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;

import java.lang.reflect.Method;



public class NeteaseCheckS2CPacket extends HeypixelCheckPacket {
    public NeteaseCheckS2CPacket(ByteBuf friendlyByteBuf) {
        try {
            Method declaredMethod = Class.forName("com.netease.mc.mod.fullscreenpopup.ToggleFullscreenTransformer").getDeclaredMethod("showGameStorePopup");
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
