package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

import java.util.Random;

public class LagFly extends Module {
    public LagFly(){super("LagFly","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        for (int i =1;i<5;i++){
            mc.getConnection().send(new ServerboundMovePlayerPacket.PosRot(mc.player.getX(),mc.player.getY(),mc.player.getZ(),mc.player.getYRot() + 360*i ,mc.player.getXRot(),mc.player.isOnGround()));
        }
    }
    @Override
    public void onDisable(){
        super.onDisable();
    }
}
