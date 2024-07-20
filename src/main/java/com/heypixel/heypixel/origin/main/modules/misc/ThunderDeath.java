package com.heypixel.heypixel.origin.main.modules.misc;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;


public class ThunderDeath extends Module {

    public ThunderDeath() {
        super("ThunderDeath","Spawn a thunder after the Entity death", Module.Category.MISC);
    }

    LightningBolt e;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) return;
        if (e != null) e.tick();
        for (Player p : mc.level.players()) {
            if (p.isDeadOrDying()) {
                e = new LightningBolt(EntityType.LIGHTNING_BOLT, mc.level);
                e.setId(-1746164);
                e.setPos(p.position());
                e.hurtMarked = false;
                e.noPhysics = false;
                e.hasImpulse = true;
                e.level.oThunderLevel = 1;
                e.level.thunderLevel = 1;
                mc.level.putNonPlayerEntity(e.getId(), e);
                e.tick();
            }
        }

    }
}
