package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;

public class ColorSigns extends Module {
    public ColorSigns(){super("ColorSigns","bzd",Category.MISC);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundSignUpdatePacket){
            ServerboundSignUpdatePacket packet = (ServerboundSignUpdatePacket) event.getPacket();
            for (int i = 0; i < packet.getLines().length; ++i) {
                String newText = packet.getLines()[i].replaceAll("(?i)§|&([0-9A-FK-OR])", "§§$1$1");
                packet.getLines()[i] = newText;
            }
            event.cancelEvent();
            PacketUtils.sendPacketNoEvent(packet);

        }
    }
}
