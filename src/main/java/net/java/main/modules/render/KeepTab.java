package net.java.main.modules.render;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;

public class KeepTab extends Module {
    public KeepTab(){super("KeepTab","bzd",Category.RENDER);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.options.keyPlayerList.setDown(true);
    }
    @Override
    public void onDisable(){
        super.onDisable();
        mc.options.keyPlayerList.setDown(mc.options.keyPlayerList.isDown());
    }
}
