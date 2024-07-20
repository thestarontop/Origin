package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;

public class FullBright extends Module {
    public FullBright(){super("FullBright","bzd",Category.RENDER);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.options.gamma = 200;
    }
}
