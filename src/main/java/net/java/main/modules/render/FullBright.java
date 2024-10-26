package net.java.main.modules.render;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;

public class FullBright extends Module {
    public FullBright(){super("FullBright","bzd",Category.RENDER);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.options.gamma = 200;
    }
}
