package net.java.main.modules.combat;

import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
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

