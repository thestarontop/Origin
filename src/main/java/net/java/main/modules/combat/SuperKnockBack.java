package net.java.main.modules.combat;

import net.java.main.event.events.AttackEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.utils.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;


public class SuperKnockBack extends Module {
    public SuperKnockBack() {
        super("SuperKnockBack","bzd", Module.Category.COMBAT);
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundInteractPacket){
            boolean sprinting = mc.player.isSprinting();
            if (sprinting) {
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.START_SPRINTING));
            }else {
                event.cancelEvent();
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                PacketUtils.sendPacketNoEvent(event.getPacket());
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
            }
        }
    }
}

