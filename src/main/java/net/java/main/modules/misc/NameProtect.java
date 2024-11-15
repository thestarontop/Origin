package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.TextEvent;
import net.java.main.modules.Module;


public class NameProtect extends Module {
    public NameProtect(){super("NameProtect","bzd",Category.MISC);}
    @EventTarget
    public void onText(TextEvent event){
        event.getText().replace(mc.player.getDisplayName().getString(),"Hidden");
    }
}
