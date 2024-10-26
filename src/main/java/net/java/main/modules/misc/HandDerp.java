package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.world.entity.HumanoidArm;

public class HandDerp extends Module {
    public HandDerp(){super("HandDerp","bzd",Category.MISC);}
    boolean lastright = true;

    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (lastright) {
            mc.getConnection().send(new ServerboundClientInformationPacket(mc.options.languageCode,mc.options.renderDistance,mc.options.chatVisibility,mc.options.chatColors,8,HumanoidArm.LEFT,mc.isTextFilteringEnabled(),mc.options.allowServerListing));
        }else{
            mc.getConnection().send(new ServerboundClientInformationPacket(mc.options.languageCode,mc.options.renderDistance,mc.options.chatVisibility,mc.options.chatColors,8,HumanoidArm.RIGHT,mc.isTextFilteringEnabled(),mc.options.allowServerListing));
        }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundClientInformationPacket packet){
            lastright = packet.mainHand() == HumanoidArm.RIGHT;
        }
    }
}
