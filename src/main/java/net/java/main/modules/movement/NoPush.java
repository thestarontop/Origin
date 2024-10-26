package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.BlockPushOutEvent;
import net.java.main.modules.Module;

public class NoPush extends Module {
    public NoPush(){super("NoPush","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onPushOutOfBlocks(BlockPushOutEvent event){
        event.cancelEvent();
    }
}
