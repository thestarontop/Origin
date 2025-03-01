package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Damage extends Module {
    public Damage(){super("Damage","bzd",Category.MISC);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.screen instanceof DeathScreen){
            mc.player.respawn();
        }
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();
            PacketUtils.sendPacketNoEvent(new ServerboundMovePlayerPacket.Pos(x, y + 10, z, false));
            PacketUtils.sendPacketNoEvent(new ServerboundMovePlayerPacket.Pos(x, y + 10, z, false));
            PacketUtils.sendPacketNoEvent(new ServerboundMovePlayerPacket.Pos(x, y, z, true));
    }
}
