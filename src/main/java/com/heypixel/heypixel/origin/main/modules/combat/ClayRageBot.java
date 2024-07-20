package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;


public class ClayRageBot extends Module {
    public ClayRageBot() {
        super("ClayRageBot","Anti Fireball attack you", Module.Category.COMBAT);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null)return;

        for(Entity entity:mc.level.entitiesForRendering()){
            if(entity instanceof AbstractClientPlayer){
                entity.setPos(mc.player.position());
            }
        }
    }
}

