package net.java.main.event.events;

import net.java.main.event.impl.Event;

public class KeyPressEvent implements Event {
    private final int key;
    public KeyPressEvent(int key){
        this.key = key;
    }
    public int getKey(){
        return key;
    }
}
