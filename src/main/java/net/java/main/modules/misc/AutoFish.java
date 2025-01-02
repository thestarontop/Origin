package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;

public class AutoFish extends Module {
    public AutoFish(){super("AutoFish","bzd",Category.MISC);}
    private boolean fishing = false;
    private MSTimer mstimer = new MSTimer();
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundSoundPacket packet){
            if (packet.getSound().getLocation().getPath().equals("entity.fishing_bobber.splash")){
                mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                fishing = false;
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (fishing) return;
        if (mstimer.hasTimePassed(150)){
            if (!fishing){
                mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                fishing = true;
            }
            mstimer.reset();
        }
    }
}
