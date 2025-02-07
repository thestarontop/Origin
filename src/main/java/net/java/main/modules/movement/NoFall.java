package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;

public class NoFall extends Module {
    public NoFall(){super("NoFall","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.player.fallDistance >= 6f){
            mc.player.setPos(mc.player.getX(),mc.player.getY()+0.00015,mc.player.getZ());
        }
    }
}
