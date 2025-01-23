package net.java.main.utils.network;


import net.minecraft.network.protocol.Packet;

import static net.java.main.utils.MinecraftInstance.mc;

public class PacketUtils {
    public static void sendNoEvent(Packet packet) {
        mc.player.connection.getConnection().send(packet);
    }
}
