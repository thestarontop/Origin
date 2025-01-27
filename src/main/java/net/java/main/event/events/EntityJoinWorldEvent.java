package net.java.main.event.events;


import net.java.main.event.impl.Event;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

import java.util.logging.Level;

public class EntityJoinWorldEvent implements Event {
    private static final EntityJoinWorldEvent INSTANCE = new EntityJoinWorldEvent();

    public Entity entity;
    public ClientLevel world;

    public static EntityJoinWorldEvent get(Entity e, ClientLevel world) {
        INSTANCE.entity = e;
        INSTANCE.world = world;
        return INSTANCE;
    }
}
