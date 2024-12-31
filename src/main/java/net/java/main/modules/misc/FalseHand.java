package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.value.ListValue;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

public class FalseHand extends Module {
    public FalseHand(){super("FalseHand","bzd",Category.MISC);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundSwingPacket packet){
            if (packet.getHand() == InteractionHand.MAIN_HAND){
                event.cancelEvent();
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.OFF_HAND));
            }
        }
    }
}
