package com.mojang.main.modules.movement;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.main.utils.MovementUtils;

public class FastLadder extends Module {
    public FastLadder(){super("FastLadder","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.player.onClimbable()){
            MovementUtils.strafe(0.057f);
            if(!mc.options.keyShift.isDown()) {
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0, mc.player.getDeltaMovement().z);
            }
            if (mc.options.keyJump.isDown()){
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,0.1786,0));
                return;
            }
        }
    }
}
