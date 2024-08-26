package com.mojang.main.modules.render;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import net.minecraft.world.entity.Entity;


public class NoInvisible extends Module {
    public NoInvisible() {
        super("NoInvisible","Render the Invisible Entities", Category.RENDER);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) {return;}
        Iterable<net.minecraft.world.entity.Entity> entitiylist = mc.level.entitiesForRendering();
           for (Entity entity : entitiylist){
              if(entity.isInvisible()){
                 entity.setInvisible(false);
                 }
              }
            }
}
