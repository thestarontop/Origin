package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Rotation extends Module {
    public Rotation(){super("Rotation","Render Rotations",Category.RENDER);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundMovePlayerPacket packet && packet.hasRotation()){
            mc.player.setYHeadRot(packet.getYRot(0));
            mc.player.setYBodyRot(packet.getYRot(0));
        }
    }
}
