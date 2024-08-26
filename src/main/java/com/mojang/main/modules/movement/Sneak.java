package com.mojang.main.modules.movement;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class Sneak extends Module {
    public Sneak(){super("Sneak","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));
    }
}
