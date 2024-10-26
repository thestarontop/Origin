package net.java.main.modules.movement;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.MovementUtils;

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
