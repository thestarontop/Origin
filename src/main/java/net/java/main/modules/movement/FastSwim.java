package net.java.main.modules.movement;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.MovementUtils;

public class FastSwim extends Module {
    public FastSwim(){super("FastSwim","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (!mc.player.isInWater()) return;

        MovementUtils.strafe(0.0574f);
        mc.player.setDeltaMovement(mc.player.getDeltaMovement().x,0,mc.player.getDeltaMovement().z);
        if (mc.options.keyJump.isDown()){
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,0.3610,0));
            return;
        }
        if (mc.options.keyShift.isDown()){
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,0.3999,0));
            return;
        }
    }
}
