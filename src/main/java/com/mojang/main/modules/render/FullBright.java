package com.mojang.main.modules.render;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;

public class FullBright extends Module {
    public FullBright(){super("FullBright","bzd",Category.RENDER);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.options.gamma = 200;
    }
}
