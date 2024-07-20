package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;
import net.minecraft.world.entity.Entity;

public class AttackEvent implements Event {
    private Entity entity;
    public AttackEvent(Entity entity){
        this.entity = entity;
    }
    public Entity getEntity() {
        return entity;
    }
}
