package com.mojang.main.event.events;

import com.mojang.main.event.impl.Event;

public class TextEvent implements Event {
    private String text;
    public TextEvent(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
