package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Fireball;

public class AntiFireBall extends Module {
    public AntiFireBall() {
        super("AntiFireBall","Anti Fireball attack you", Module.Category.COMBAT);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) return;

        Iterable<Entity> entitiylist = mc.level.entitiesForRendering();
        for (Entity entity:entitiylist){
            if(entity instanceof Fireball && entity.distanceTo(mc.player) <= 5){
                mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,false));
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            }
        }
        }
    }

