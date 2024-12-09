package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.value.ListValue;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

public class FalseHand extends Module {
    public FalseHand(){super("FalseHand","bzd",Category.MISC);
    addValues(mode);}
    public ListValue mode = new ListValue("mode",new String[]{"OnlyLeft","Both"},"OnlyLeft");
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundSwingPacket packet){
            if (packet.getHand() == InteractionHand.MAIN_HAND){
                if (mode.getValue() == "OnlyLeft") {
                    event.cancelEvent();
                }
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.OFF_HAND));
            }
        }
    }
}
