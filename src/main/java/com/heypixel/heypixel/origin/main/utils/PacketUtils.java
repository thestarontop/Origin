package com.heypixel.heypixel.origin.main.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;

public class PacketUtils {
    public static boolean pass = false;
    public static void sendPacketNoEvent(Packet<?> packet){
        pass = true;
        Minecraft.getInstance().getConnection().send(packet);
        pass = false;
    }

}
