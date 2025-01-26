package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class AntiAFK extends Module {
    public AntiAFK(){super("AntiAFK","bzd",Category.MISC);}
    @EventTarget
    public void onUpdate(){
        mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(mc.player.getX()+1,mc.player.getY()+1,mc.player.getZ()+1,false));
        mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(mc.player.getX(),mc.player.getY(),mc.player.getZ(),true));
    }
}
