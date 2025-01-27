package net.java.main.event.events;


import net.java.main.event.impl.Event;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.logging.Level;

public class WorldChangeEvent implements Event {
    private static final WorldChangeEvent INSTANCE = new WorldChangeEvent();

    public ClientLevel world;

    public static WorldChangeEvent get(ClientLevel world) {
        INSTANCE.world = world;
        return INSTANCE;
    }
}
