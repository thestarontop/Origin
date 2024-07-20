package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;

public class KeyPressEvent implements Event {
    private final int key;
    public KeyPressEvent(int key){
        this.key = key;
    }
    public int getKey(){
        return key;
    }
}
