package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class Sneak extends Module {
    public Sneak(){super("Sneak","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));
    }
}
