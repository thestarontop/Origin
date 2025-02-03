package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.mixins.acesser.ServerboundMovePlayerPacketAcesser;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Fly extends Module {
    public Fly(){super("Fly","Creatively",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.player.getAbilities().mayfly = true;
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundMovePlayerPacket){
            ServerboundMovePlayerPacketAcesser packet = (ServerboundMovePlayerPacketAcesser) event.getPacket();
            packet.setOnGround(false);
        }
    }
}
