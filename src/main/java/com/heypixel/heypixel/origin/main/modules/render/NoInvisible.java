package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
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
