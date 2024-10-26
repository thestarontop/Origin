package net.java.main.modules.combat;

import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
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

