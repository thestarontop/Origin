package net.java.main.utils;

import net.java.main.command.ChatManager;
import net.java.main.madebystarontopandfml;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

import java.util.Objects;

public class PacketUtils extends MinecraftInstance{
    public static boolean pass = false;
    public static void sendPacketNoEvent(Packet packet){
        pass = true;
        if (isCPacket(packet)) {
            mc.getConnection().send(packet);
        }
        if (isSPacket(packet)) {
            packet.handle(Objects.requireNonNull(mc.getConnection().getConnection().getPacketListener()));
        }
        pass = false;
    }
    public static boolean isCPacket(Packet packet){
        return packet.getClass().getName().toLowerCase().startsWith("net.minecraft.network.protocol.game.serverbound");
    }
    public static boolean isSPacket(Packet packet){
        return packet.getClass().getName().toLowerCase().startsWith("net.minecraft.network.protocol.game.clientbound");
    }
    public static boolean isUseFulPacket(Packet packet){
        return isCPacket(packet) || isSPacket(packet);
    }

}
