package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;

public class Fly extends Module {
    public Fly(){super("Fly","Creatively",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.player.getAbilities().mayfly = true;
    }
}
